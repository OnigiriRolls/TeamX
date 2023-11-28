package com.szi.teamx.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class Team {
    private String name;
    private String owner;
    private String description;
    private Map<String, String> requirements;

    public Team() {
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getRequirements() {
        return requirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name) && Objects.equals(owner, team.owner) && Objects.equals(description, team.description) && Objects.equals(requirements, team.requirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner, description, requirements);
    }

    @Override
    public String toString() {
        return name;
    }

    public void update(Team team){
        this.name = team.name;
        this.description = team.description;
        this.requirements = team.requirements;
    }
}
