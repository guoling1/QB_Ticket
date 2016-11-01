package com.jkm.util;

import java.security.MessageDigest;

public class MD5Util {
    /**
     * MD5加密
     * @author lijia
     * @param input
     * @return 加密结果
     * @throws Exception
     */
    public static String MD5(String input) throws Exception {
        if (StringIsNull(input)) {
            return "";
        }
        byte[] buf = input.getBytes("utf-8");
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(buf);
        byte[] md = m.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < md.length; i++) {
            int val = ((int) md[i]) & 0xff;
            if (val < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    public static boolean StringIsNull(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }
}
