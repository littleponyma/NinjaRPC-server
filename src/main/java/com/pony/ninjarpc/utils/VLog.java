package com.pony.ninjarpc.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VLog {
    private static final Logger log = LoggerFactory.getLogger(VLog.class);
    public static void e(String data) {
        if (data != null) {
            log.info(data);
        }
    }
}
