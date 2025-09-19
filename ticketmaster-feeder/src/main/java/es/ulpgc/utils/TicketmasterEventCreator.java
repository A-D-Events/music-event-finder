package es.ulpgc.utils;

import java.time.LocalDateTime;

import es.ulpgc.model.TicketmasterEvent;
import es.ulpgc.model.TicketmasterResponse;

public class TicketmasterEventCreator {
    public static TicketmasterEvent fromResponse(TicketmasterResponse response, String sourceSystem) {
        return new TicketmasterEvent(
                LocalDateTime.now(), // Timestamp actual
                sourceSystem,        // Sistema origen (ej: "ticketmaster-feeder")
                response.link,       // id (puedes cambiarlo si tienes un campo id específico)
                response.name,
                response.link,       // url
                response.date,
                response.venue,
                response.address     // city (ajusta aquí si tu modelo cambia)
        );
    }
}