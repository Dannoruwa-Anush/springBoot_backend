package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.common.projectEnum.OrderStatus;
import com.example.demo.dto.request.OrderRequestDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Order;
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

    // Helper function for save & update order
    private List<Book> fetchAndValidateBooks(Set<Long> bookIds) {
        List<Book> books = bookRepository.findAllById(bookIds);
        if (books.isEmpty()) {
            throw new NoSuchElementException("No books found for the given book IDs.");
        }

        books.forEach(book -> {
            if (book.getQoh() <= 0) {
                throw new IllegalStateException("Book with title '" + book.getTitle() + "' is out of stock.");
            }
            book.setQoh(book.getQoh() - 1);
        });

        return books;
    }
    // ---

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
        // Validate total amount
        if (orderRequestDTO.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Total amount must be a positive value.");
        }

        // Fetch customer details
        User customer = userRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Customer not found with userId: " + orderRequestDTO.getCustomerId()));

        // Fetch and validate books
        List<Book> books = fetchAndValidateBooks(orderRequestDTO.getBookIds());

        // Create and populate order
        Order order = new Order();
        order.setTotalAmount(orderRequestDTO.getTotalAmount());
        order.setBooks(books);
        order.setUser(customer);
        order.setStatus(OrderStatus.PENDING); // Set status to PENDING

        // Save order
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
        Order existingOrder = getOrderById(id);

        // Validate order status
        if (existingOrder.getStatus().equals(OrderStatus.DELIVERED) ||
                existingOrder.getStatus().equals(OrderStatus.SHIPPED)) {
            throw new IllegalArgumentException("This order cannot be updated, since it is already processed.");
        }

        // Restore stock for books in the existing order
        existingOrder.getBooks().forEach(book -> book.setQoh(book.getQoh() + 1));

        // Validate new order total amount
        if (orderRequestDTO.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Total amount must be a positive value.");
        }

        // Fetch customer details
        User customer = userRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Customer not found with userId: " + orderRequestDTO.getCustomerId()));

        // Fetch and validate books
        List<Book> books = fetchAndValidateBooks(orderRequestDTO.getBookIds());

        // Update order details
        existingOrder.setTotalAmount(orderRequestDTO.getTotalAmount());
        existingOrder.setBooks(books);
        existingOrder.setUser(customer);
        existingOrder.setStatus(OrderStatus.PENDING); // Reset status to PENDING

        // Save and return the updated order
        return orderRepository.save(existingOrder);
    }
    // ---

    @Override
    public void deleteOrder(long id) {
        Optional<Order> existingOrder = orderRepository.findById(id);

        if (!existingOrder.isPresent()) {
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
    public List<Order> getAllOrdersByOrderPlaceDate(Date inputOrderPlacedDate) {
        return orderRepository.findAllOrdersByOrderPlaceDate(inputOrderPlacedDate);
    }
    // ---

    @Override
    public Order updateOrderStatus(long id, OrderStatus newStatus) {
        Order existingOrder = getOrderById(id);

        // Validate the current state and the new state
        if (existingOrder.getStatus() == OrderStatus.DELIVERED && newStatus != OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Cannot change the status of a delivered order.");
        }

        if (existingOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot update the status of a cancelled order.");
        }

        // Update status and save the order
        existingOrder.setStatus(newStatus);
        return orderRepository.save(existingOrder);
    }
}
