package com.example.demo.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.example.demo.common.projectEnum.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders") // set table name
@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class Order {

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

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // EnumType.STRING: This will store the enum value as a String in the database.
    private OrderStatus status;

    /*
     * set createdAt and updatedAt automatically when an order is created or updated
     * 
     * 
     * The method annotated with @PrePersist is called before the entity is inserted
     * into the database during a persist() operation.
     * 
     * The method annotated with @PreUpdate is called before the entity is updated
     * during an update() operation.
     * 
     */
    @PrePersist // is called before the entity is inserted
    public void onPrePersist() {
        ZonedDateTime zonedNowUtc = ZonedDateTime.now(ZoneId.of("UTC")); // Convert to UTC time zone
        this.createdAt = zonedNowUtc.toLocalDateTime(); // Convert to LocalDateTime for storage
        this.updatedAt = zonedNowUtc.toLocalDateTime(); // Convert to LocalDateTime for storage
    }

    @PreUpdate // is called before the entity is updated
    public void onPreUpdate() {
        ZonedDateTime zonedNowUtc = ZonedDateTime.now(ZoneId.of("UTC"));// Convert to UTC time zone
        this.updatedAt = zonedNowUtc.toLocalDateTime(); // Convert to LocalDateTime for storage
    }

    /*
     * LAZY: This means that the related entities (in the Many-to-Many relationship)
     * will not be fetched immediately when the parent entity is loaded.
     */
    // Order (One) --- (Many) OrderBooks
    // Order side relationship
    //"order" -> variable private Order order; in OrderBook.java
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderBook> orderBooks;

    // User (One) --- (Many) Order
    // FK : id ->User
    // Order side relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column
    private User user;
}
