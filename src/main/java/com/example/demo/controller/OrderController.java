package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.customHttpResponse.CustomErrorResponse;
import com.example.demo.dto.request.OrderRequestDTO;
import com.example.demo.dto.request.OrderStatusUpdateRequestDTO;
import com.example.demo.dto.request.ShoppingCartTotalRequestDTO;
import com.example.demo.dto.response.OrderBillResponseDTO;
import com.example.demo.dto.response.OrderResponseDTO;
import com.example.demo.dto.response.ShoppingCartTotalResponseDTO;
import com.example.demo.dto.request.OrderByDateRequestDTO;
import com.example.demo.dto.request.OrderByStatusRequestDTO;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("order")
/*
 * It allows you to specify which external origins (i.e., domains or URLs) are
 * permitted to make requests to your API. This is useful when your frontend
 * application (running on a different server or port) needs to interact with
 * your backend
 */
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*
     * ResponseEntity is a powerful class in Spring Boot for managing HTTP
     * responses.
     * 
     * It allows you to:
     * 
     * Return custom status codes.
     * Add headers.
     * Set the body of the response.
     * 
     * .build() - You typically use .build() when you want to send an HTTP status
     * without any associated content in the response body.
     */

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(orders);
    }
    // ---

    @GetMapping("/{id}")
    public ResponseEntity<OrderBillResponseDTO> getAllOrderById(@PathVariable Long id) {
        try {
            OrderBillResponseDTO order = orderService.getOrderById(id);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // ---

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            OrderResponseDTO savedOrder = orderService.saveOrder(orderRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // ---

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            OrderResponseDTO updatedOrder = orderService.updateOrder(id, orderRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order is not found");
        }
    }
    // ---

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    @GetMapping("/customer/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getAllOrdersByUserId(userId);

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(orders);
    }
    // ---

    @GetMapping("/orderStatus")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersByOrderStatus(@RequestBody OrderByStatusRequestDTO orderByStatusRequestDTO) {
        List<OrderResponseDTO> orders = orderService.getAllOrdersByOrderStatus(orderByStatusRequestDTO);

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(orders);
    }
    // ---

    @GetMapping("/orderDateAndStatus")
    public ResponseEntity<List<OrderResponseDTO>> findAllOrdersByDateAndStatus(@RequestBody OrderByDateRequestDTO orderByDateRequestDTO) {
        List<OrderResponseDTO> orders = orderService.findAllOrdersByDateAndStatus(orderByDateRequestDTO);

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(orders);
    }
    // ---

    @PutMapping("/updateOrderStatus/{id}")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable long id, @RequestBody OrderStatusUpdateRequestDTO orderStatusUpdateRequestDTO) {
        try {
            OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, orderStatusUpdateRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order is not found");
        }
    }
    // ---


    @PostMapping("/getShoppingCartTotal")
    public ResponseEntity<Object> getShoppingCartTotal(@RequestBody ShoppingCartTotalRequestDTO ShoppingCartTotalRequestDTO) {
        try {
            ShoppingCartTotalResponseDTO calculatedTotal = orderService.calculateTotalAmount(ShoppingCartTotalRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(calculatedTotal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //---
}
