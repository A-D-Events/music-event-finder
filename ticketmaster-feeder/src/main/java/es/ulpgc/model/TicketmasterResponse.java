package es.ulpgc.model;

public class TicketmasterResponse {
public String id, name, artist, date, time, venue, address, price, segment, genre, url, image;

    public TicketmasterResponse(String id, String name, String artist, String date, String time, String venue,
                 String address, String price, String segment, String genre,
                 String url, String image) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.address = address;
        this.price = price;
        this.segment = segment;
        this.genre = genre;
        this.url = url;
        this.image = image;
    }

    @Override
    public String toString() {
        return  "id: " + id +
                "\nEvent: " + name +
                "\nArtist: " + artist +
                "\nDate: " + date + time +
                "\nVenue: " + venue +
                "\nAddress: " + address +
                "\nPrice: " + price +
                "\nClassification: " + segment + " / " + genre +
                "\nURL: " + url +
                "\nImage: " + image +
                "\n--------------------------------------------------";
    }
}

