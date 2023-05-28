package wispysparks.skyhead.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import wispysparks.skyhead.Cache;
import wispysparks.skyhead.SkyHead;
import wispysparks.skyhead.api.API;

/**
 * This class is used to interact with the visible elements on the screen to set player names.
 */
public class Display {
	
    public static void setLevel(Entity entity) { // set the level of an entity
    	if (entity instanceof EntityOtherPlayerMP) { // make sure its a player and not local player
			final EntityOtherPlayerMP player = (EntityOtherPlayerMP) entity; 
			if (!Cache.inCache(player.getName(), false)) { 
            	new Thread(() -> {
					String level = API.getLevel(player.getUniqueID().toString()); 
					Cache.addPlayer(player.getName(), level);
					player.refreshDisplayName(); 
				}).start();
			}
			else { 
            	new Thread(() -> {
					player.refreshDisplayName(); 
				}).start();
			}
		}
    }

    public static void setLevels() {
		Minecraft mc = Minecraft.getMinecraft();
		if (SkyHead.enabled()) {
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
