package wispysparks.skyhead.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import wispysparks.skyhead.SkyHead;
import wispysparks.skyhead.api.API;
import wispysparks.skyhead.api.Cache;

/**
 * This class is used to interact with the visible elements on the screen to set player names.
 */
public class Display {
	
    public static void setLevel(Entity entity) { 
    	if (entity instanceof EntityOtherPlayerMP) { // make sure its a player and not local player
			final EntityOtherPlayerMP player = (EntityOtherPlayerMP) entity; 
			String result = Cache.query(player.getName());
			if (!Cache.contains(player.getName()) || result.equals(" §fLimit") || result.equals(" §fbadkey") || result.equals("")) { 
            	new Thread(() -> {
					String level = API.getLevel(player.getUniqueID().toString()); 
					System.out.println("SkyHead: " + player.getName() + level);
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
			for (EntityPlayer player : mc.theWorld.playerEntities) { 
				setLevel(player);
			}
		} else {
			for (EntityPlayer player : mc.theWorld.playerEntities) {
				player.refreshDisplayName();
			}
		}
	}

}
