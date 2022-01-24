package com.haemilsoft.encare.utils;

import android.content.Context;
import android.util.Base64;

import java.math.BigInteger;


public class StringUtils {

    /**
     * Base64 인코딩
     * Base64.NO_WRAP : 개행문자 미삽입.
     */
    public static String getBase64encode(String content){
        return Base64.encodeToString(content.getBytes(), Base64.NO_WRAP);
    }

    /**
     * Base64 디코딩
     */
    public static String getBase64decode(String content){
        return new String(Base64.decode(content, 0));
    }


    public static String padLeft(String word, int totalWidth, char paddingChar) {
        String padWord = word;
        padWord = String.format("%" + totalWidth + "s", word).replace(' ', paddingChar);
        return padWord;
    }

    /**
    * String to Byte Array
    */
    public static byte[] StringToByte(String text) {
        return HexToByte(StringToHex(text));
    }

    /**
     * String to Hex String
     */
    public static String StringToHex(String text) {
        try {
            byte[] byteArrayForPlain = text.getBytes();

            String hexString = "";

            for (byte b : byteArrayForPlain) {
                hexString += Integer.toString((b & 0xF0) >> 4, 16);
                hexString += Integer.toString(b & 0x0F, 16);
            }

            return hexString;
        } catch (Exception ex) {

        }

        return null;
    }

    /**
     * Hex String to byte array
     */
    public static byte[] HexToByte(String hexString) {
        try {
            return new BigInteger(hexString, 16).toByteArray();
        } catch (Exception ex) {

        }

        return null;
    }

    /**
     * Byte Array to String
     */
    public static String ByteToString(byte[] bytes) {
        return HexToString(ByteToHex(bytes));
    }

    /**
     * byte array to Hex String
     */
    public static String ByteToHex(byte[] bytes) {
        try {
            return new BigInteger(bytes).toString(16);
        } catch (Exception ex) {

        }

        return null;
    }

    /**
     * Hex String to String
     */
    public static String HexToString(String hexString) {
        try {
            byte[] hexBytes = new byte [hexString.length() / 2];
            int j = 0;
            for (int i = 0; i < hexString.length(); i += 2) {
                hexBytes[j++] = Byte.parseByte(hexString.substring(i, i + 2), 16);
            }
            return new String(hexBytes);
        } catch (Exception ex) {

        }

        return null;
    }

    public static String TimeFormat(String timeStr) {
        return new StringBuilder()
                .append(timeStr.substring(0, 2))
                .append(":")
                .append(timeStr.substring(2, 4))
                .append(":")
                .append(timeStr.substring(4))
                .toString();
    }

    public static String ConvertToPhoneNumberFormat(String number) {
        if (number == null) return "";
        number = number.replace("+82", "0");

        if (number.length() == 10) {
            return new StringBuilder()
                    .append(number.substring(0, 3))
                    .append("-")
                    .append(number.substring(3, 6))
                    .append("-")
                    .append(number.substring(6, 10))
                    .toString();
        } else if (number.length() == 11) {
            return new StringBuilder()
                    .append(number.substring(0, 3))
                    .append("-")
                    .append(number.substring(3, 7))
                    .append("-")
                    .append(number.substring(7, 11))
                    .toString();
        } else {
            return number;
        }
    }
}
