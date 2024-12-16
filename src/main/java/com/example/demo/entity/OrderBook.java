package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_books") // set table name
@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class OrderBook {
    /*
     * Previous Version:
     * 
     * @ManyToMany(fetch = FetchType.LAZY)
     * 
     * @JoinTable(name = "order_books", joinColumns = @JoinColumn(name =
     * "order_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
     * private List<Book> books;
     * 
     * Order (Many) --------------- (Many) Book
     * 
     * @JoinTable will be automatically create a new table that is called as
     * “Order_Books”. That
     * table consists of two columns as order_id, and book_id.
     * 
     * However, if you need to add more columns (EX: quantity) to Order_Books table
     * you have to create it manually and mapped them as following:
     * 
     * Order (One) ------ (Many) OrderBooks (Many) --------- (One)Book
     */

    @Id // set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    @Column(nullable = false)
    private int quantity; // Quantity of the book

    // Order (One) ------ (Many) OrderBooks
    // FK : id ->Order
    // OrderBook side relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false) // Foreign key column
    private Order order;

    // OrderBooks (Many) --------- (One)Book
    // FK : id ->Book
    // OrderBook side relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false) // Foreign key column
    private Book book;
}
