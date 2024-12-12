package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.common.projectEnum.OrderStatus;
import com.example.demo.entity.Book;
import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    // ---

    @Override
    public Order getOrderById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order is not found with id: \" + id"));
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
    public Order saveOrder(Order order) {
        if (order.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Total amount must be a positive value.");
        }

        // Reduce the qoh for each book in the order
        for (Book book : order.getBooks()) {
            if (book.getQoh() <= 0) {
                throw new IllegalStateException("Book with title '" + book.getTitle() + "' is out of stock.");
            }
            book.setQoh(book.getQoh() - 1); // Decrement qoh by 1 for each book ordered
        }

        return orderRepository.save(order);
    }
    // ---

    @Override
    @Transactional
    public Order updateOrder(long id, Order order) {
        Order existingOrder = getOrderById(id);

        // Check if the order is already processed
        if (existingOrder.getStatus().equals(OrderStatus.DELIVERED)
                || existingOrder.getStatus().equals(OrderStatus.SHIPPED)) {
            throw new IllegalArgumentException("This order cannot be updated, since it is already processed.");
        }

        // Restore qoh for books in the existing order
        for (Book existingBook : existingOrder.getBooks()) {
            existingBook.setQoh(existingBook.getQoh() + 1); // Add back the reserved quantity
        }

        // Validate the new order details
        if (order.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Total amount must be a positive value.");
        }

        // Reduce qoh for the new books
        for (Book newBook : order.getBooks()) {
            if (newBook.getQoh() <= 0) {
                throw new IllegalStateException("Book with title '" + newBook.getTitle() + "' is out of stock.");
            }
            newBook.setQoh(newBook.getQoh() - 1); // Decrement qoh for each book
        }

        // Update the existing order
        existingOrder.setBooks(order.getBooks());
        existingOrder.setTotalAmount(order.getTotalAmount());
        existingOrder.setStatus(order.getStatus()); // Optionally update the status if needed

        return orderRepository.save(existingOrder); // Save the updated order
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
