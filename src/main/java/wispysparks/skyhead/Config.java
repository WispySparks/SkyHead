package wispysparks.skyhead;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class Config {

    private static Configuration config; 
    private static Property modEnabled; 
    private static Property tabEnabled; 
    private static Property mode; 
    private static Property apiKey; 
    
    public static void loadConfig() { 
    	File configFile = new File(Loader.instance().getConfigDir(), "SkyHead.cfg");
    	config = new Configuration(configFile);
    	config.load();
    	modEnabled = config.get(Configuration.CATEGORY_CLIENT, "enabled", true); 
    	tabEnabled = config.get(Configuration.CATEGORY_CLIENT, "tabEnabled", false);
    	mode = config.get(Configuration.CATEGORY_CLIENT, "mode", 0); 
    	apiKey = config.get(Configuration.CATEGORY_CLIENT, "apiKey", ""); 
    }

    public static boolean isEnabled() {
        return modEnabled.getBoolean();
    }

    public static void setEnabled(boolean bool) {
        modEnabled.set(bool);
        config.save();
    }

    public static boolean isTabEnabled() {
        return tabEnabled.getBoolean();
    }

    public static void setTabEnabled(boolean bool) {
        tabEnabled.set(bool);
        config.save();
    }

    public static Mode getMode() {
        return Mode.fromIndex(mode.getInt());
    }

    public static void setMode(Mode m) {
        mode.set(m.getIndex());
    }

    public static String getAPIKey() {
        return apiKey.getString();
    }

    public static void setAPIKey(String key) {
        apiKey.set(key);
        config.save();
    }

    public enum Mode {
        SKYWARS(0, "Skywars"),
        BEDWARS(1, "Bedwars");
        private int num;
        private String name;
        Mode(int i, String s) {
            num = i;
            name = s;
        }
        public int getIndex() {
            return num;
        }
        public String getName() {
            return name;
        }
        public static Mode fromIndex(int i) {
            Mode m = SKYWARS;
            switch (i) {
                case 0: m = SKYWARS; break;
                case 1: m = BEDWARS; break;
			    default: throw new IllegalArgumentException("Invalid Index");
            }
            return m;
        }
    }

}
