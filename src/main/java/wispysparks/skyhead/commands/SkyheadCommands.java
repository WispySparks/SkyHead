package wispysparks.skyhead.commands;

import static wispysparks.skyhead.SkyHead.MC;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import wispysparks.skyhead.Config;
import wispysparks.skyhead.Config.Mode;
import wispysparks.skyhead.api.APILimiter;
import wispysparks.skyhead.api.Cache;
import wispysparks.skyhead.gui.Display;
import wispysparks.skyhead.util.Text;


/** 
 * Class for handling player commands.
 */  
public class SkyheadCommands extends CommandBase {
	
	private final List<String> aliases = new ArrayList<>(); 
	private final List<String> tabComplete = new ArrayList<>(); 
	private final String subCommands = "<on:off:tab:mode:key:requests:size:clear>"; 
	
	public SkyheadCommands() { 
        aliases.add("sh"); 
        tabComplete.add("on");
        tabComplete.add("off");
        tabComplete.add("tab");
        tabComplete.add("mode");
        tabComplete.add("key");
        tabComplete.add("requests");
        tabComplete.add("size");
		tabComplete.add("clear");
	}

	@Override
	public String getCommandName() {
		return "skyhead";
	}

	@Override 
    public List<String> getCommandAliases() { 
		return this.aliases;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "skyhead " + subCommands;
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
		if (commandSender instanceof EntityPlayerSP) return true; 
		return false;
	}
	
	@Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return getListOfStringsMatchingLastWord(args, tabComplete);
    }

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			if (args[0].equals("on")) { // turn mod on
				Config.setEnabled(true);
				Display.setLevels();
				MC.thePlayer.addChatMessage(Text.ChatText("Skyhead ON", "§a"));
			}
			else if (args[0].equals("off")) { // turn mod off
				Config.setEnabled(false);
				Display.setLevels();
				MC.thePlayer.addChatMessage(Text.ChatText("Skyhead OFF", "§c"));
			}
			else if (args[0].equals("tab")) { // toggle tab levels on and off
				Config.setTabEnabled(!Config.isTabEnabled());
				MC.thePlayer.addChatMessage(Text.ChatText("Tab is now ", "§6")
					.appendSibling(Text.ChatText(convertBool(Config.isTabEnabled(), false), convertBool(Config.isTabEnabled(), true))));
			}
			else if (args[0].equals("requests")) { // display number of requests this minute
				MC.thePlayer.addChatMessage(Text.ChatText("Requests made this minute: " + APILimiter.getRequests(), "§6"));
			}
			else if (args[0].equals("size")) { // display cache size
				MC.thePlayer.addChatMessage(Text.ChatText("Player Cache Size: " + Cache.getSize(), "§6"));
			}
			else if (args[0].equals("mode")) { // change level modes
				if (args.length > 1) {
					if (args[1].equals("sw")) {
						handleMode(Mode.SKYWARS);
					}
					else if (args[1].equals("bw")) {
						handleMode(Mode.BEDWARS);
					}
					else {
						MC.thePlayer.addChatMessage(Text.ChatText("Invalid Mode, Valid Modes are sw and bw", "§6"));
					}
				}
				else {
					MC.thePlayer.addChatMessage(Text.ChatText("Must Specify a Mode", "§6"));
				}
			}
			else if (args[0].equals("key")) { // set api key
				if (args.length > 1) {
					Config.setAPIKey(args[1]);
					Config.setEnabled(true);
					MC.thePlayer.addChatMessage(Text.ChatText("Set API Key to " + args[1], "§6"));
					Display.setLevels();
				}
				else {
					MC.thePlayer.addChatMessage(Text.ChatText("Must Specify API Key", "§6"));
				}
			}
			else if (args[0].equals("help")) { // help command
				MC.thePlayer.addChatMessage(Text.ChatText("Welcome to SkyHead. Here is an explanation of every command."
				+ " On and off will turn the whole mod on or off, tab will toggle showing levels in tab on and off, abbreviations are used to change the mode"
				+ " such as sw or bw. Key is used to set the api key to be used by the mod, size tells you the current size of the players cached for"
				+ " the mode you're currently in, and requests tells you how many requests have been sent to the api this minute.", "§6"));
			}
			else if (args[0].equals("clear")) { // clear player cache, unstable
				Cache.clear();
				MC.thePlayer.addChatMessage(Text.ChatText("Cleared Cache", "§6"));
				Display.setLevels();
			}
			else { // if not a valid subcommand
				MC.thePlayer.addChatMessage(Text.ChatText("Invalid Subcommand, " + subCommands, "§6"));
			}
		}
		else { // if no subcommand given
			MC.thePlayer.addChatMessage(Text.ChatText("SkyHead is currently ", "§6")
				.appendSibling(Text.ChatText(convertBool(Config.isEnabled(), false), convertBool(Config.isEnabled(), true)))
				.appendSibling(Text.ChatText(" with tab levels ", "§6"))
				.appendSibling(Text.ChatText(convertBool(Config.isTabEnabled(), false), convertBool(Config.isTabEnabled(), true)))
				.appendSibling(Text.ChatText(" and is in mode ", "§6")
				.appendSibling(Text.ChatText(Config.getMode().getName(), "§5"))));
		}
	}
	
	private String convertBool(boolean bool, boolean color) { 
		if (color == false) {
			if (bool == true) return "Enabled";
			return "Disabled";
		}
		if (bool == true) return "§a";
		return "§c";
	}
	
	private void handleMode(Mode mode) {
		Config.setMode(mode);
		Display.setLevels();
		MC.thePlayer.addChatMessage(Text.ChatText("Set to " + mode.getName() + " Mode", "§6"));
	}

}
