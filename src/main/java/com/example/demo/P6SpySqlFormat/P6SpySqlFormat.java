package com.example.demo.P6SpySqlFormat;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6SpySqlFormat implements MessageFormattingStrategy {

    @Override
    public String formatMessage(
            int connectionId,
            String now,
            long elapsed,
            String category,
            String prepared,
            String sql,
            String url
    ) {

        if (sql.trim().isEmpty()) {
            return "";
        }

        return "\n\n" +
                "-------------------- SQL --------------------\n" +
                sql.trim() + ";\n" +
                "----------------------------------------------\n";
    }
}