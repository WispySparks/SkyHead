package wispysparks.skyhead;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import wispysparks.skyhead.commands.SkyheadCommands;
import wispysparks.skyhead.events.Events;

@Mod(modid = SkyHead.MODID, version = SkyHead.VERSION, updateJSON = SkyHead.UPDATE_LINK, clientSideOnly = true)
public class SkyHead {
	
    public static final String MODID = "skyhead";
    public static final String VERSION = "1.6";
    public static final String UPDATE_LINK = "https://raw.githubusercontent.com/WispySparks/SkyHead/master/update.json";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final Minecraft MC = Minecraft.getMinecraft();
    private static final String SERVER_IP = "mc.hypixel.net";
    
    public SkyHead() {
    	MinecraftForge.EVENT_BUS.register(new Events()); 
        ClientCommandHandler.instance.registerCommand(new SkyheadCommands()); 
    }

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) { 
        Config.loadConfig();
    }
    
    public static boolean enabled() {
        ServerData server = MC.getCurrentServerData();
        return 
            !MC.isSingleplayer() 
            && server != null 
            && server.serverIP.equals(SERVER_IP) 
            && Config.isEnabled();
    }

}
