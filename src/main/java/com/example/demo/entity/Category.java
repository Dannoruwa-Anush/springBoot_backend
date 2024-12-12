package com.example.demo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories") // set table name
@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class Category {

    @Id // set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generate the primary key value by the database itself using
                                                        // the auto-increment column option
    private Long id; // primary key

    @Column(name="category_name", nullable = false, unique = true)
    private String CategoryName;

    // Category (one) -- (Many) SubCategory
    // Category side relationship
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true) // category -> variable "private
                                                                                       // Category category;" in the
                                                                                       // SubCategory.java
    @JsonIgnore //prevent this property from being included in the JSON output.
    private List<SubCategory> subcategories;

    /*
     * Parent Entity: The entity on the "one" side of the relationship.
     * Child Entity: The entity on the "many" side of the relationship.
     * 
     * 
     * cascade = CascadeType.ALL:
     * when you perform an operation on the parent entity, that operation will also
     * be applied to the child entities (those referenced by the relationship).
     * 
     * 
     * orphanRemoval = true
     * the child entity becomes an "orphan," and if it's no longer associated with
     * any parent, it is removed from the database.
     */
}
