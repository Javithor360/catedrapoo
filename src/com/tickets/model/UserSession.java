package com.tickets.model;

import java.util.Date;

public class UserSession {
    private int id;
    private String name;
    private String email;
    private Date birthday;
    private String gender;
    private Integer role_id;
    private Date created_at;

    public UserSession(int id, String name, String email, String gender, Date birthday, Integer role_id, Date created_at) {
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

    public Integer getRole_id() {
        return role_id;
    }

    public Date getCreated_at() {
        return created_at;
    }
}
