package com.jkm.util;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * 随机数工具类
 */
public class RandomUtil {
    private static SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    /**
     * 将n随机分为l份（发随机红包）
     * @param n
     * @param l
     * @return
     */
    public int[] RandomAsign(int n,int l){
        int sum = 0;
        int[] a = new int[l];
        while (!getArray(n,l,sum,a)) {
            getArray(n,l,sum,a);
        }
       return a;
    }
    static boolean getArray(int n,int l,int sum,int []a) {
        for (int i = 0; i < l; i++) {
            a[i] = 0;
        }
        Random rand = new Random();
        for (int i = 0; i < l-1; i++) {
            a[i] = rand.nextInt(n - sum)+1;
            sum = sum + a[i];
            if (sum >= n) {
                return false;
            }
        }
        a[l-1]=n-sum;
        return true;
    }
}
