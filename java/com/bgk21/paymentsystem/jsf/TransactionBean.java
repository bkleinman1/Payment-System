package com.bgk21.paymentsystem.jsf;

import com.bgk21.paymentsystem.ejb.TransactionEJB;
import com.bgk21.paymentsystem.ejb.UserService;
import com.bgk21.paymentsystem.entity.SystemUser;
import com.bgk21.paymentsystem.entity.TransactionEntity;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class TransactionBean implements Serializable {
    
    @EJB
    TransactionEJB ejb;
    
    @EJB
    UserService usrSrv;
            
    String senderEmail;
    String recipientEmail;
    double value;
    String currency;
    
    String returnMessage;
    
    public TransactionBean() {
        returnMessage = "";
    }
    
    public List<TransactionEntity> getAllTransactions() {
        return ejb.getAllTransactions();
    }
    
    public List<TransactionEntity> getUserTransactions() {
        return ejb.getUserTransactions(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
    }
    
    public List<TransactionEntity> getRequestedPayments() {
        return ejb.getRequestedPayments(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
    }
    
    public void makePayment() {
        senderEmail = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        currency = usrSrv.getAccount(senderEmail).getCurrency();
        SystemUser from = usrSrv.getAccount(senderEmail);
        SystemUser to = usrSrv.getAccount(recipientEmail);
        if(to != null && from != null) { 
            returnMessage = ejb.makePayment(from, to, value);
        } else {
            returnMessage = "User account with that email does not exist";
        }
    }
    
    public void requestPayment() {
        recipientEmail = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        currency = usrSrv.getAccount(recipientEmail).getCurrency();
        SystemUser from = usrSrv.getAccount(senderEmail);
        SystemUser to = usrSrv.getAccount(recipientEmail);
        if(to != null && from != null) { 
            returnMessage = ejb.requestPayment(from, to, value);
        } else {
            returnMessage = "User account with that email does not exist";
        }
    }
    
    public void acceptRequest(Long id) {
        ejb.acceptRequest(id);
    }
    
    public void rejectRequest(Long id) {
        ejb.rejectRequest(id);
    }

    public TransactionEJB getEjb() {
        return ejb;
    }

    public void setEjb(TransactionEJB ejb) {
        this.ejb = ejb;
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
    
    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
}
