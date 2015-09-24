package com.redhat.addressbook.backend;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * A simple DTO for the address book example.
 *
 * Serializable and cloneable Java Object that are typically persisted in the
 * database and can also be easily converted to different formats like JSON.
 */
// Backend DTO class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class Contact implements Serializable, Cloneable {

    private UUID id;

    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private String email = "";
    private Date birthDate;
    private SimpleDateFormat format = new SimpleDateFormat ("yyy-MM-dd");
    
    public Contact(){
    	this.id = UUID.randomUUID();
    	this.email="set me";
    	this.phone="555-555-5555";
    	this.birthDate = new Date();
    	this.firstName = "set me";
    	this.lastName = "set me";
    }
    public Contact (UUID id, String firstName, String lastName, String phone, String email, Date birthDate){
    	this.id = id;
    	this.birthDate = birthDate;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.phone = phone;
    	this.email = email;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public Contact clone() throws CloneNotSupportedException {
        try {
            return (Contact) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + email;
        
    }

}
