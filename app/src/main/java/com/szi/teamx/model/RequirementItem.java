package com.szi.teamx.model;

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
}
