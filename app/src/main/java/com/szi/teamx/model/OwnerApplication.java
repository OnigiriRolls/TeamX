package com.szi.teamx.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

@IgnoreExtraProperties
public class OwnerApplication {
    private String teamName;
    private String userName;

    public OwnerApplication() {
    }

    public String getTeamName() {
        return teamName;
    }

    public String getUserName() {
        return userName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwnerApplication that = (OwnerApplication) o;
        return Objects.equals(teamName, that.teamName) && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamName, userName);
    }
}
