package com.example.brijeshvishwanat.cafesling;

import java.io.Serializable;

/**
 * Created by Brijesh.Vishwanat on 7/21/2016.
 */
public class Employee implements Serializable{
    public String name;
    public int id;
    public int monthlyDue;
    public Employee(String name,int id,int monthlyDue){
        this.name=name;
        this.id=id;
        this.monthlyDue=monthlyDue;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonthlyDue() {
        return monthlyDue;
    }

    public void setMonthlyDue(int monthlyDue) {
        this.monthlyDue = monthlyDue;
    }
}

