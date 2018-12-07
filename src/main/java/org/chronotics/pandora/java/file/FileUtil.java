package org.chronotics.pandora.java.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.chronotics.pandora.java.exception.ExceptionUtil;
import org.chronotics.pandora.java.log.Logger;
import org.chronotics.pandora.java.log.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
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

    public static byte[] readFileByte(String strFilePath) {
        if (new File(strFilePath).exists()) {
            try {
                byte[] arrContentBytes = Files.readAllBytes(Paths.get(strFilePath));

                return arrContentBytes;
            } catch (Exception objEx) {
                log.error("ERR: ", ExceptionUtil.getStrackTrace(objEx));

                return null;
            }
        } else {
            return null;
        }
    }

    public static HashMap<String, byte[]> readStreamFileWithBuffer(BufferedInputStream objBufferedInputStream, Long lCurOffset, Integer intBufferLength, HashMap<Integer, String> mapFieldOffset) {
        HashMap<String, byte[]> mapContent = new HashMap<>();

        try {
            byte[] arrBuffer = new byte[intBufferLength];

            Integer intCurReadLength = objBufferedInputStream.read(arrBuffer);

            if (intCurReadLength != -1) {
                List<Integer> lstFieldOffset = mapFieldOffset.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());

                for (int intCount = 0; intCount < lstFieldOffset.size(); intCount++) {
                    Integer intCurFieldOffset = lstFieldOffset.get(intCount);
                    Integer intLengthField = (intCount < lstFieldOffset.size() - 1) ? (lstFieldOffset.get(intCount + 1) - intCurFieldOffset) : (arrBuffer.length - intCurFieldOffset);

                    if (intLengthField > 0) {
                        byte[] arrCurFieldByte = Arrays.copyOfRange(arrBuffer, intCurFieldOffset, intCurFieldOffset + intLengthField);
                        String strCurFieldName = mapFieldOffset.get(intCurFieldOffset);

                        mapContent.put(strCurFieldName, arrCurFieldByte);
                    } else {
                        throw new Exception();
                    }
                }
            }
        } catch (Exception objEx) {
            log.error("ERR: " + ExceptionUtil.getStrackTrace(objEx));
            mapContent = null;
        }

        return mapContent;
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
