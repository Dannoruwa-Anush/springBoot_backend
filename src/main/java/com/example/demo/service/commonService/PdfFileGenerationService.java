package com.example.demo.service.commonService;

import org.springframework.stereotype.Service;

import com.example.demo.dto.response.OrderBillResponseDTO;
import com.example.demo.dto.response.OrderBookResponseDTO;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Paragraph;
import java.io.ByteArrayOutputStream;

@Service
public class PdfFileGenerationService {
    
     public byte[] generateOrderPdf(OrderBillResponseDTO orderBill) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        // Add title
        document.add(new Paragraph("Order Detail").setBold().setFontSize(18));

        // Add delivery information
        document.add(new Paragraph("Delivery Information").setBold());
        Table deliveryTable = new Table(2);
        deliveryTable.addCell("Name").addCell(orderBill.getCustomer().getUsername());
        deliveryTable.addCell("Email").addCell(orderBill.getCustomer().getEmail());
        deliveryTable.addCell("Address").addCell(orderBill.getCustomer().getAddress());
        deliveryTable.addCell("Telephone").addCell(orderBill.getCustomer().getTelephoneNumber());
        document.add(deliveryTable);

        // Add order details
        document.add(new Paragraph("\nOrder Details").setBold());
        Table orderTable = new Table(5);
        orderTable.addCell("No").addCell("Item No").addCell("Title").addCell("Unit Price").addCell("Quantity").addCell("Sub Total");
        int index = 1;

        for (OrderBookResponseDTO orderBook : orderBill.getOrderBooks()) {
            orderTable.addCell(String.valueOf(index++));
            orderTable.addCell(String.valueOf(orderBook.getBook().getId()));
            orderTable.addCell(orderBook.getBook().getTitle());
            orderTable.addCell(String.valueOf(orderBook.getBook().getUnitPrice()));
            orderTable.addCell(String.valueOf(orderBook.getQuantity()));
            orderTable.addCell(String.valueOf(orderBook.getSubTotal()));
        }
        document.add(orderTable);

        // Add total
        document.add(new Paragraph("\nTotal: Rs. " + orderBill.getTotalAmount()).setBold());

        document.close();
        return outputStream.toByteArray();
    }
}
