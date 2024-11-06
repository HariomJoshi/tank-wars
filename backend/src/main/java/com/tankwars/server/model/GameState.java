package com.tankwars.server.model;

import com.tankwars.server.enums.TType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameState {
    private TType terrain;
    private Player player1;
    private Player player2;
    private String roomId;
}
