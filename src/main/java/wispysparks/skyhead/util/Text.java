package wispysparks.skyhead.util;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

/**
 * Utility class used for creating ChatComponents with a color style added.
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
			default: throw new IllegalArgumentException("Invalid Color Indentifier");
		}
		return format;
	}
	
}
