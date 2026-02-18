package com.begac.minicrm.lead.domain;

import com.begac.minicrm.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "leads")
public class Lead extends BaseEntity {

    @Column(length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(length = 120)
    private String email;

    @Column(length = 30)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private LeadStatus status = LeadStatus.NEW;

    @Column
    private UUID convertedOpportunityId;

    protected Lead() {
        // JPA için gerekli (reflection ile object oluşturur)
    }

    public Lead(String firstName, String lastName, String email, String phone) {
        this.firstName = normalize(firstName);
        this.lastName = requireNonBlank(lastName, "lastName is required");
        this.email = normalize(email);
        this.phone = normalize(phone);
        this.status = LeadStatus.NEW;
    }

    // --- Domain davranışı: status geçişini entity üstünden yapacağız
    public void changeStatus(LeadStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("newStatus is required");
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Lead status transition not allowed: " + this.status + " -> " + newStatus);
        }
        this.status = newStatus;
    }

    // --- getter'lar (şimdilik setter yok; state kontrollü değişsin)
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LeadStatus getStatus() { return status; }
    public UUID getConvertedOpportunityId() { return convertedOpportunityId; }

    private static String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String requireNonBlank(String s, String message) {
        if (s == null || s.trim().isEmpty()) throw new IllegalArgumentException(message);
        return s.trim();
    }
}
