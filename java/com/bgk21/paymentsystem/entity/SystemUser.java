package com.bgk21.paymentsystem.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQuery(name="findAllUsers", query="SELECT u from SystemUser u")
public class SystemUser implements Serializable {
    
    @Id
    String email;
    @NotNull
    String password;
    @NotNull
    String name;
    @NotNull
    String surname;
    @NotNull
    double balance;
    @NotNull
    String currency;

    public SystemUser() {
    }
    
    public SystemUser(String email, String password, String name, String surname, double balance, String currency) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.balance = balance;
        this.currency = currency;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.email);
        hash = 89 * hash + Objects.hashCode(this.surname);
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
        final SystemUser other = (SystemUser) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "username: " + email + ", name: " + name + ", surname: " + surname + ", balance: " + balance + ", currency: " + currency;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
    
}
