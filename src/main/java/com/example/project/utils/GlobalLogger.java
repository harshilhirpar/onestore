package com.example.project.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalLogger {
    public static Logger getLogger(Class<?> clazz){
        return LoggerFactory.getLogger(clazz);
    }
}
