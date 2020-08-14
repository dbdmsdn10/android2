package com.example.myproject;

public class VOeatlist {
    String _id, eatdate, who, foodid, wheneat, name, kcal, once;

    public void setName(String name) {
        this.name = name;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public void setOnce(String once) {
        this.once = once;
    }

    public String getName() {
        return name;
    }

    public String getKcal() {
        return kcal;
    }

    public String getOnce() {
        return once;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setEatdate(String eatdate) {
        this.eatdate = eatdate;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public void setWheneat(String wheneat) {
        this.wheneat = wheneat;
    }

    public String get_id() {
        return _id;
    }

    public String getEatdate() {
        return eatdate;
    }

    public String getWho() {
        return who;
    }

    public String getFoodid() {
        return foodid;
    }

    public String getWheneat() {
        return wheneat;
    }
}
