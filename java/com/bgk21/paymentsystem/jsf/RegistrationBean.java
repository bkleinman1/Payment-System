package com.bgk21.paymentsystem.jsf;

import com.bgk21.paymentsystem.ejb.UserService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class RegistrationBean {

    @EJB
    UserService usrSrv;
    
    String email;
    String password;
    String name;
    String surname;
    double balance;
    String currency;

    public RegistrationBean() {
        balance = 1000;
    }
    
    //call the injected EJB
    public String register() {
        usrSrv.registerUser(email, password, name, surname, balance, currency, "users");
        return "index";
    }
    
    public String registerAdmin() {
        usrSrv.registerUser(email, password, name, surname, balance, currency, "admins");
        return "/admin/account";
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
    
    public UserService getUsrSrv() {
        return usrSrv;
    }

    public void setUsrSrv(UserService usrSrv) {
        this.usrSrv = usrSrv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getUserpassword() {
        return password;
    }

    public void setUserpassword(String userpassword) {
        this.password = userpassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    
}
