package com.wispy.skyhead.commands;

import java.util.ArrayList;
import java.util.List;

import com.wispy.skyhead.Cache;
import com.wispy.skyhead.SkyHead;
import com.wispy.skyhead.api.API;
import com.wispy.skyhead.api.APILimiter;
import com.wispy.skyhead.gui.Display;
import com.wispy.skyhead.util.Text;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class SkyheadCommands extends CommandBase {
	
	private final List<String> aliases = new ArrayList<String>(); 
	private final String options = "<on:off:sw:bw:key:requests:size>";
	
	public SkyheadCommands() { // make sh also a valid command
        aliases.add("sh"); 
	}

	@Override
	public String getCommandName() { // set cmd name
		return "skyhead";
	}

	@Override
	public String getCommandUsage(ICommandSender arg0) {
		return "skyhead " + options;
	}
	
	@Override 
    public List<String> getCommandAliases() {
		return this.aliases;
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
		if (commandSender instanceof EntityPlayerSP) return true; // only work if player is local player
		return false;
	}

	@Override
	public void processCommand(ICommandSender arg0, String[] args) throws CommandException {
		Minecraft mc = Minecraft.getMinecraft();
		if (args.length > 0) {
			if (args[0].equals("on")) { // turn mod on
				Property prop = SkyHead.config.get(Configuration.CATEGORY_CLIENT, "enabled", true); // get config property and set it so that it persists between launches
				prop.setValue(true); 
				SkyHead.config.save();
				SkyHead.enabled = true;
				mc.thePlayer.addChatMessage(Text.ChatText("Skyhead ON", "§a"));
			}
			else if (args[0].equals("off")) { // turn mod off
				Property prop = SkyHead.config.get(Configuration.CATEGORY_CLIENT, "enabled", true); // get config property and set it so that it persists between launches
				prop.setValue(false);
				SkyHead.config.save();
				SkyHead.enabled = false;
				Display.currentLevel = "";
				for (EntityPlayer player : mc.theWorld.playerEntities) { // clear the names of everyone in the lobby
					player.refreshDisplayName();
				}
				mc.thePlayer.addChatMessage(Text.ChatText("Skyhead OFF", "§c"));
			}
			else if (args[0].equals("requests")) { // display number of requests this minute
				mc.thePlayer.addChatMessage(Text.ChatText("Requests made this minute: " + APILimiter.requests, "§6"));
			}
			else if (args[0].equals("size")) { // display cache size
				mc.thePlayer.addChatMessage(Text.ChatText("Player Cache Size: " + Cache.getSize(), "§6"));
			}
			else if (args[0].equals("sw")) { // set mod mode to skywars levels
				Property prop = SkyHead.config.get(Configuration.CATEGORY_CLIENT, "mode", 0);
				prop.setValue(0);
				SkyHead.config.save();
				SkyHead.mode = 0; // set mode in config and current instance
				Display.currentLevel = "";
				for (EntityPlayer player : mc.theWorld.playerEntities) { // clear the names of everyone in the lobby
					player.refreshDisplayName();
				}
				mc.thePlayer.addChatMessage(Text.ChatText("Set to Skywars Mode", "§6"));
			}
			else if (args[0].equals("bw")) { // set mod mode to bedwars levels
				Property prop = SkyHead.config.get(Configuration.CATEGORY_CLIENT, "mode", 0);
				prop.setValue(1);
				SkyHead.config.save();
				SkyHead.mode = 1;  // set mode in config and current instance
				Display.currentLevel = "";
				for (EntityPlayer player : mc.theWorld.playerEntities) { // clear the names of everyone in the lobby
					player.refreshDisplayName();
				}
				mc.thePlayer.addChatMessage(Text.ChatText("Set to Bedwars Mode", "§6"));
			}
			else if (args[0].equals("key")) {
				if (args.length > 1) {
					Property prop = SkyHead.config.get(Configuration.CATEGORY_CLIENT, "apiKey", ""); // get config property and set it so that it persists between launches
					prop.setValue(args[1]);  // set key in config and in code
					SkyHead.config.save();
					API.apikey = args[1];
					SkyHead.enabled = true;
					mc.thePlayer.addChatMessage(Text.ChatText("Set API Key to " + args[1], "§6"));
				}
				else {
					mc.thePlayer.addChatMessage(Text.ChatText("Must Specify API Key", "§6"));
				}
			}
			else { // if not a valid subcommand
				mc.thePlayer.addChatMessage(Text.ChatText("Invalid Subcommand, " + options, "§6"));
			}
		}
		else { // if no subcommand given
			mc.thePlayer.addChatMessage(Text.ChatText("SkyHead is currently ", "§6")
				.appendSibling(Text.ChatText(convertBool(SkyHead.enabled, false), convertBool(SkyHead.enabled, true)))
				.appendSibling(Text.ChatText(" and is in mode " + convertMode(SkyHead.mode), "§6")));
		}
	}
	
	public String convertBool(Boolean bool, Boolean color) {
		if (color == false) {
			if (bool == true) return "Enabled";
			return "Disabled";
		}
		if (bool == true) return "§a";
		return "§c";
	}
	
	public String convertMode(int mode) {
		switch (mode) {
		case 0:
			return "Skywars";
		case 1:
			return "Bedwars";
		default:
			return "No Mode";
		}
	}

}
