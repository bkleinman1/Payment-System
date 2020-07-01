package com.bgk21.paymentsystem.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class SystemUserGroup implements Serializable {
    
    @Id
    String email;
    @NotNull
    String groupname;
     

    public SystemUserGroup() {
    }
    
    public SystemUserGroup(String email, String users) {
        this.email = email;
        this.groupname = users;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.email);
        hash = 97 * hash + Objects.hashCode(this.groupname);
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
        final SystemUserGroup other = (SystemUserGroup) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.groupname, other.groupname)) {
            return false;
        }
        return true;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getEmail() {
        return email;
    }

    public String getGroupname() {
        return groupname;
    }

    
}
