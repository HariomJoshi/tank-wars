package com.tankwars.server.model;

import com.tankwars.server.enums.WType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
@Getter
@Setter
public class Player {
    private Integer posX;
    private Integer posY;
    private HashMap<WType, Integer> weaponCount;
    private Integer healthRemaining;


   Player(){
       // all the person will have same amount of weapons at the start
       weaponCount= new HashMap<>();
       weaponCount.put(WType.BASIC, 5);
       weaponCount.put(WType.OIL_SLICK, 2);
       weaponCount.put(WType.MISSILE, 3);
       healthRemaining = 100;
   }

   public void reduceHealth(int damage){
       healthRemaining -= damage;
   }

   public boolean isDead(){
       return healthRemaining >0;
   }
}
