package com.qvl.springairag.utils;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

public class DateTool {

    // 取得當前日期時間並符合使用者時區
    @Tool(description = "Get the current date and time in the user's timezone")
    public static String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
