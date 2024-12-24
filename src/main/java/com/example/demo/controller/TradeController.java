package com.example.demo.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.customHttpResponse.CustomErrorResponse;
import com.example.demo.dto.request.OrderStatusUpdateRequestDTO;
import com.example.demo.dto.request.ShoppingCartTotalRequestDTO;
import com.example.demo.dto.response.OrderBillResponseDTO;
import com.example.demo.dto.response.OrderResponseDTO;
import com.example.demo.dto.response.ShoppingCartTotalResponseDTO;
import com.example.demo.service.OrderService;
import com.example.demo.service.commonService.PdfFileGenerationService;

@RestController
@RequestMapping("trade")
/*
 * It allows you to specify which external origins (i.e., domains or URLs) are
 * permitted to make requests to your API. This is useful when your frontend
 * application (running on a different server or port) needs to interact with
 * your backend
 */
@CrossOrigin(origins = "*")
public class TradeController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PdfFileGenerationService pdfFileGenerationService;

    @PostMapping("/getShoppingCartTotal")
    public ResponseEntity<Object> getShoppingCartTotal(
            @RequestBody ShoppingCartTotalRequestDTO ShoppingCartTotalRequestDTO) {
        try {
            ShoppingCartTotalResponseDTO calculatedTotal = orderService
                    .calculateTotalAmount(ShoppingCartTotalRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(calculatedTotal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // ---

    @PutMapping("/updateTradeStatus/{id}")
    public ResponseEntity<Object> updateTradeStatus(@PathVariable long id,
            @RequestBody OrderStatusUpdateRequestDTO orderStatusUpdateRequestDTO) {
        try {
            OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, orderStatusUpdateRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order is not found");
        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CustomErrorResponse("Concurrent modification detected, please try again."));
        }
    }
    // ---

    // GET Bill in PDF
    @GetMapping("/{id}/pdfTradeBill")
    public ResponseEntity<byte[]> getTradeBillByOrderId(@PathVariable Long id) {
        OrderBillResponseDTO order = orderService.getOrderById(id);
        byte[] pdfBytes = pdfFileGenerationService.generateOrderPdf(order);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    // ----
}
