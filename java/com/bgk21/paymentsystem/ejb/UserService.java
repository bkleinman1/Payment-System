package com.bgk21.paymentsystem.ejb;

import com.bgk21.paymentsystem.entity.SystemUser;
import com.bgk21.paymentsystem.entity.SystemUserGroup;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@DeclareRoles({"admins", "users"})
public class UserService {

    @PersistenceContext
    EntityManager em;

    public UserService() {
    }

    @PermitAll
    public void registerUser(String email, String userpassword, String name, String surname, double balance, String currency, String userGroup) {
        try {
            SystemUser sys_user;
            SystemUserGroup sys_user_group;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = userpassword;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            String paswdToStoreInDB = sb.toString();
            
            double conversion = getConversion("GBP", currency);
            
            sys_user = new SystemUser(email, paswdToStoreInDB, name, surname, balance * conversion, currency);
            sys_user_group = new SystemUserGroup(email, userGroup);

            em.persist(sys_user);
            em.persist(sys_user_group);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PermitAll
    public SystemUser getAccount(String email) {
        String queryString = "SELECT a FROM SystemUser a WHERE a.email = :email";
        TypedQuery<SystemUser> query = em.createQuery(queryString, SystemUser.class);
        query.setParameter("email", email);
        List<SystemUser> list = query.getResultList();
        if(list.isEmpty() || list.size() > 1) {
            return null;
        } else {
            return list.get(0);
        }
    }
    
    @PermitAll
    public ArrayList<String> getAccountDetails(String email) {
        SystemUser user = getAccount(email);
        ArrayList<String> str = new ArrayList<>();
        str.add(user.getEmail());
        str.add(user.getName());
        str.add(user.getSurname());
        str.add(Double.toString(user.getBalance()));
        str.add(user.getCurrency());
        return str;
    }
    
    @RolesAllowed("admins")
    public List<SystemUser> getUsers() {
        return em.createNamedQuery("findAllUsers").getResultList();
    }
    
    @PermitAll
    public static double getConversion(String currencyFrom, String currencyTo) {
        //String url = "http://ec2amaz-pd51354:8080/PaymentSystem-1.0-SNAPSHOT/rest/conversion";
        String url = "http://localhost:8080/PaymentSystem/rest/conversion";
            String charset = "UTF-8";
            String currency1 = currencyFrom;
            String currency2 = currencyTo;

            String query = null; 
        try {
            query = String.format("%s/%s",
                    URLEncoder.encode(currency1, charset),
                    URLEncoder.encode(currency2, charset));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            URLConnection connection;
            String responseBody = null;
            try {
                connection = new URL(url + "/" + query).openConnection();
                connection.setRequestProperty("Accept-Charset", charset);
                InputStream response = connection.getInputStream();
                Scanner scanner = new Scanner(response);
                responseBody = scanner.useDelimiter("\\A").next();
            } catch (MalformedURLException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Double.parseDouble(responseBody);
    }
    
}