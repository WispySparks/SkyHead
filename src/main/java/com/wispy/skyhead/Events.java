package com.wispy.skyhead;

import com.wispy.skyhead.api.API;
import com.wispy.skyhead.gui.Display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events {
	
	private API api = new API();

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        Minecraft mc = Minecraft.getMinecraft(); // get minecraft
    	if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.equals("mc.hypixel.net") && SkyHead.enabled) { // make sure not in singleplayer and on hypixel and mod enabled
    		if (event.entity instanceof EntityPlayer && !(event.entity instanceof EntityPlayerSP)) { // make sure its a player and not local player
    			if (!Cache.inCache(event.entity.getName())) { // check if we already have this players level
        			final Entity entity = event.entity; // get entity object
                	new Thread(new Runnable() {
                		@Override
                		public void run() {
                      		Display.newTag((EntityPlayer) entity, API.getLevel(entity.getName())); // set tag of entity to their level
                		}
                	}) .start();
    			}
    			else { // if we do get it from the cache
    				final Entity entity = event.entity; // get entity object
                	new Thread(new Runnable() {
                		@Override
                		public void run() {
                      		Display.oldTag((EntityPlayer) entity); // set tag of entity to their level
                		}
                	}).start();
    			}
    		}
    	}
    }
    
    @SubscribeEvent
    public void nameFormat(PlayerEvent.NameFormat event) {
    	event.displayname = event.displayname + Display.currentLevel; // set a players display name to their name plus level
    	Display.currentLevel = ""; // reset current level
    }
    
}
