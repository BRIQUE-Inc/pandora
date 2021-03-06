package org.chronotics.pandora.java.converter;

import org.apache.commons.lang3.SerializationUtils;
import org.chronotics.pandora.java.exception.ExceptionUtil;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

public class ConverterUtil {
    public static Object convertByteArrayToObject(byte[] arrByte, String strClass) {
        try {
            ByteOrder objByteOrder = ByteOrder.LITTLE_ENDIAN;
            Integer intNumBytes = 0;

            if (strClass.contains("float")) {
                intNumBytes = Float.BYTES;
            } else if (strClass.contains("int")) {
                intNumBytes = Integer.BYTES;
            } else if (strClass.contains("short")) {
                intNumBytes = Short.BYTES;
            } else if (strClass.contains("char")) {
                intNumBytes = Character.BYTES;
            } else if (strClass.contains("string")){
                Charset objCharset = StandardCharsets.UTF_8;

                if (objByteOrder.equals(ByteOrder.LITTLE_ENDIAN)) {
                    byte[] arrNewByte = new byte[arrByte.length];

                    for (int intCount = 0; intCount < arrByte.length; intCount++) {
                        arrNewByte[intCount] = arrByte[arrByte.length - 1 - intCount];
                    }

                    String strReturn = new String(arrNewByte).trim();

                    return strReturn.replace("\\u0000", "").replace("\u0000", "").trim();

                } else {
                    return new String(arrByte).replace("\\u0000", "").replace("\u0000", "").trim();
                }
                //return new String(arrByte).trim();
            } else {
                return SerializationUtils.deserialize(arrByte);
            }

            if (intNumBytes > 0) {
                Integer intMissingByte = intNumBytes - arrByte.length;
                byte[] arrNewByte = new byte[intNumBytes];

                if (objByteOrder.equals(ByteOrder.BIG_ENDIAN)) {
                    for (int intCount = 0; intCount < intMissingByte; intCount++) {
                        arrNewByte[intCount] = (byte) 0;
                    }

                    for (int intCount = 0; intCount < arrByte.length; intCount++) {
                        arrNewByte[intMissingByte + intCount] = arrByte[intCount];
                    }
                } else {
                    for (int intCount = 0; intCount < arrByte.length; intCount++) {
                        arrNewByte[intCount] = arrByte[intCount];
                    }

                    for (int intCount = 0; intCount < intMissingByte; intCount++) {
                        arrNewByte[intMissingByte + intCount] = (byte) 0;
                    }
                }

                if (strClass.contains("float")) {
                    Float objObject = ByteBuffer.wrap(arrNewByte, 0, Float.BYTES).order(objByteOrder).getFloat();

                    if (objObject.equals(Float.NaN) || objObject.equals(Float.POSITIVE_INFINITY) || objObject.equals(Float.NEGATIVE_INFINITY)) {
                        return null;
                    } else {
                        return objObject;
                    }
                } else if (strClass.contains("int")) {
                    Integer objObject = ByteBuffer.wrap(arrNewByte, 0, Integer.BYTES).order(objByteOrder).getInt();

                    return objObject;
                } else if (strClass.contains("short")) {
                    return ByteBuffer.wrap(arrNewByte, 0, Short.BYTES).order(objByteOrder).getShort();
                } else if (strClass.contains("char")) {
                    return ByteBuffer.wrap(arrNewByte, 0, Character.BYTES).order(objByteOrder).getChar();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception objEx) {
            return null;
        }
    }

    public static String convertDashField(String strDashField) {
        if (strDashField.contains("-")) {
            return "['" + strDashField + "']";
        } else {
            return "." + strDashField;
        }
    }
    public static String convertDateToString(Date objDate, String strFormat) {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(strFormat);
        return objSimpleDateFormat.format(objDate);
    }

    public static Double randomDouble(Random objRandom, Double dbMin, Double dbMax) {
        return (dbMin + (dbMax - dbMin) * objRandom.nextDouble());
    }

    public static Long convertStringToLocalTimestamp(String strDate, String strFormat) {
        Long lMillis = 0L;

        try {
            SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(strFormat);
            objSimpleDateFormat.setTimeZone(TimeZone.getDefault());
            Date objDate = objSimpleDateFormat.parse(strDate);
            Calendar objCalender = Calendar.getInstance();
            objCalender.setTime(objDate);
            lMillis = objCalender.getTimeInMillis();
        } catch (Exception objEx) {
        }

        return lMillis;
    }

    public static Long convertMillisecondsToLocationTimestamp(Calendar objDate) {
        objDate.setTimeZone(TimeZone.getDefault());
        return objDate.getTimeInMillis();
    }

    public static String convertTimestampToLocalString(Long objTimestamp, String strFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(objTimestamp);
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(strFormat);
        objSimpleDateFormat.setTimeZone(TimeZone.getDefault());
        return objSimpleDateFormat.format(calendar.getTime());
    }

    private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>();

    static {

        DATE_FORMAT_REGEXPS.put("^\\d{8}$", "yyyyMMdd");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "dd/MM/yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        DATE_FORMAT_REGEXPS.put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}\\.\\d{1,3}$",
                "yyyy-MM-dd HH:mm:ss.S");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd/MM/yyyy HH:mm:ss");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
    }

    public static List<String> getDateFormats() {
        return Arrays.asList(DATE_FORMAT_REGEXPS.values().toArray(new String[DATE_FORMAT_REGEXPS.size()]));
    }

    private static Boolean tryBoolean(String strText) {
        try {
            strText = strText.toLowerCase();

            switch (strText) {
                case "true":
                    return true;
                case "false":
                    return false;
                case "1":
                    return true;
                case "0":
                    return false;
                case "y":
                    return true;
                case "n":
                    return false;
                default:
                    return null;
            }
        } catch (Exception objEx) {
            return null;
        }
    }

    private static Date tryDate(String strText) {
        for (Map.Entry<String, String> entry : DATE_FORMAT_REGEXPS.entrySet()) {
            if (strText.toLowerCase().matches(entry.getKey())) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(entry.getValue());
                try {
                    return dateFormat.parse(strText);
                } catch (ParseException e) {
                }
            }
        }
        return null;
    }

