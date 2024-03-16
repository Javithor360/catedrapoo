package com.tickets.model;

import java.util.Date;

public class Ticket {
    private int id;
    private String code;
    private String name;
    private String description;
    private String state;
    private String requester_area_name;
    private String boss_name;
    private String dev_boss_name;
    private String tester_name;
    private String programmer_name;
    private Date due_date;
    private Date created_at;

    public Ticket(int id, String code, String name, String description, String state, String requester_area_name, String boss_name, String dev_boss_name, String tester_name, String programmer_name, Date due_date, Date created_at) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.state = state;
        this.requester_area_name = requester_area_name;
        this.boss_name = boss_name;
        this.dev_boss_name = dev_boss_name;
        this.tester_name = tester_name;
        this.programmer_name = programmer_name;
        this.due_date = due_date;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public String getRequester_area_name() {
        return requester_area_name;
    }

    public String getBoss_name() {
        return boss_name;
    }

    public String getDev_boss_name() {
        return dev_boss_name;
    }

    public String getTester_name() {
        return tester_name;
    }

    public String getProgrammer_name() {
        return programmer_name;
    }

    public Date getDue_date() {
        return due_date;
    }

    public Date getCreated_at() {
        return created_at;
    }
}
