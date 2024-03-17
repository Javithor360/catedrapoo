package com.tickets.model;

import java.time.LocalDate;
import java.util.Date;

public class Ticket {
    private int id;
    private String code;
    private String name;
    private String description;
    private int state_id;
    private String state;
    private String requester_area_name;
    private String boss_name;
    private String dev_boss_name;
    private int tester_id;
    private String tester_name;
    private int programmer_id;
    private String programmer_name;
    private LocalDate due_date;
    private Date created_at;

    public Ticket(int id, String code, String name, String description, String state, String requester_area_name, String boss_name, String dev_boss_name, Date created_at) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.state = state;
        this.requester_area_name = requester_area_name;
        this.boss_name = boss_name;
        this.dev_boss_name = dev_boss_name;
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

    public LocalDate getDue_date() {
        return due_date;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public int getTester_id() {
        return tester_id;
    }

    public int getProgrammer_id() {
        return programmer_id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTester_id(int tester_id) {
        this.tester_id = tester_id;
    }

    public void setProgrammer_id(int programmer_id) {
        this.programmer_id = programmer_id;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }
}