    private static Float tryFloat(String strText) {
        try {
            if (strText.contains(".")) {
                return Float.valueOf(strText);
            } else {
                return null;
            }
        } catch (Exception objEx) {
            return null;
        }
    }

    private static Long tryLong(String strText) {
        try {
            return Long.valueOf(strText);
        } catch (Exception objEx) {
            return null;
        }
    }

    private static Integer tryInteger(String strText) {
        try {
            return Integer.valueOf(strText);
        } catch (Exception objEx) {
            return null;
        }
    }

    /*
     *
     */
    private static Boolean tryBoolean(Object objValue) {
        try {
            if (objValue instanceof Boolean) {
                return (Boolean)objValue;
            } else {
                String strText = objValue.toString().toLowerCase();

                return tryBoolean(strText);
            }
        } catch (Exception objEx) {
            return null;
        }
    }

    private static Date tryDate(Object objValue) {
        if (objValue instanceof Date) {
            return (Date)objValue;
        } else if (objValue instanceof Calendar) {
            return ((Calendar)objValue).getTime();
        } else {
            String strText = objValue.toString();
            return tryDate(strText);
        }
    }

    private static Float tryFloat(Object objValue) {
        if (objValue instanceof Float) {
            return (Float)objValue;
        } else if (objValue instanceof Double) {
            return Float.valueOf(((Double)objValue).toString());
        } else {
            String strText = objValue.toString();
            return tryFloat(strText);
        }
    }

    private static Long tryLong(Object objValue) {
        if (objValue instanceof Long) {
            return (Long)objValue;
        } else {
            String strText = objValue.toString();
            return tryLong(strText);
        }
    }

    private static Integer tryInteger(Object objValue) {
        if (objValue instanceof Integer) {
            return (Integer)objValue;
        } else {
            return tryInteger(objValue.toString());
        }
    }

    public static Object tryArray(Object objValue) {
        if (objValue instanceof ArrayList) {
            List<Object> lstObj = (ArrayList)objValue;

            if (lstObj != null && lstObj.size() > 0) {
                return convertObjectToDataType(lstObj.get(0));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static final List<Function<String, Object>> FUNCTIONS = Arrays.asList(s -> tryDate(s),
            s -> tryFloat(s), s -> tryInteger(s), s -> tryLong(s), s -> tryBoolean(s));

    private static final List<Function<Object, Object>> FUNCTION_OBJS = Arrays.asList(s -> tryArray(s), s -> tryDate(s),
            s -> tryFloat(s), s -> tryInteger(s), s -> tryLong(s), s -> tryBoolean(s));

    public static Object convertStringToDataType(String strValueAsString) {
        return FUNCTIONS.stream().map(f -> f.apply(strValueAsString)).filter(Objects::nonNull).findFirst().orElse(strValueAsString);
    }

    public static Object convertObjectToDataType(Object objValue) {
        return FUNCTION_OBJS.stream().map(f -> f.apply(objValue)).filter(Objects::nonNull).findFirst().orElse(objValue);
    }
}
