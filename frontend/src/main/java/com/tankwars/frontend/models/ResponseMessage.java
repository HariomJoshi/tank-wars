package com.tankwars.frontend.models;

public class ResponseMessage {
    private boolean accepted;
    private String inviterUsername;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getInviterUsername() {
        return inviterUsername;
    }

    public void setInviterUsername(String inviterUsername) {
        this.inviterUsername = inviterUsername;
    }


    ResponseMessage(String inviterUsername, boolean accepted) {
        this.inviterUsername = inviterUsername;
        this.accepted = accepted;
    }


}
