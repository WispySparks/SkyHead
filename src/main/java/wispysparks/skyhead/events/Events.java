package wispysparks.skyhead.events;

import java.lang.reflect.Field;

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
import wispysparks.skyhead.Cache;
import wispysparks.skyhead.Config;
import wispysparks.skyhead.SkyHead;
import wispysparks.skyhead.gui.Display;
import wispysparks.skyhead.gui.PlayerListGui;
import wispysparks.skyhead.util.Text;

/**
 * Events class where events are subscribed to and handled when fired.
 */
public class Events {
	
	private static PlayerListGui playerList = new PlayerListGui(Minecraft.getMinecraft(), Minecraft.getMinecraft().ingameGUI);
	private static Field tabListHeader = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175256_i", "header"});
	private static Field tabListFooter = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175255_h", "footer"});
	private static Field tabListTime = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175253_j", "lastTimeOpened"});
    private static boolean checkedUpdate = false;

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinWorldEvent event) {
        if (!SkyHead.enabled()) return;
        Display.setLevel(event.entity); 
    }
    
    @SubscribeEvent
    public static void nameFormat(PlayerEvent.NameFormat event) { // called when a players display name is changed
        if (!SkyHead.enabled()) return;
		String level = Cache.queryCache(event.username);
    	event.displayname = event.displayname + level; 
    }
    
	@SubscribeEvent
	public static void tabListRender(RenderGameOverlayEvent.Pre event) { 
        if (!SkyHead.enabled()) return;
		if (event.type.equals(RenderGameOverlayEvent.ElementType.PLAYER_LIST) && Config.isTabEnabled()) {
			event.setCanceled(true); 
			GuiPlayerTabOverlay tabList = Minecraft.getMinecraft().ingameGUI.getTabList();
			try { // gather info from real tab list
				playerList.header = (IChatComponent) tabListHeader.get(tabList);
				playerList.footer = (IChatComponent) tabListFooter.get(tabList);
				playerList.lastTimeOpened = Long.valueOf(tabListTime.get(tabList).toString());
			} catch (IllegalArgumentException | IllegalAccessException e) {
                SkyHead.LOGGER.error("Tab List Render Error", e);
            }  
			Minecraft mc = Minecraft.getMinecraft();
			playerList.renderPlayerlist(new ScaledResolution(mc).getScaledWidth(), mc.theWorld.getScoreboard(), mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0));
		}
	}
    
	@SubscribeEvent
    public static void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) { // update checker to send messages and get result
        if (checkedUpdate) return;
        checkedUpdate = true;
        new Thread(() -> {
            boolean pending = true;
            CheckResult result = null;
            String message = "";
            while (pending) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    SkyHead.LOGGER.error("Update Checker Error", e);
                }
                result = ForgeVersion.getResult(Loader.instance().activeModContainer());
                switch (result.status) {
                    case AHEAD: 
                        pending = false;
                        break;
                    case BETA: 
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
                    case PENDING: 
                        break;
                    case UP_TO_DATE: 
                        pending = false;
                        break;
                }
            }
            boolean sentMessage = false;
            while (!sentMessage) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    SkyHead.LOGGER.error("Update Checker Error", e);
                }
                if (Minecraft.getMinecraft().thePlayer != null) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(Text.ChatText(message, ""));
                    sentMessage = true;
                }
            }
        }).start();
    }

}