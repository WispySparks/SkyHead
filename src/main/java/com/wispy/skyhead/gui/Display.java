package com.wispy.skyhead.gui;

import com.wispy.skyhead.Cache;
import com.wispy.skyhead.SkyHead;
import com.wispy.skyhead.api.API;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
/**
 * This class is used to interact with the visible elements on the screen to set player names.
 */
public class Display {
	
    private static void newTag(EntityPlayer player, String level) { // set players level and add to cache
    	Cache.addPlayer(player.getName(), level);
    	player.refreshDisplayName(); // call name format event
    }
    
    private static void oldTag(EntityPlayer player) { // get player from cache and set level
    	player.refreshDisplayName(); // call name format event
    }

    public static void setLevel(Entity entity) { // set the level of an entity
    	if (entity instanceof EntityOtherPlayerMP) { // make sure its a player and not local player
			final EntityPlayer entityPlayer = (EntityPlayer) entity; // get entity object
			if (!Cache.inCache(entity.getName(), false)) { // check if we already have this players level
            	new Thread(new Runnable() { // use threads to not hold up main thread
            		@Override
            		public void run() {
                  		Display.newTag(entityPlayer, API.getLevel(entityPlayer.getUniqueID().toString())); // set tag of entity to their level
            		}
            	}) .start();
			}
			else { // if we do get it from the cache
            	new Thread(new Runnable() { // use threads to not hold up main thread
            		@Override
            		public void run() {
                  		Display.oldTag(entityPlayer); // set tag of entity to their level
            		}
            	}).start();
			}
		}
    }

    public static void setLevels() {
		Minecraft mc = Minecraft.getMinecraft();
		if (SkyHead.enabled) {
			for (EntityPlayer player : mc.theWorld.playerEntities) { // set the names of everyone in the lobby
				setLevel(player);
			}
		} else {
			for (EntityPlayer player : mc.theWorld.playerEntities) { // clear the names of everyone in the lobby
				player.refreshDisplayName();
			}
		}
	}

}
