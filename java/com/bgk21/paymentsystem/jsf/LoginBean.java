package com.bgk21.paymentsystem.jsf;

import com.bgk21.paymentsystem.ejb.UserService;
import com.bgk21.paymentsystem.entity.SystemUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    
    @EJB
    UserService usrSrv;
    
    String email;
    String password;
    String name;
    String surname;
    Double balence;
    String currency;
    
    public LoginBean() {
        
    }
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try {
            request.login(email, password);
            return getAccountPage();
        } catch (ServletException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try {
            //this method will disassociate the principal from the session (effectively logging him/her out)
            request.logout();
            return "index";
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Logout failed."));
        }
        return "";
    }
    
    public String getAccountDetails() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        email = request.getRemoteUser();
        ArrayList<String> list = usrSrv.getAccountDetails(email);
        email = list.get(0);
        name = list.get(1);
        surname = list.get(2);
        balence = Double.valueOf(list.get(3));
        currency = list.get(4);
        return "Email: " + email + ", Name: " + name + ", Surname: " + surname + ", Balence: " + balence + ", Currency: " + currency;
    }
    
    public String getAccountPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if(request.isUserInRole("users")) {
                return "/users/account";
            } else if(request.isUserInRole("admins")) {
                return "/admin/account";
            } else {
                return "/users/account";
        }
    }
    
    public List<SystemUser> getUsers() {
        return usrSrv.getUsers();
    }
    
    public void setUsrSrv(UserService usrSrv) {
        this.usrSrv = usrSrv;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBalence(Double balence) {
        this.balence = balence;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public UserService getUsrSrv() {
        return usrSrv;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Double getBalence() {
        return balence;
    }

    public String getCurrency() {
        return currency;
    }
}
