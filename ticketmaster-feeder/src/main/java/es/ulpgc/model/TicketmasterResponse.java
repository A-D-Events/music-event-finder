package es.ulpgc.model;

public class TicketmasterResponse {
public String name, artist, date, time, venue, address, price, segment, genre, link, image;

    public TicketmasterResponse(String name, String artist, String date, String time, String venue,
                 String address, String price, String segment, String genre,
                 String link, String image) {
        this.name = name;
        this.artist = artist;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.address = address;
        this.price = price;
        this.segment = segment;
        this.genre = genre;
        this.link = link;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Event: " + name +
                "\nArtist: " + artist +
                "\nDate: " + date + time +
                "\nVenue: " + venue +
                "\nAddress: " + address +
                "\nPrice: " + price +
                "\nClassification: " + segment + " / " + genre +
                "\nLink: " + link +
                "\nImage: " + image +
                "\n--------------------------------------------------";
    }
}

