/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgk21.paymentsystem.ejb;

import com.bgk21.paymentsystem.entity.SystemUser;
import com.bgk21.paymentsystem.entity.SystemUserGroup;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Startup
@Singleton
public class StartupBean {
    
    @PersistenceContext(unitName = "PaymentSystemPU")
    EntityManager em;
  
    @PostConstruct
    public void init() {
        iniAdmin();
    }
    
    public void iniAdmin() {
        try {
            
            SystemUser admin = em.find(SystemUser.class, "admin1");
            
            if(admin == null) {
                SystemUser sys_user;
                SystemUserGroup sys_user_group;

                MessageDigest md = MessageDigest.getInstance("SHA-256");
                String passwd = "admin1";
                md.update(passwd.getBytes("UTF-8"));
                byte[] digest = md.digest();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < digest.length; i++) {
                    sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
                }
                String paswdToStoreInDB = sb.toString();

                sys_user = new SystemUser("admin1", paswdToStoreInDB, "admin1", "admin1", 0, "GBP");
                sys_user_group = new SystemUserGroup("admin1", "admins");

                em.persist(sys_user);
                em.persist(sys_user_group);
            }
            
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(StartupBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   
}