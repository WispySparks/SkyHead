package com.wispy.skyhead.util;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class Text {
	
	public static ChatComponentText ChatText(String text, String colorCode) { // simplified code to create a colored chat component
		ChatComponentText chatText = new ChatComponentText(text);
		ChatStyle cs = new ChatStyle();
		EnumChatFormatting color = EnumChatFormatting.WHITE;
		if (colorCode != null) {
			color = getColor(colorCode);
		}
		cs.setColor(color);
		chatText.setChatStyle(cs);
		return chatText;
	}
	
	private static EnumChatFormatting getColor(String color) {
		if (color == "§0") {
			return EnumChatFormatting.BLACK;
		}
		else if (color == "§1") {
			return EnumChatFormatting.DARK_BLUE;
		}
		else if (color == "§2") {
			return EnumChatFormatting.DARK_GREEN;
		}
		else if (color == "§3") {
			return EnumChatFormatting.DARK_AQUA;
		}
		else if (color == "§4") {
			return EnumChatFormatting.DARK_RED;
		}
		else if (color == "§5") {
			return EnumChatFormatting.DARK_PURPLE;
		}
		else if (color == "§6") {
			return EnumChatFormatting.GOLD;
		}
		else if (color == "§7") {
			return EnumChatFormatting.GRAY;
		}
		else if (color == "§8") {
			return EnumChatFormatting.DARK_GRAY;
		}
		else if (color == "§9") {
			return EnumChatFormatting.BLUE;
		}
		else if (color == "§a") {
			return EnumChatFormatting.GREEN;
		}
		else if (color == "§b") {
			return EnumChatFormatting.AQUA;
		}
		else if (color == "§c") {
			return EnumChatFormatting.RED;
		}
		else if (color == "§d") {
			return EnumChatFormatting.LIGHT_PURPLE;
		}
		else if (color == "§e") {
			return EnumChatFormatting.YELLOW;
		}
		else {
			return EnumChatFormatting.WHITE;
		}
	}
	
}
