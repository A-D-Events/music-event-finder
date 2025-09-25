package es.ulpgc.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import es.ulpgc.config.ConfigLoader;

public class TicketmasterParamsBuilder {
    public static String build(String keyword) {
        String apiKey = ConfigLoader.getProperty("ticketmaster.api.key");
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        String query = "?apikey=" + apiKey + "&keyword=" + encodedKeyword;
        return query;
    }
}
