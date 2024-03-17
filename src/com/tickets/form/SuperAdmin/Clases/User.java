package com.tickets.form.SuperAdmin.Clases;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private Date birthday;
    private int role;
    private Timestamp createdAt;


    public User(int id, String name, String email, String password, String gender, Date birthday, int role, Timestamp creado) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.role = role;
        this.createdAt = creado;
    }

    public User( String name, String email, String password, String gender, Date birthday, int role, Timestamp creado) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.role = role;
        this.createdAt = creado;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
