package com.wispy.skyhead;

import java.lang.reflect.Field;

import com.wispy.skyhead.api.API;
import com.wispy.skyhead.gui.Display;
import com.wispy.skyhead.gui.PlayerListGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
/**
 * Events class where events are subscribed to and handled when fired.
 */
public class Events {
	
	private final static Field header = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175256_i", "header"});
	private final static Field footer = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175255_h", "footer"});
	private final static Field time = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175253_j", "lastTimeOpened"});
	public PlayerListGui playerList = new PlayerListGui(Minecraft.getMinecraft(), Minecraft.getMinecraft().ingameGUI);

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        Minecraft mc = Minecraft.getMinecraft(); // get minecraft
    	if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.equals("mc.hypixel.net") && SkyHead.enabled) { // make sure not in singleplayer and on hypixel and mod enabled
    		setLevel(event.entity); // set level for the entity
    	}
    }
    
    @SubscribeEvent
    public void nameFormat(PlayerEvent.NameFormat event) { // called when a players display name is changed
		String level = Cache.queryCache(event.username); // get level
    	event.displayname = event.displayname + level; // set a players display name to their name plus level
    }

	@SubscribeEvent
	public void tabListRender(RenderGameOverlayEvent.Pre event) { // render event on tab list
		if (event.type.equals(RenderGameOverlayEvent.ElementType.PLAYER_LIST) && SkyHead.enabled && SkyHead.tabEnabled) {
			event.setCanceled(true); // cancel original tab rendering
			GuiPlayerTabOverlay tabList = Minecraft.getMinecraft().ingameGUI.getTabList();
			Minecraft mc = Minecraft.getMinecraft();
			try { // gather info from real tab list
				playerList.header = (IChatComponent) header.get(tabList);
				playerList.footer = (IChatComponent) footer.get(tabList);
				playerList.lastTimeOpened = Long.valueOf(time.get(tabList).toString());
			} catch (IllegalArgumentException e) {System.out.println(e);} catch (IllegalAccessException e) {System.out.println(e);} // render my own custom tab list
			playerList.renderPlayerlist(new ScaledResolution(mc).getScaledWidth(), mc.theWorld.getScoreboard(), mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0));
		}
	}

	public static void setLevel(Entity entity) { // set the level of an entity
    	if (entity instanceof EntityOtherPlayerMP) { // make sure its a player and not local player
			final EntityPlayer entityPlayer = (EntityPlayer) entity; // get entity object
			if (!Cache.inCache(entity.getName())) { // check if we already have this players level
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

}
