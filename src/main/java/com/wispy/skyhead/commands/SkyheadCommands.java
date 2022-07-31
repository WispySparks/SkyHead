package com.wispy.skyhead.commands;

import java.util.ArrayList;
import java.util.List;

import com.wispy.skyhead.Cache;
import com.wispy.skyhead.SkyHead;
import com.wispy.skyhead.api.API;
import com.wispy.skyhead.api.APILimiter;
import com.wispy.skyhead.gui.Display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.Property;

public class SkyheadCommands extends CommandBase {
	
	private final List aliases;
	
	public SkyheadCommands() { // make sh also a valid command
		aliases = new ArrayList(); 
        aliases.add("sh"); 
	}

	@Override
	public String getCommandName() { // set cmd name
		return "skyhead";
	}

	@Override
	public String getCommandUsage(ICommandSender arg0) {
		return "skyhead <on:off:requests:size:key>";
	}
	
	@Override 
    public List getCommandAliases() {
		return this.aliases;
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
		if (commandSender instanceof EntityPlayerSP) return true; // only work if player is local player
		return false;
	}

	@Override
	public void processCommand(ICommandSender arg0, String[] args) throws CommandException {
		if (args.length > 0) {
			if (args[0].equals("on")) { // turn mod on
				Property prop = SkyHead.config.get(SkyHead.config.CATEGORY_CLIENT, "enabled", true); // get config property and set it so that it persists between launches
				prop.setValue(true); 
				SkyHead.config.save();
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§aSkyhead ON"));
				SkyHead.enabled = true;
			}
			else if (args[0].equals("off")) { // turn mod off
				Property prop = SkyHead.config.get(SkyHead.config.CATEGORY_CLIENT, "enabled", true); // get config property and set it so that it persists between launches
				prop.setValue(false);
				SkyHead.config.save();
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§cSkyhead OFF"));
				SkyHead.enabled = false;
				Display.currentLevel = "";
			}
			else if (args[0].equals("requests")) { // display number of requests this minute
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6Requests made this minute: " + APILimiter.requests));
			}
			else if (args[0].equals("size")) { // display cache size
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6Player Cache Size: " + Cache.getSize()));
			}
			else if (args[0].equals("key")) {
				if (args.length > 1) {
					Property prop = SkyHead.config.get(SkyHead.config.CATEGORY_CLIENT, "apiKey", ""); // get config property and set it so that it persists between launches
					prop.setValue(args[1]);  // set key in config and in code
					SkyHead.config.save();
					API.apikey = args[1];
					SkyHead.enabled = true;
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6Set API Key to " + args[1]));
				}
				else {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6Must Specify API Key"));
				}
			}
			else { // if not a valid subcommand
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6Invalid Subcommand, <on:off:requests:size:key>"));
			}
		}
		else { // if no subcommand given
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6No Subcommand, <on:off:requests:size:key>"));
		}
	}

}
