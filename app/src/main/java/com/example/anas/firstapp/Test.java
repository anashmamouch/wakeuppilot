package com.example.anas.firstapp;

import java.io.Serializable;

/**
 * Created by Anas on 28/7/15.
 */
public class Test implements Serializable {

    private int id;
    private int ballTouched;
    private int totalTouches;
    private boolean firstTime;
    private int userId;
    private String createdAt;

    public Test() {
    }

    public Test(int id, int ballTouched, int totalTouches, boolean firstTime, int userId, String createdAt) {
        this.id = id;
        this.ballTouched = ballTouched;
        this.totalTouches = totalTouches;
        this.firstTime = firstTime;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Test(int ballTouched, int totalTouches, boolean firstTime, int userId) {
        this.ballTouched = ballTouched;
        this.totalTouches = totalTouches;
        this.firstTime = firstTime;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBallTouched() {
        return ballTouched;
    }

    public void setBallTouched(int ballTouched) {
        this.ballTouched = ballTouched;
    }

    public int getTotalTouches() {
        return totalTouches;
    }

    public void setTotalTouches(int totalTouches) {
        this.totalTouches = totalTouches;
    }

    public boolean getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", ballTouched=" + ballTouched +
                ", totalTouches=" + totalTouches +
                ", firstTime=" + firstTime +
                ", userId=" + userId +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}

