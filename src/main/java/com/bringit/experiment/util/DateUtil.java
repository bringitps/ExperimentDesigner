package com.bringit.experiment.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vf-root on 15/4/17.
 */
public class DateUtil {

    public static Date getDate() {
        Date updateDate = new Date();
        DateFormat dfUpdateDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateDateStr = dfUpdateDate.format(updateDate);
        try {
            updateDate = dfUpdateDate.parse(updateDateStr);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return updateDate;
    }
}
