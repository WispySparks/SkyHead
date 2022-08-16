package com.wispy.skyhead.commands;

import java.util.ArrayList;
import java.util.List;

import com.wispy.skyhead.Cache;
import com.wispy.skyhead.Events;
import com.wispy.skyhead.SkyHead;
import com.wispy.skyhead.api.API;
import com.wispy.skyhead.api.APILimiter;
import com.wispy.skyhead.gui.Display;
import com.wispy.skyhead.util.Text;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
/** 
 * Class for handling player commands.
 */  
public class SkyheadCommands extends CommandBase {
	
	private final List<String> aliases = new ArrayList<String>(); // command aliases
	private final String options = "<on:off:tab:sw:bw:key:requests:size>"; // availabe subcommands
	private final List<String> tabComplete = new ArrayList<String>(); // tab completions
	
	public SkyheadCommands() { // make sh also a valid command and add tab completions
        aliases.add("sh"); 
        tabComplete.add("on");
        tabComplete.add("off");
        tabComplete.add("tab");
        tabComplete.add("sw");
        tabComplete.add("bw");
        tabComplete.add("key");
        tabComplete.add("requests");
        tabComplete.add("size");
	}

	@Override
	public String getCommandName() { // set cmd name
		return "skyhead";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "skyhead " + options;
	}
	
	@Override 
    public List<String> getCommandAliases() { // add sh as an alias
		return this.aliases;
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
		if (commandSender instanceof EntityPlayerSP) return true; // only work if player is local player
		return false;
	}
	
	@Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return getListOfStringsMatchingLastWord(args, tabComplete); // add tab completion
    }

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		Minecraft mc = Minecraft.getMinecraft();
		if (args.length > 0) {
			if (args[0].equals("on")) { // turn mod on
				Property prop = SkyHead.config.get(Configuration.CATEGORY_CLIENT, "enabled", true); // get config property and set it so that it persists between launches
				prop.setValue(true); 
				SkyHead.config.save();
				SkyHead.enabled = true;
				retrieveLevels();
				mc.thePlayer.addChatMessage(Text.ChatText("Skyhead ON", "§a"));
			}
			else if (args[0].equals("off")) { // turn mod off
				Property prop = SkyHead.config.get(Configuration.CATEGORY_CLIENT, "enabled", true); // get config property and set it so that it persists between launches
				prop.setValue(false);
				SkyHead.config.save();
				SkyHead.enabled = false;
				Display.currentLevel = "";
				retrieveLevels();
				mc.thePlayer.addChatMessage(Text.ChatText("Skyhead OFF", "§c"));
			}
			else if (args[0].equals("tab")) { // toggle tab levels on and off
				Property prop = SkyHead.config.get(Configuration.CATEGORY_CLIENT, "tabEnabled", false);
				SkyHead.tabEnabled = !prop.getBoolean();
				prop.setValue(!prop.getBoolean());
				SkyHead.config.save();
				if (prop.getBoolean() == false) { // reset tab names of everyone
					clearTab();
				} else if (SkyHead.enabled) { // set tab names of everyone
					GuiPlayerTabOverlay tabList = Minecraft.getMinecraft().ingameGUI.getTabList();
					for (NetworkPlayerInfo player : mc.getNetHandler().getPlayerInfoMap()) {
						if (player.getGameProfile().getName() == mc.thePlayer.getName()) continue;
						if (Cache.inCache(player.getGameProfile().getName())) {
							String name = tabList.getPlayerName(player) + " " + Cache.queryCache(player.getGameProfile().getName());
							player.setDisplayName(Text.ChatText(name, ""));
						}
					}
				}
				mc.thePlayer.addChatMessage(Text.ChatText("Tab is now ", "§6")
					.appendSibling(Text.ChatText(convertBool(SkyHead.tabEnabled, false), convertBool(SkyHead.tabEnabled, true))));
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
				clearTab();
				SkyHead.mode = 0; // set mode in config and current instance
				retrieveLevels();
				mc.thePlayer.addChatMessage(Text.ChatText("Set to Skywars Mode", "§6"));
			}
			else if (args[0].equals("bw")) { // set mod mode to bedwars levels
				Property prop = SkyHead.config.get(Configuration.CATEGORY_CLIENT, "mode", 0);
				prop.setValue(1);
				SkyHead.config.save();
				clearTab();
				SkyHead.mode = 1;  // set mode in config and current instance
				retrieveLevels();
				mc.thePlayer.addChatMessage(Text.ChatText("Set to Bedwars Mode", "§6"));
			}
			else if (args[0].equals("key")) { // set api key
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
			else if (args[0].equals("help")) { // help command
				mc.thePlayer.addChatMessage(Text.ChatText("Welcome to SkyHead. Here is an explanation of every command. "
				+ "On and off will turn the whole mod on or off, tab will toggle showing levels in tab on and off, abbreviations are used to change the mode"
				+ " such as sw or bw. Key is used to set the api key to be used by the mod, size tells you the current size of the players cached for"
				+ " the mode you're currently in, and requests tells you how many requests have been sent to the api this minute.", "§6"));
			}
			else { // if not a valid subcommand
				mc.thePlayer.addChatMessage(Text.ChatText("Invalid Subcommand, " + options, "§6"));
			}
		}
		else { // if no subcommand given
			mc.thePlayer.addChatMessage(Text.ChatText("SkyHead is currently ", "§6")
				.appendSibling(Text.ChatText(convertBool(SkyHead.enabled, false), convertBool(SkyHead.enabled, true)))
				.appendSibling(Text.ChatText(" with tab levels ", "§6"))
				.appendSibling(Text.ChatText(convertBool(SkyHead.tabEnabled, false), convertBool(SkyHead.tabEnabled, true)))
				.appendSibling(Text.ChatText(" and is in mode ", "§6")
				.appendSibling(Text.ChatText(convertMode(SkyHead.mode), "§5"))));
		}
	}
	
	private String convertBool(Boolean bool, Boolean color) { // convert a boolean value to a color and a word
		if (color == false) {
			if (bool == true) return "Enabled";
			return "Disabled";
		}
		if (bool == true) return "§a";
		return "§c";
	}
	
	private String convertMode(int mode) { // convert the mode to text
		switch (mode) {
		case 0:
			return "Skywars";
		case 1:
			return "Bedwars";
		default:
			return "No Mode";
		}
	}

	private void retrieveLevels() {
		Minecraft mc = Minecraft.getMinecraft();
		clearTab();
		if (SkyHead.enabled) {
			for (EntityPlayer player : mc.theWorld.playerEntities) { // set the names of everyone in the lobby
				Events.setLevel(player);
			}
		} else {
			for (EntityPlayer player : mc.theWorld.playerEntities) { // clear the names of everyone in the lobby
				player.refreshDisplayName();
			}
		}
	}

	private void clearTab() {
		Minecraft mc = Minecraft.getMinecraft();
		GuiPlayerTabOverlay tabList = Minecraft.getMinecraft().ingameGUI.getTabList();
		for (NetworkPlayerInfo player : mc.getNetHandler().getPlayerInfoMap()) {
			if (player.getGameProfile().getName() == mc.thePlayer.getName()) continue;
			String name = tabList.getPlayerName(player).replaceAll(" " + Cache.queryCache(player.getGameProfile().getName()), "");
			player.setDisplayName(Text.ChatText(name, ""));
		}
	}

}
