package org.chronotics.pandora.java.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.chronotics.pandora.java.exception.ExceptionUtil;
import org.chronotics.pandora.java.log.Logger;
import org.chronotics.pandora.java.log.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

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

    public static Boolean writeFile(String strContent, String strFilePath) {
        Boolean bIsWrite = true;

        try {
            String strDir = "";

            if (strFilePath.contains("/")) {
                strDir = strFilePath.substring(0, strFilePath.lastIndexOf("/"));

                File objDir = new File(strDir);

                if (!objDir.exists()) {
                    objDir.mkdirs();
                }
            }

            BufferedWriter objBufferedWriter = new BufferedWriter(new FileWriter(strFilePath));
            objBufferedWriter.write(strContent);

            objBufferedWriter.close();

            bIsWrite = true;
        } catch (Exception objEx) {
            bIsWrite = false;
            log.error("ERR", ExceptionUtil.getStrackTrace(objEx));
        }

        return bIsWrite;
    }

    public static String readFile(String strFilePath) {
        StringBuilder objContentBuilder = new StringBuilder();

        if (new File(strFilePath).exists()) {
            try (Stream<String> objStream = Files.lines(Paths.get(strFilePath), StandardCharsets.UTF_8)) {
                objStream.forEach((curLine) ->  {
                    objContentBuilder.append(curLine).append("\r\n");
                });
            } catch (Exception objEx) {
                log.error("ERR", ExceptionUtil.getStrackTrace(objEx));
            }
        }

        return objContentBuilder.toString();
    }

    public static Boolean createDirectory(String strDir) {
        Boolean bIsCreated = true;

        try {
            File objDir = new File(strDir);
            String strExtension = FilenameUtils.getExtension(strDir);

            if (!objDir.exists() && !objDir.isFile() && (strExtension == null || strExtension.isEmpty())) {
                objDir.mkdirs();
            }

        } catch (Exception objEx) {
            bIsCreated = false;
            log.error("ERR", ExceptionUtil.getStrackTrace(objEx));
        }

        return bIsCreated;
    }

    public static Boolean copyDirectory(String strFromDir, String strToDir) {
        Boolean bIsCopied = false;

        try {
            File objFromDir = new File(strFromDir);
            File objToDir = new File(strToDir);

            if (objFromDir.exists()) {
                if (!objToDir.exists()) {
                    objToDir.mkdirs();
                }

                FileUtils.copyDirectory(objFromDir, objToDir);
            } else {
                log.error("ERR", "FromDir: " + strFromDir + " is not exits");
            }
        } catch (Exception objEx) {
            bIsCopied = false;
            log.error("ERR", ExceptionUtil.getStrackTrace(objEx));
        }

        return bIsCopied;
    }

    public static String getFileNameExtension(String strFileName) {
        String strExtension = "";

        if (strFileName.contains(".")) {
            int intDotIndex = strFileName.indexOf(".");
            strExtension = strFileName.substring(intDotIndex).trim();
        }

        return strExtension;
    }
}
