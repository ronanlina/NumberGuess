package com.example.connivingdog.numberguess;

public class SimPrefix {
    private String simCard;
    private String simPref;

    public SimPrefix(String simCard, String simPref) {
        this.simCard = simCard;
        this.simPref = simPref;
    }

    public SimPrefix() {
    }

    public String getSimCard() {
        return simCard;
    }

    public void setSimCard(String simCard) {
        this.simCard = simCard;
    }

    public String getSimPref() {
        return simPref;
    }

    public void setSimPref(String simPref) {
        this.simPref = simPref;
    }
}
