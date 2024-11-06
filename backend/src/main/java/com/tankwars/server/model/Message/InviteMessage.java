package com.tankwars.server.model.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InviteMessage {
    private String inviterUsername;
    private String inviteeUsername;
}
