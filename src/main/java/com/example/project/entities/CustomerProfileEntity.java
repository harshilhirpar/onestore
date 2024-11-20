package com.example.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customer")
public class CustomerProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Use UUID for scalability
    @Column(nullable = false, updatable = false)
    private String id;

    @OneToOne // Assuming a customer has a 1:1 relationship with a user
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = true, length = 200)
    private String address;

    @Column(nullable = true, length = 100)
    private String city;

    @Column(nullable = true, length = 50)
    private String state;

    @Column(nullable = true, length = 20)
    private String zipCode;

    @Column(nullable = true, length = 50)
    private String country;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
