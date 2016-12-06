package com.jkm.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 读取请求参数
 * Created by lt on 2016/12/6.
 */
public class ReadParameter {

    public static String getProcess(final HttpServletRequest request) {
        StringBuffer sb = null;
        InputStream is = null;
        BufferedReader br = null;
        String s = "";
        try {
            sb = new StringBuffer();
            is = request.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            if (sb.toString().length() <= 0) {
                return null;
            } else {
                return sb.toString();
            }
        } catch (Exception e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }
}
