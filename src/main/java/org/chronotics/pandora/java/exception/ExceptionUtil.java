package org.chronotics.pandora.java.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
    public static String getStrackTrace(Throwable objEx) {
        return getStackTrace(objEx);
    }

    public static String getStackTrace(Throwable objEx) {
        StringWriter objSW = new StringWriter();
        PrintWriter objPW = new PrintWriter(objSW);
        objEx.printStackTrace(objPW);

        String strStackTrace = objSW.toString();

        return strStackTrace;
    }
}
