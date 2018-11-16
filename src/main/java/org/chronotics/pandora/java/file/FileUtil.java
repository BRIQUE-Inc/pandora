package org.chronotics.pandora.java.file;

import org.chronotics.pandora.java.exception.ExceptionUtil;
import org.chronotics.pandora.java.log.Logger;
import org.chronotics.pandora.java.log.LoggerFactory;

import java.io.File;

public class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    public static String createFile(String strFileName) {
        try {
            if (new File(strFileName).exists()) {
                new File(strFileName).delete();
            }

            File objFileName = new File(strFileName);
            File objDir = objFileName.getParentFile();

            if (!objDir.exists()) {
                objDir.mkdirs();
            }

            new File(strFileName).createNewFile();

            return strFileName;
        } catch (Exception objEx) {
            log.error("ERR: " + ExceptionUtil.getStrackTrace(objEx));

            return "";
        }
    }
}
