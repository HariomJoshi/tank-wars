package com.tankwars.server.model;

import com.tankwars.server.enums.WType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Weapon {

    private WType name;
    private int damage;
    private int damageRadius;
    private int weight;
}
