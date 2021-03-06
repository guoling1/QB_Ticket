package com.jkm.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hutao on 15/6/25.
 * 下午2:22
 */
public final class DateFormatUtil {

    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    /**
     * DateFormat maps
     */
    private static final ImmutableMap<String, DateFormat> DATE_FORMAT_IMMUTABLE_MAP = new ImmutableMap.Builder<String, DateFormat>()
            .put("yyyyMMddHHmmss", new SimpleDateFormat(yyyyMMddHHmmss, Locale.CHINA))
            .put("yyyyMMdd", new SimpleDateFormat("yyyyMMdd", Locale.CHINA))
            .put("yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
            .put("yyyy-MM-dd hh:mm:ss", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA))
            .put("yyyy-MM-dd HH:mm", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA))
            .put("yyyy-MM-dd HH:mm:ss", new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.CHINA))
            .put("yyyyMMddHHmmssSSS", new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA))
            .put("MM-dd", new SimpleDateFormat("MM-dd", Locale.CHINA))
            .build();

    private DateFormatUtil() {
    }

    /**
     * format date to string
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(final Date date, final String format) {
        if (date == null) {
            return "";
        }
        Preconditions.checkArgument(DATE_FORMAT_IMMUTABLE_MAP.containsKey(format), format);
        return DATE_FORMAT_IMMUTABLE_MAP.get(format).format(date);
    }

    /**
     * format date to string
     *
     * @param date
     * @return
     */
    public static String format(final Date date) {
        return format(date, yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * date str -> date
     *
     * @param date
     * @param format
     * @return
     */
    public static Date parse(final String date, final String format) {
        Preconditions.checkArgument(DATE_FORMAT_IMMUTABLE_MAP.containsKey(format), format);
        try {
            return DATE_FORMAT_IMMUTABLE_MAP.get(format).parse(date);
        } catch (final ParseException e) {
            throw Throwables.propagate(e);
        }
    }
}
