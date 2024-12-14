package com.example.demo.entity;

import java.util.List;

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
@Table(name = "authors") // set table name
@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class Author {

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

    /*
     * @GeneratedValue
     * generate the primary key value by the database itself using the
     * auto-increment column option
     */
    @Id // set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    @Column(name = "author_name", nullable = false, unique = true)
    private String authorName;

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

    // Author (one) --- (Many) Book
    // Author side relationship
    /*
     * author -> variable private Author author; in Book.java
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;
}
