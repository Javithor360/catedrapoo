package com.tickets.model;

import java.util.Date;

public class UserSession {
    protected int id;
    protected String name;
    protected String email;
    protected Date birthday;
    protected String gender;
    protected String profile_pic;
    protected int role_id;
    protected Date created_at;

    public UserSession(int id, String name, String email, String gender, Date birthday, String profile_pic, int role_id, Date created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.role_id = role_id;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public int getRole_id() {
        return role_id;
    }

    public Date getCreated_at() {
        return created_at;
    }
}
