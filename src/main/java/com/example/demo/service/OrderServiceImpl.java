package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.common.projectEnum.OrderStatus;
import com.example.demo.dto.request.OrderBookRequestDTO;
import com.example.demo.dto.request.OrderByDateRequestDTO;
import com.example.demo.dto.request.OrderByStatusRequestDTO;
import com.example.demo.dto.request.OrderRequestDTO;
import com.example.demo.dto.request.OrderStatusUpdateRequestDTO;
import com.example.demo.dto.request.ShoppingCartTotalRequestDTO;
import com.example.demo.dto.response.BookInOrderResponseDTO;
import com.example.demo.dto.response.CustomerUserResponseDTO;
import com.example.demo.dto.response.OrderBillResponseDTO;
import com.example.demo.dto.response.OrderBookResponseDTO;
import com.example.demo.dto.response.OrderResponseDTO;
import com.example.demo.dto.response.ShoppingCartTotalResponseDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderBook;
import com.example.demo.entity.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    // **** (Entity -> DTO conversions)
    private OrderResponseDTO toOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt().toLocalDate());
        dto.setStatus(order.getStatus());
        dto.setCustomerName(order.getUser().getUsername());

        return dto;
    }
    // ---

    //---
    private CustomerUserResponseDTO toCustomerUserResponseDTO(User user){
        CustomerUserResponseDTO customer = new CustomerUserResponseDTO();
        customer.setId(user.getId());
        customer.setUsername(user.getUsername());
        customer.setEmail(user.getEmail());
        customer.setAddress(user.getAddress());
        customer.setTelephoneNumber(user.getTelephoneNumber());
        return customer;
    }
    //---

    //---
     private BookInOrderResponseDTO toBookInOrderResponseDTO(Book book) {
        BookInOrderResponseDTO bookDto = new BookInOrderResponseDTO();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setUnitPrice(book.getUnitPrice());
        return bookDto;
    }
    //---

    //---
    private List<OrderBookResponseDTO> toListOrderBookResponseDTO (List<OrderBook> orderBooks){
        List<OrderBookResponseDTO> listDTOs = new ArrayList<>();

        for (OrderBook orderBook : orderBooks) {
            OrderBookResponseDTO dto = new OrderBookResponseDTO();
            dto.setQuantity(orderBook.getQuantity());
            dto.setBook(toBookInOrderResponseDTO(orderBook.getBook()));

            //calculate subTotal
            double subTotal = orderBook.getQuantity() * orderBook.getBook().getUnitPrice();
            dto.setSubTotal(subTotal);
            
            listDTOs.add(dto);
        }
    
        return listDTOs;
    }
    //---

    // ---
    private OrderBillResponseDTO toOrderBillResponseDTO(Order order) {
        OrderBillResponseDTO billDTO = new OrderBillResponseDTO();
        billDTO.setId(order.getId());
        billDTO.setTotalAmount(order.getTotalAmount());
        billDTO.setCreatedAt(order.getCreatedAt().toLocalDate());
        billDTO.setStatus(order.getStatus());
        billDTO.setCustomer(toCustomerUserResponseDTO(order.getUser()));
        billDTO.setOrderBooks(toListOrderBookResponseDTO(order.getOrderBooks()));

        return billDTO;
    }
    // ---

    private ShoppingCartTotalResponseDTO toShoppingCartTotalResponseDTO(double totalAmount) {

        ShoppingCartTotalResponseDTO cartDTO = new ShoppingCartTotalResponseDTO();
        cartDTO.setTotalAmount(totalAmount);

        return cartDTO;
    }
    // ****

    // *****
    // Helper method to get all books and create OrderBook
    private List<OrderBook> prepareOrderBooks(List<OrderBookRequestDTO> booksDTOs) {
        List<OrderBook> orderBooks = new ArrayList<>();

        for (OrderBookRequestDTO bookDTO : booksDTOs) {
            // Fetch the book
            Book book = bookRepository.findById(bookDTO.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + bookDTO.getBookId()));

            // Check stock
            if (book.getQoh() < bookDTO.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for book: " + book.getTitle());
            }

            // Deduct stock
            book.setQoh(book.getQoh() - bookDTO.getQuantity());

            // Create OrderBook mapping
            OrderBook orderBook = new OrderBook();
            orderBook.setBook(book);
            orderBook.setQuantity(bookDTO.getQuantity());
            orderBooks.add(orderBook);
        }

        return orderBooks;
    }
    // ****

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> orderDTOs = orders.stream().map(this::toOrderResponseDTO).collect(Collectors.toList());
        return orderDTOs;
    }
    // ---

    @Override
    public OrderBillResponseDTO getOrderById(long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Order is not found with id: " + id));

        return toOrderBillResponseDTO(order);
    }
    // ---

    /*
     * @Transactional Annotation: Applied to both methods to define the transaction
     * boundary.
     * 
     * Automatic Rollback: Any exception thrown during the execution of these
     * methods will trigger a rollback.
     * 
     * Database Consistency: If an exception occurs (e.g., IllegalStateException),
     * all changes made to the database within the method will be undone.
     * 
     */
    @Override
    @Transactional
    public OrderResponseDTO saveOrder(OrderRequestDTO orderRequestDTO) {

        // Fetch customer details
        User customer = userRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Customer not found with userId: " + orderRequestDTO.getCustomerId()));

        // Prepare orderBooks
        List<OrderBook> orderBooks = prepareOrderBooks(orderRequestDTO.getBooks());

        // Calculate total amount
        double totalAmount = orderBooks.stream()
                .mapToDouble(orderBook -> orderBook.getBook().getUnitPrice() * orderBook.getQuantity())
                .sum();

        // Create and save order
        Order orderToSave = new Order();
        orderToSave.setTotalAmount(totalAmount);

        // set status to PENDING, when creating a new order
        orderToSave.setStatus(OrderStatus.PENDING);
        orderToSave.setOrderBooks(orderBooks);
        orderToSave.setUser(customer);

        // Persist order and associated orderBooks
        for (OrderBook orderBook : orderBooks) {
            orderBook.setOrder(orderToSave); // Set back-reference to the order
        }

        return toOrderResponseDTO(orderRepository.save(orderToSave));
    }
    // ---

    /*
     * @Transactional Annotation: Applied to both methods to define the transaction
     * boundary.
     * 
     * Automatic Rollback: Any exception thrown during the execution of these
     * methods will trigger a rollback.
     * 
     * Database Consistency: If an exception occurs (e.g., IllegalStateException),
     * all changes made to the database within the method will be undone.
     * 
     */
    @Override
    @Transactional
    public OrderResponseDTO updateOrder(long id, OrderRequestDTO orderRequestDTO) {
        // Fetch existing order
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + id));

        // Check if the status of the order allows updates
        if (existingOrder.getStatus() == OrderStatus.SHIPPED || existingOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot update a shipped or cancelled order.");
        }

        // Adjust stock for books in the old order
        for (OrderBook orderBook : existingOrder.getOrderBooks()) {
            Book book = orderBook.getBook();
            book.setQoh(book.getQoh() + orderBook.getQuantity()); // Return stock
        }

        // Prepare new orderBooks
        List<OrderBook> newOrderBooks = prepareOrderBooks(orderRequestDTO.getBooks());

        // Calculate total amount
        double newTotalAmount = newOrderBooks.stream()
                .mapToDouble(orderBook -> orderBook.getBook().getUnitPrice() * orderBook.getQuantity())
                .sum();

        // ********Update order details *********
        existingOrder.setTotalAmount(newTotalAmount);

        // set status to PENDING, when creating a new order
        existingOrder.setStatus(OrderStatus.PENDING);

        // Set the new books for the order
        existingOrder.getOrderBooks().clear();
        existingOrder.getOrderBooks().addAll(newOrderBooks);
        // ****************************************

        // Ensure bi-directional relationship
        for (OrderBook orderBook : newOrderBooks) {
            orderBook.setOrder(existingOrder);
        }

        // Save and return the updated order
        return toOrderResponseDTO(orderRepository.save(existingOrder));
    }
    // ---

    @Override
    public void deleteOrder(long id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order is not found with id: " + id);
        }

        orderRepository.deleteById(id);
        logger.info("Order with id {} was deleted.", id);
    }
    // ---

    @Override
    public List<OrderResponseDTO> getAllOrdersByUserId(long inputUserId) {
        List<Order> orders = orderRepository.findAllOrdersByUserId(inputUserId);
        List<OrderResponseDTO> orderDTOs = orders.stream().map(this::toOrderResponseDTO).collect(Collectors.toList());
        return orderDTOs;
    }
    // ---

    @Override
    public List<OrderResponseDTO> getAllOrdersByOrderStatus(OrderByStatusRequestDTO orderByStatusRequestDTO) {
        List<Order> orders = orderRepository.findAllOrdersByOrderStatus(orderByStatusRequestDTO.getCheckedStatus());
        List<OrderResponseDTO> orderDTOs = orders.stream().map(this::toOrderResponseDTO).collect(Collectors.toList());
        return orderDTOs;
    }
    // ---

    @Override
    public List<OrderResponseDTO> findAllOrdersByDateAndStatus(OrderByDateRequestDTO orderByDateRequestDTO) {
        List<Order> orders = orderRepository.findAllOrdersByDateAndStatus(orderByDateRequestDTO.getCheckedDate(),
                orderByDateRequestDTO.getCheckedStatus());
        List<OrderResponseDTO> orderDTOs = orders.stream().map(this::toOrderResponseDTO).collect(Collectors.toList());
        return orderDTOs;
    }
    // ---

    @Override
    public OrderResponseDTO updateOrderStatus(long id, OrderStatusUpdateRequestDTO orderStatusUpdateRequestDTO) {

        if (orderStatusUpdateRequestDTO.getNewStatus() == OrderStatus.PENDING) {
            // Status is set to PENDING automatically, when creationg or saving an order.
            throw new IllegalArgumentException("Cannot update the status to PENDING.");
        }

        // get the order details
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + id));

        // Validate the current state and the new state
        if (existingOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot update the status of a cancelled order.");
        }

        if (existingOrder.getStatus() == OrderStatus.DELIVERED
                && orderStatusUpdateRequestDTO.getNewStatus() != OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Cannot change the status of a delivered order.");
        }

        // Restore stock if transitioning to `CANCELLED`
        if (orderStatusUpdateRequestDTO.getNewStatus() == OrderStatus.CANCELLED) {
            for (OrderBook orderBook : existingOrder.getOrderBooks()) {
                Book book = orderBook.getBook();
                book.setQoh(book.getQoh() + orderBook.getQuantity());
            }
        }

        // Update status and save the order
        existingOrder.setStatus(orderStatusUpdateRequestDTO.getNewStatus());
        return toOrderResponseDTO(orderRepository.save(existingOrder));
    }
    // ----

    @Override
    public ShoppingCartTotalResponseDTO calculateTotalAmount(ShoppingCartTotalRequestDTO ShoppingCartTotalRequestDTO) {
        double totalAmount = 0;

        for (OrderBookRequestDTO bookDTO : ShoppingCartTotalRequestDTO.getShoppingCartBooks()) {
            Book book = bookRepository.findById(bookDTO.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + bookDTO.getBookId()));

            if (book.getQoh() < bookDTO.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for book: " + book.getTitle());
            }

            totalAmount += book.getUnitPrice() * bookDTO.getQuantity();
        }

        return toShoppingCartTotalResponseDTO(totalAmount);
    }
    // ----
}
