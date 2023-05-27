package wispysparks.skyhead;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import wispysparks.skyhead.api.API;
import wispysparks.skyhead.commands.SkyheadCommands;
import wispysparks.skyhead.events.Events;
/**
 * Main mod class where the startup events are fired.
 */
@Mod(modid = SkyHead.modID, version = SkyHead.version, clientSideOnly = true, updateJSON = SkyHead.updateLink)
public class SkyHead {
	
    public static final String modID = "skyhead";
    public static final String version = "1.5.1";
    public static final String updateLink = "https://raw.githubusercontent.com/WispySparks/SkyHead/master/update.json";
    public static final Logger logger = LogManager.getLogger(modID);
    public static Configuration config; // config
    public static boolean enabled; // mod on/off
    public static boolean tabEnabled; // whether tab mode is on or off
    public static int mode; // which game level to retrieve
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) { // get config file and set all mod wide variables from the config
    	File configFile = new File(Loader.instance().getConfigDir(), "SkyHead.cfg");
    	config = new Configuration(configFile);
    	config.load();
    	Property propB = config.get(Configuration.CATEGORY_CLIENT, "enabled", true); // get enabled property from config file
    	enabled = propB.getBoolean();
    	Property propT = config.get(Configuration.CATEGORY_CLIENT, "tabEnabled", false); // if tab is enabled from config
    	tabEnabled = propT.getBoolean();
    	Property propS = config.get(Configuration.CATEGORY_CLIENT, "apiKey", ""); // get api key from config
    	API.apikey = propS.getString();
    	Property propM = config.get(Configuration.CATEGORY_CLIENT, "mode", 0); // 0 = skywars, 1 = bedwars
    	mode = propM.getInt();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) { // register my mod stuff
        ClientCommandHandler.instance.registerCommand(new SkyheadCommands()); // register commands
    	MinecraftForge.EVENT_BUS.register(new Events()); // register events handler
        MinecraftForge.EVENT_BUS.register(this); // register my mod
    } 

    public static boolean allowedToSet() {
        Minecraft mc = Minecraft.getMinecraft();
        return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.equals("mc.hypixel.net") && SkyHead.enabled;
    }

}
