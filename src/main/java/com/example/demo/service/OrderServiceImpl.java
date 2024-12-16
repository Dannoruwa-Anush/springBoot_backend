package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.common.projectEnum.OrderStatus;
import com.example.demo.dto.request.OrderBookRequestDTO;
import com.example.demo.dto.request.OrderRequestDTO;
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

    // ****
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
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    // ---

    @Override
    public Order getOrderById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order is not found with id: " + id));
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
    public Order saveOrder(OrderRequestDTO orderRequestDTO) {

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
        Order order = new Order();
        order.setTotalAmount(totalAmount);

        // set status to PENDING, when creating a new order
        order.setStatus(OrderStatus.PENDING);
        order.setOrderBooks(orderBooks);
        order.setUser(customer);

        // Persist order and associated orderBooks
        for (OrderBook orderBook : orderBooks) {
            orderBook.setOrder(order); // Set back-reference to the order
        }

        return orderRepository.save(order);
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
    public Order updateOrder(long id, OrderRequestDTO orderRequestDTO) {
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
        return orderRepository.save(existingOrder);
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
    public List<Order> getAllOrdersByUserId(Long inputUserId) {
        return orderRepository.findAllOrdersByUserId(inputUserId);
    }
    // ---

    @Override
    public List<Order> getAllOrdersByOrderStatus(OrderStatus inputOrderStatus) {
        return orderRepository.findAllOrdersByOrderStatus(inputOrderStatus);
    }
    // ---

    @Override
    public List<Order> findAllOrdersByDateAndStatus(LocalDate inputOrderPlacedDate, OrderStatus inputOrderStatus) {
        return orderRepository.findAllOrdersByDateAndStatus(inputOrderPlacedDate, inputOrderStatus);
    }
    // ---

    @Override
    public Order updateOrderStatus(long id, OrderStatus newStatus) {

        if (newStatus == OrderStatus.PENDING) {
            // Status is set to PENDING automatically, when creationg or saving an order.
            throw new IllegalArgumentException("Cannot update the status to PENDING.");
        }

        // get the order details
        Order existingOrder = getOrderById(id);

        // Validate the current state and the new state
        if (existingOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot update the status of a cancelled order.");
        }

        if (existingOrder.getStatus() == OrderStatus.DELIVERED && newStatus != OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Cannot change the status of a delivered order.");
        }

        // Restore stock if transitioning to `CANCELLED`
        if (newStatus == OrderStatus.CANCELLED) {
            for (OrderBook orderBook : existingOrder.getOrderBooks()) {
                Book book = orderBook.getBook();
                book.setQoh(book.getQoh() + orderBook.getQuantity());
            }
        }

        // Update status and save the order
        existingOrder.setStatus(newStatus);
        return orderRepository.save(existingOrder);
    }
    // ----

    @Override
    public double calculateTotalAmount(List<OrderBookRequestDTO> booksDTOs) {
        double totalAmount = 0;

        for (OrderBookRequestDTO bookDTO : booksDTOs) {
            Book book = bookRepository.findById(bookDTO.getBookId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + bookDTO.getBookId()));

            if (book.getQoh() < bookDTO.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for book: " + book.getTitle());
            }

            totalAmount += book.getUnitPrice() * bookDTO.getQuantity();
        }

        return totalAmount;
    }
    // ----
}
