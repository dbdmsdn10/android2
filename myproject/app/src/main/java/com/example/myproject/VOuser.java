package com.example.myproject;

public class VOuser {
    String _id;
    double weight,height,age,gender,how;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public double getAge() {
        return age;
    }

    public double getGender() {
        return gender;
    }

    public double getHow() {
        return how;
    }



    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public void setGender(double gender) {
        this.gender = gender;
    }

    public void setHow(double how) {
        this.how = how;
    }
}
