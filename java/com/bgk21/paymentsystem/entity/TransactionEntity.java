package com.bgk21.paymentsystem.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class TransactionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    String senderEmail;
    @NotNull
    String recipientEmail;
    @NotNull
    double value;
    @NotNull
    String currency;
    @NotNull
    String accepted;
    
    public TransactionEntity() {
    }
    
    public TransactionEntity(String emailFrom, String emailTo, double value, String currency, String accepted) {
        this.recipientEmail = emailTo;
        this.senderEmail = emailFrom;
        this.value = value;
        this.currency = currency;
        this.accepted = accepted;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.senderEmail);
        hash = 47 * hash + Objects.hashCode(this.recipientEmail);
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TransactionEntity other = (TransactionEntity) obj;
        if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        if (!Objects.equals(this.senderEmail, other.senderEmail)) {
            return false;
        }
        if (!Objects.equals(this.recipientEmail, other.recipientEmail)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "TransactionEntity{" + "id=" + id + ", senderEmail=" + senderEmail + ", recipientEmail=" + recipientEmail + ", value=" + value + ", currency=" + currency + '}';
    }

    public String isAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getAccepted() {
        return accepted;
    }
    
    
    
}
