package es.ulpgc.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import es.ulpgc.config.ConfigLoader;

public class TicketmasterParamsBuilder {
    public static String build(String keyword, Integer size, Integer page, String city, String countryCode) {
        String apiKey = ConfigLoader.getProperty("ticketmaster.api.key");
        if (apiKey == null) apiKey = "";
        apiKey = apiKey.replace("\"", "");
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        StringBuilder queryBuilder = new StringBuilder("?apikey=").append(apiKey).append("&keyword=").append(encodedKeyword);
        if (size != null) queryBuilder.append("&size=").append(size);
        if (page != null) queryBuilder.append("&page=").append(page);
        if (city != null && !city.isBlank()) queryBuilder.append("&city=").append(URLEncoder.encode(city, StandardCharsets.UTF_8));
        if (countryCode != null && !countryCode.isBlank()) queryBuilder.append("&countryCode=").append(URLEncoder.encode(countryCode, StandardCharsets.UTF_8));
        return queryBuilder.toString();
    }
}
