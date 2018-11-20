package org.chronotics.pandora.java.env;

public class EnvConfig {
    public static final int getOS() {
        String strOSName = System.getProperty("os.name").toLowerCase();

        if (strOSName.indexOf("win") >= 0) {
            return 2;
        } else if (strOSName.indexOf("nix") >= 0 || strOSName.indexOf("nux") >= 0 || strOSName.indexOf("aix") >= 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
