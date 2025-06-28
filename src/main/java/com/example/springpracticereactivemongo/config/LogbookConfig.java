package com.example.springpracticereactivemongo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.Sink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.logstash.LogstashLogbackSink;

/**
 * Configuration class for Logbook integration.
 * Provides beans for logging HTTP requests and responses using Logstash and JSON formatting.
 */
@Configuration
public class LogbookConfig {

    /**
     * Creates a Logbook Sink bean configured with a JSON HTTP log formatter and Logstash integration.
     *
     * @return a Sink instance for logging HTTP traffic
     */
    @Bean
    public Sink logbookLogStash() {
        HttpLogFormatter formatter = new JsonHttpLogFormatter();
        return new LogstashLogbackSink(formatter);
    }
}