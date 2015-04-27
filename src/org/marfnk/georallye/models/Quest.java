package org.marfnk.georallye.models;

public class Quest {

    private String code;
    private String title;
    private double latitude;
    private double longitude;
    private int distanceTrigger;
    private String welcomeMessage;
    private String questMessage;
    private String completeMessage;
    private boolean reveiled = false;
    private boolean completed = false;
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public int getDistanceTrigger() {
        return distanceTrigger;
    }
    public void setDistanceTrigger(int distanceTrigger) {
        this.distanceTrigger = distanceTrigger;
    }
    public String getWelcomeMessage() {
        return welcomeMessage;
    }
    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }
    public String getQuestMessage() {
        return questMessage;
    }
    public void setQuestMessage(String questMessage) {
        this.questMessage = questMessage;
    }
    public String getCompleteMessage() {
        return completeMessage;
    }
    public void setCompleteMessage(String completeMessage) {
        this.completeMessage = completeMessage;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public boolean isReveiled() {
        return reveiled;
    }
    public void setReveiled(boolean distanceTriggered) {
        this.reveiled = distanceTriggered;
    }
}
