package org.example.api;


public class EventSearchFilter {
	public String segment;
	public String genre;
	public String countryCode;
	public String city;
	public String keyword;

	public EventSearchFilter(String segment, String genre, String countryCode, String city, String keyword) {
		this.segment = segment;
		this.genre = genre;
		this.countryCode = countryCode;
		this.city = city;
		this.keyword = keyword;
	}
}
