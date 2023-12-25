package com.szi.teamx.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class Team {
    private String id;
    private String key;
    private String name;
    private String lowerCaseName;
    private String owner;
    private String description;
    private Map<String, String> requirements;
    private Map<String, String> requirementsLower;

    public Team() {
    }

    public String getKey() {
        return key;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLowerCaseName() {
        return lowerCaseName;
    }

    public Map<String, String> getRequirementsLower() {
        return requirementsLower;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLowerCaseName(String lowerCaseName) {
        this.lowerCaseName = lowerCaseName;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(Map<String, String> requirements) {
        this.requirements = requirements;
    }

    public void setRequirementsLower(Map<String, String> requirementsLower) {
        this.requirementsLower = requirementsLower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }

    public void update(Team team) {
        this.name = team.name;
        this.lowerCaseName = team.lowerCaseName;
        this.description = team.description;
        this.requirements = team.requirements;
        this.requirementsLower = team.requirementsLower;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
