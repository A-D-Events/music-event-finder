package model;

public class Event {
	public String name;
	public String artist;
	public String date;
	public String time;
	public String venue;
	public String address;
	public String price;
	public String segment;
	public String genre;
	public String link;
	public String image;

	public Event(String name, String artist, String date, String time,
				 String venue, String address, String price,
				 String segment, String genre, String link, String image) {
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
		return name + " by " + artist + " on " + date;
	}
}
