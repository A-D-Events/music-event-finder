package es.ulpgc.utils;

import java.time.LocalDateTime;

import es.ulpgc.model.TicketmasterEvent;
import es.ulpgc.model.TicketmasterResponse;

public class TicketmasterEventCreator {
    public static TicketmasterEvent fromResponse(TicketmasterResponse response, String sourceSystem) {
        return new TicketmasterEvent(
                LocalDateTime.now(),
                sourceSystem,
                response.id,
                response.name,
                response.url,
                response.date,
                response.venue,
                response.address
        );
    }
}