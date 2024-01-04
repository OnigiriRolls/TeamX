package com.szi.teamx.model;

import java.util.Objects;

public class RequirementItem {
    private String userInput;

    public RequirementItem() {
    }

    public RequirementItem(String userInput) {
        this.userInput = userInput;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequirementItem that = (RequirementItem) o;
        return Objects.equals(userInput, that.userInput);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInput);
    }
}
