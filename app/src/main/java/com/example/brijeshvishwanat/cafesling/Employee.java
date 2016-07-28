package com.example.brijeshvishwanat.cafesling;

import java.io.Serializable;

/**
 * Created by Brijesh.Vishwanat on 7/21/2016.
 */
public class Employee implements Serializable{
    public String name;
    public String email;
    public String id;
    public int monthlyDue;

    public Employee(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
public String getEmail(){
    return email;
}
    public void setEmail(String email){
        this.email= email;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMonthlyDue() {
        return monthlyDue;
    }

    public void setMonthlyDue(int monthlyDue) {
        this.monthlyDue = monthlyDue;
    }
}

