package wispysparks.skyhead.events;

import static wispysparks.skyhead.SkyHead.MC;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import wispysparks.skyhead.Config;
import wispysparks.skyhead.SkyHead;
import wispysparks.skyhead.api.Cache;
import wispysparks.skyhead.gui.Display;
import wispysparks.skyhead.gui.PlayerListGui;
import wispysparks.skyhead.util.Text;

/**
 * Events class where events are subscribed to and handled when fired.
 */
public class Events {
	
    private static PlayerListGui playerList = new PlayerListGui(MC, MC.ingameGUI);
    private static boolean checkedUpdate = false;

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (!SkyHead.enabled()) return;
        Display.setLevel(event.entity); 
    }
    
    @SubscribeEvent
    public void nameFormat(PlayerEvent.NameFormat event) { 
        if (!SkyHead.enabled()) return;
        if (Cache.contains(event.username)) {
            String level = Cache.query(event.username);
            event.displayname = event.displayname + level; 
        }
    }
    
	@SubscribeEvent
	public void tabListRender(RenderGameOverlayEvent.Pre event) { 
        if (!SkyHead.enabled()) return;
		if (event.type.equals(RenderGameOverlayEvent.ElementType.PLAYER_LIST) && Config.isTabEnabled()) {
			event.setCanceled(true); 
			playerList.updateValues();
			playerList.renderPlayerlist(new ScaledResolution(MC).getScaledWidth(), MC.theWorld.getScoreboard(), MC.theWorld.getScoreboard().getObjectiveInDisplaySlot(0));
		}
	}
    
	@SubscribeEvent
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) { 
        // Update Checker
        if (checkedUpdate) return;
        checkedUpdate = true;
        new Thread(() -> {
            boolean pending = true;
            String message = "";
            while (pending) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    SkyHead.LOGGER.error("SkyHead Update Checker Error", e);
                }
                CheckResult result = ForgeVersion.getResult(Loader.instance().activeModContainer());
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
                    case UP_TO_DATE: 
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
                    case PENDING: break;
                }
            }
            boolean sentMessage = false;
            while (!sentMessage) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    SkyHead.LOGGER.error("SkyHead Update Checker Error", e);
                }
                if (MC.thePlayer != null) {
                    MC.thePlayer.addChatMessage(Text.ChatText(message, ""));
                    sentMessage = true;
                }
            }
        }).start();
    }

}