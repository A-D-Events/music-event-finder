package es.ulpgc;

import es.ulpgc.control.TicketmasterController;

public class Main {
    public static void main(String[] args) {
        String sourceSystem = "ticketmaster-feeder";
        TicketmasterController.processSpotifyEvents(sourceSystem);
    }
}
