package com.example.itwaretestapp;

public class DataModel {

    String controlType;
    String question;
    String requiredValidator;
    String requiredValidatorMessage;

    public String getRegularExpression() {
        return regularExpression;
    }

    public void setRegularExpression(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    String regularExpression;

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRequiredValidator() {
        return requiredValidator;
    }

    public void setRequiredValidator(String requiredValidator) {
        this.requiredValidator = requiredValidator;
    }

    public String getRequiredValidatorMessage() {
        return requiredValidatorMessage;
    }

    public void setRequiredValidatorMessage(String requiredValidatorMessage) {
        this.requiredValidatorMessage = requiredValidatorMessage;
    }
}
