package com.example.project.entities;

import com.example.project.enums.BusinessStatus;
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
@Table(name = "business_profiles")
public class BusinessProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;
    // Business details
    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String businessType;

    @Column(unique = true, nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private String taxIdNumber;

    @Column(nullable = false)
    private String businessEmail;

    @Column(nullable = false)
    private String businessPhone;

    // Optional: Address for the business
    @Column(nullable = true)
    private String businessAddress;

    @Column(nullable = true)
    private String businessWebsite;

    @Column(nullable = true)
    private String businessFacebook;

    @Column(nullable = true)
    private String businessInstagram;

    // Optional: Logo URL or Image
    @Column(nullable = true)
    private String businessLogo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
