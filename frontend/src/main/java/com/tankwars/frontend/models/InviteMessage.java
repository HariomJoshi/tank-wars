package com.tankwars.frontend.models;

public class InviteMessage {
    private String inviterUsername;
    private String inviteeUsername;

    public String getInviterUsername() {
        return inviterUsername;
    }

    public void setInviterUsername(String inviterUsername) {
        this.inviterUsername = inviterUsername;
    }

    public String getInviteeUsername() {
        return inviteeUsername;
    }

    public void setInviteeUsername(String inviteeUsername) {
        this.inviteeUsername = inviteeUsername;
    }


    public InviteMessage(String inviterUsername, String inviteeUsername) {
        this.inviterUsername = inviterUsername;
        this.inviteeUsername = inviteeUsername;
    }
}
