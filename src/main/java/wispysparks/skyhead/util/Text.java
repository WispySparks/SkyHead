package wispysparks.skyhead.util;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

/**
 * Utility class used for working with text and strings.
 */
public class Text {
	
	/**
	 * @return a colored text component using the color code
	 */
	public static ChatComponentText ChatText(String text, String colorCode) {
		ChatComponentText chatText = new ChatComponentText(text);
		ChatStyle cs = new ChatStyle();
		cs.setColor(getColor(colorCode));
		chatText.setChatStyle(cs);
		return chatText;
	}
	
	private static EnumChatFormatting getColor(String color) { 
		EnumChatFormatting format;
		switch (color) {
			case "§0": format = EnumChatFormatting.BLACK; break;
			case "§1": format = EnumChatFormatting.DARK_BLUE; break;
			case "§2": format = EnumChatFormatting.DARK_GREEN; break;
			case "§3": format = EnumChatFormatting.DARK_AQUA; break;
			case "§4": format = EnumChatFormatting.DARK_RED; break;
			case "§5": format = EnumChatFormatting.DARK_PURPLE; break;
			case "§6": format = EnumChatFormatting.GOLD; break;
			case "§7": format = EnumChatFormatting.GRAY; break;
			case "§8": format = EnumChatFormatting.DARK_GRAY; break;
			case "§9": format = EnumChatFormatting.BLUE; break;
			case "§a": format = EnumChatFormatting.GREEN; break;
			case "§b": format = EnumChatFormatting.AQUA; break;
			case "§c": format = EnumChatFormatting.RED; break;
			case "§d": format = EnumChatFormatting.LIGHT_PURPLE; break;
			case "§e": format = EnumChatFormatting.YELLOW; break;
			case "§f": format = EnumChatFormatting.WHITE; break;
			default: format = EnumChatFormatting.WHITE; break;
		}
		return format;
	}

	public static String getFormattedBWLevel(String level) { 
		String colorCode = "§7";
		String assembledLevel = " " + colorCode + level.trim() + "✫";
		boolean rainbow = false;
		String rainbowLevel = "";
    	int levelNum = Integer.parseInt(level);
    	if (levelNum < 100) colorCode = "§7"; 
    	else if (levelNum < 200) colorCode = "§f";
    	else if (levelNum < 300) colorCode = "§6";
    	else if (levelNum < 400) colorCode = "§b";
    	else if (levelNum < 500) colorCode = "§2";
    	else if (levelNum < 600) colorCode = "§3";
    	else if (levelNum < 700) colorCode = "§4";
    	else if (levelNum < 800) colorCode = "§d";
    	else if (levelNum < 900) colorCode = "§9";
    	else if (levelNum < 1000) colorCode = "§5";
    	else {
			rainbowLevel = toRainbowString(level + "✫");
			rainbow = true;
		}  
		if (rainbow) {
			assembledLevel = " " + rainbowLevel;
		} 
		else {
			assembledLevel = " " + colorCode + level.trim() + "✫";
		}
		return assembledLevel;
    }

	private static String toRainbowString(String string) { 
		String rainbowString = "";
		String[] colorChar = {
			EnumChatFormatting.RED.toString(),
			EnumChatFormatting.GOLD.toString(),
			EnumChatFormatting.YELLOW.toString(),
			EnumChatFormatting.GREEN.toString(),
			EnumChatFormatting.AQUA.toString(),
			EnumChatFormatting.BLUE.toString(),
			EnumChatFormatting.LIGHT_PURPLE.toString(),
			EnumChatFormatting.DARK_PURPLE.toString()
		};
		for (int i = 0; i < string.length(); i++) {
			rainbowString += colorChar[i%8] + string.charAt(i);
		}
		return rainbowString;
	}
	
}
