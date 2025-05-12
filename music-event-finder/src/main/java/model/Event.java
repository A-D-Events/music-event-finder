package model;

public class Event {
	public String name, artist, date, time, venue, address, price, segment, genre, link, image;

	public Event(String name, String artist, String date, String time, String venue,
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
		return "🎤 Event: " + name +
				"\n🎶 Artist: " + artist +
				"\n📅 Date: " + date + " 🕒 " + time +
				"\n🌆 Venue: " + venue +
				"\n📍 Address: " + address +
				"\n💵 Price: " + price +
				"\n🎭 Classification: " + segment + " / " + genre +
				"\n🔗 Link: " + link +
				"\n🖼️ Image: " + image +
				"\n--------------------------------------------------";
	}
}
