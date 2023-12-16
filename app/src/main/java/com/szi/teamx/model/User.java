package com.szi.teamx.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class User {
    private List<String> teams;
    private List<String> teamsOwner;

    public User() {
    }

    public List<String> getTeams() {
        return teams;
    }

    public List<String> getTeamsOwner() {
        return teamsOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(teams, user.teams) && Objects.equals(teamsOwner, user.teamsOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teams, teamsOwner);
    }
}
