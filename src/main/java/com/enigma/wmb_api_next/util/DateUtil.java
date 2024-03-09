package com.enigma.wmb_api_next.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date parseDate(String requestDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = simpleDateFormat.parse(requestDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
}
