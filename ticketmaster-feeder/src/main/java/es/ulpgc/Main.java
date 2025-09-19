package es.ulpgc;

import es.ulpgc.control.TicketmasterController;

public class Main {
    public static void main(String[] args) {
        String sourceSystem = (args != null && args.length > 0 && args[0] != null && !args[0].isBlank())
                ? args[0]
                : "ticketmaster-feeder";
        TicketmasterController.processSpotifyEvents(sourceSystem);
    }
}
