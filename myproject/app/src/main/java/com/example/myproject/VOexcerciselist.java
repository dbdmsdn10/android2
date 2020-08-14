package com.example.myproject;

public class VOexcerciselist {
    String _id, who, dodate, name, metid;
    double met;
    long time;

    public void setMetid(String metid) {
        this.metid = metid;
    }

    public String getMetid() {
        return metid;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setDodate(String dodate) {
        this.dodate = dodate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMet(double met) {
        this.met = met;
    }

    public String get_id() {
        return _id;
    }

    public long getTime() {
        return time;
    }

    public String getWho() {
        return who;
    }

    public String getDodate() {
        return dodate;
    }

    public String getName() {
        return name;
    }

    public double getMet() {
        return met;
    }
}
