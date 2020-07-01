package com.bgk21.paymentsystem.ejb;

import com.bgk21.paymentsystem.entity.SystemUser;
import com.bgk21.paymentsystem.entity.TransactionEntity;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@DeclareRoles({"admins", "users"})
public class TransactionEJB {

    @PersistenceContext(unitName = "PaymentSystemPU")
    EntityManager em;

    @PermitAll
    public TransactionEntity getTransaction(long id) {
        TransactionEntity e = em.find(TransactionEntity.class, id);
        return e;
    }

    @RolesAllowed("users")
    public void writeTransaction(TransactionEntity e) {
        em.persist(e);
        em.flush();
    }

    //returns true if payment successful
    @RolesAllowed("users")
    public String makePayment(SystemUser from, SystemUser to, double value) {
        TransactionEntity e = new TransactionEntity(from.getEmail(), to.getEmail(), value, from.getCurrency(), "true");
        SystemUser userFrom = em.find(SystemUser.class, from.getEmail());
        SystemUser userTo = em.find(SystemUser.class, to.getEmail());
        if (userFrom != null && userTo != null) {
            if (userFrom.getBalance() - value > 0) {
                userFrom.setBalance(userFrom.getBalance() - value);
                userTo.setBalance(userTo.getBalance() + value * UserService.getConversion(userFrom.getCurrency(), userTo.getCurrency()));
                writeTransaction(e);
                return "Payment succesful";
            } else {
                return "Payment failed - Balence lower then required";
            }
        } else {
            return "Payment failed - The email address: " + to.getEmail() + " is not linked to an account";
        }
    }

    @RolesAllowed("users")
    public String requestPayment(SystemUser from, SystemUser to, double value) {
        TransactionEntity e = new TransactionEntity(from.getEmail(), to.getEmail(), value, to.getCurrency(), "false");
        SystemUser userFrom = em.find(SystemUser.class, from.getEmail());
        SystemUser userTo = em.find(SystemUser.class, to.getEmail());
        if (userFrom != null && userTo != null) {
            writeTransaction(e);
            return "Request successful";
        } else {
            return "Payment request failed - The email address: " + from.getEmail() + " is not linked to an account";
        }
    }

    @RolesAllowed("users")
    public String acceptRequest(Long id) {
        TransactionEntity trans = em.find(TransactionEntity.class, id);
        SystemUser userFrom = em.find(SystemUser.class, trans.getSenderEmail());
        SystemUser userTo = em.find(SystemUser.class, trans.getRecipientEmail());

        if (userFrom.getBalance() - trans.getValue()*UserService.getConversion(userTo.getCurrency(), userFrom.getCurrency()) > 0) {
            trans.setAccepted("true");
            userFrom.setBalance(userFrom.getBalance() - trans.getValue()*UserService.getConversion(userTo.getCurrency(), userFrom.getCurrency()));
            userTo.setBalance(userTo.getBalance() + trans.getValue());
            return "Payment succesful";
        } else {
            return "Payment failed - Balence lower then required";
        }
    }

    @RolesAllowed("users")
    public String rejectRequest(Long id) {
        TransactionEntity trans = em.find(TransactionEntity.class, id);
        trans.setAccepted("rejected");
        return "Rejected successfully";
    }

    @RolesAllowed("admins")
    public List<TransactionEntity> getAllTransactions() {
        String queryString = "SELECT t FROM TransactionEntity t";
        TypedQuery<TransactionEntity> query = em.createQuery(queryString, TransactionEntity.class);
        List<TransactionEntity> list = query.getResultList();
        return list;
    }

    @PermitAll
    public List<TransactionEntity> getUserTransactions(String email) {
        String queryString = "SELECT t FROM TransactionEntity t WHERE t.senderEmail = :email or t.recipientEmail = :email";
        TypedQuery<TransactionEntity> query = em.createQuery(queryString, TransactionEntity.class);
        query.setParameter("email", email);
        return query.getResultList();
    }

    @RolesAllowed("users")
    public List<TransactionEntity> getRequestedPayments(String email) {
        String queryString = "SELECT t FROM TransactionEntity t WHERE t.senderEmail = :email AND t.accepted = :accepted";
        TypedQuery<TransactionEntity> query = em.createQuery(queryString, TransactionEntity.class);
        query.setParameter("email", email);
        query.setParameter("accepted", "false");
        return query.getResultList();
    }
}
