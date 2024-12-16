package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books") // set table name
@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class Book {

    // Define columns in table

    /*
     * @JsonIgnore : prevent this attribute from being included in the JSON output.
     * private DataType attributeName;
     * 
     * Don't use @JsonIgnore in the entity level beacuse it's better to isolate the
     * serialization concerns from the entity logic, which is where DTOs help.
     * 
     * DTO specifies exactly what fields should be serialized.
     */
    @Id // set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private int qoh;// Quantity on hand

    @Column(name = "cover_image", nullable = false)
    private String coverImage; // path for coverImage

    /*
     * LAZY: This means that the related entities (in the Many-to-Many relationship)
     * will not be fetched immediately when the parent entity is loaded.
     */

    // Book (Many) --- (One) SubCategory
    // Book side relationship
    // FK : id ->SubCategory
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false) // Foreign key column
    private SubCategory subCategory;

    // Author (one) --- (Many) Book
    // FK : id ->Author
    // Author side relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false) // Foreign key column
    private Author author;

    // OrderBooks (Many) --- (One) Book
    // Book side relationship
    //"book" -> variable private Book book; in OrderBook.java
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderBook> orderBooks;
}
