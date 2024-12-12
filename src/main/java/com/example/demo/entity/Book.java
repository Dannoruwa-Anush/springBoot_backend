package com.example.demo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books") //set table name
@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class Book {

    @Id //set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //generate the primary key value by the database itself using the auto-increment column option
    private Long id; //primary key

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private int qoh;//Quantity on hand

    @Column(name = "cover_image", nullable = false)
    private String coverImage; //path for coverImage

    //Book (Many) ---  (One) SubCategory
    //Book side relationship
    //FK : id ->SubCategory
    @ManyToOne(fetch = FetchType.LAZY) //LAZY: This means that the related entities (Category) will not be fetched immediately when the parent entity (SubCategory) is loaded. 
    @JoinColumn(name = "sub_category_id", nullable = false)  // Foreign key column
    @JsonIgnore //prevent this property from being included in the JSON output.
    private SubCategory subCategory;

    //Author (one) ---  (Many) Book
    //FK : id ->Author
    //Author side relationship
    @ManyToOne(fetch = FetchType.LAZY) //LAZY: This means that the related entities will not be fetched immediately when the parent entity is loaded. 
    @JoinColumn(name = "author_id", nullable = false)// Foreign key column
    @JsonIgnore //prevent this property from being included in the JSON output.
    private Author author;

    //Order (Many) ---  (Many) Book
    //Book side relationship
    @ManyToMany(mappedBy = "books") // books -> variable private List<Book> books; in Order.java
    @JsonIgnore //prevent this property from being included in the JSON output.
    private List<Order> orders;
}
