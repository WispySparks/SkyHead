package com.wispy.skyhead;

import java.io.File;

import com.wispy.skyhead.api.API;
import com.wispy.skyhead.commands.SkyheadCommands;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SkyHead.modID, version = SkyHead.version)
public class SkyHead
{
	
    public static final String modID = "skyhead";
    public static final String version = "LOCAL";
    public static Configuration config;
    public static boolean enabled; // on/off
    public static int mode; // which game level to retrieve
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	File configFile = new File(Loader.instance().getConfigDir(), "SkyHead.cfg");
    	config = new Configuration(configFile);
    	config.load();
    	Property propB = config.get(Configuration.CATEGORY_CLIENT, "enabled", true); // get enabled property from config file
    	enabled = propB.getBoolean();
    	Property propS = config.get(Configuration.CATEGORY_CLIENT, "apiKey", ""); // get api key from config
    	API.apikey = propS.getString();
    	Property propM = config.get(Configuration.CATEGORY_CLIENT, "mode", 0); // 0 = skywars, 1 = bedwars
    	mode = propM.getInt();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) // register my mod stuff
    {
    	ClientCommandHandler.instance.registerCommand(new SkyheadCommands());
    	MinecraftForge.EVENT_BUS.register(new Events());
        MinecraftForge.EVENT_BUS.register(this);
    }
    
}
