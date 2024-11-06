package com.tankwars.server.model.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseMessage {
    private boolean accepted;
    private String inviterUsername;
}
