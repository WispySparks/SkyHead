package com.wispy.skyhead.events;

import java.lang.reflect.Field;

import org.apache.logging.log4j.Logger;

import com.wispy.skyhead.Cache;
import com.wispy.skyhead.SkyHead;
import com.wispy.skyhead.gui.Display;
import com.wispy.skyhead.gui.PlayerListGui;
import com.wispy.skyhead.util.Text;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
/**
 * Events class where events are subscribed to and handled when fired.
 */
public class Events {
	
	private final static Field header = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175256_i", "header"});
	private final static Field footer = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175255_h", "footer"});
	private final static Field time = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175253_j", "lastTimeOpened"});
	private PlayerListGui playerList = new PlayerListGui(Minecraft.getMinecraft(), Minecraft.getMinecraft().ingameGUI);
	private Logger logger = SkyHead.logger;
    private Boolean checkedUpdate = false;

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        Minecraft mc = Minecraft.getMinecraft(); // get minecraft
    	if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.equals("mc.hypixel.net") && SkyHead.enabled) { // make sure not in singleplayer and on hypixel and mod enabled
    		Display.setLevel(event.entity); // set level for the entity
    	}
    }
    
    @SubscribeEvent
    public void nameFormat(PlayerEvent.NameFormat event) { // called when a players display name is changed
		String level = (SkyHead.enabled) ? Cache.queryCache(event.username) : "";
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
			} catch (IllegalArgumentException e) {SkyHead.logger.error(e);} catch (IllegalAccessException e) {SkyHead.logger.error(e);} // render my own custom tab list
			playerList.renderPlayerlist(new ScaledResolution(mc).getScaledWidth(), mc.theWorld.getScoreboard(), mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0));
		}
	}
    
	@SubscribeEvent
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) { // update checker to send messages and get result
        if (checkedUpdate) return;
        checkedUpdate = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean pending = true;
                CheckResult result = null;
                String message = "";
                while (pending) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        logger.error(e);
                    }
                    result = ForgeVersion.getResult(Loader.instance().activeModContainer());
                    switch (result.status) {
                        case AHEAD: // I guess you're somehow ahead of the latest version but I don't know how that makes sense
                            pending = false;
                            break;
                        case BETA: // you have latest unstable version
                            pending = false;
                            break;
                        case BETA_OUTDATED:
                            pending = false;
                            break;
                        case FAILED:
                            pending = false;
                            message = "SkyHead Update Checker: Could not access update.json.";
                            break;
                        case OUTDATED:
                            pending = false;
                            message = "SkyHead Update Checker: New stable version available for SkyHead.";
                            break;
                        case PENDING: // pending response
                            break;
                        case UP_TO_DATE: // you have latest stable version
                            pending = false;
                            break;
                    }
                }
                Boolean sentMessage = false;
                while (!sentMessage) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        logger.error(e);
                    }
                    if (Minecraft.getMinecraft().thePlayer != null) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(Text.ChatText(message, ""));
                        sentMessage = true;
                    }
                }
            }
        }).start();;
    }

}