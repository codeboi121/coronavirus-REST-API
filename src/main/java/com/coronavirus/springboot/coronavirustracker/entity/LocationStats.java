package com.coronavirus.springboot.coronavirustracker.entity;

import java.util.LinkedHashMap;

public class LocationStats {
	private String state;
	private String country;
	private int latestTotalCases;
	
	//hashmap for storing the weekly cases dates and number of cases on that week
	private LinkedHashMap<String, Integer> lastWeekCases;

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getLatestTotalCases() {
		return latestTotalCases;
	}
	public void setLatestTotalCases(int latestTotalCases) {
		this.latestTotalCases = latestTotalCases;
	}
	public LinkedHashMap<String, Integer> getLastWeekCases() {
		return lastWeekCases;
	}
	public void setLastWeekCases(LinkedHashMap<String, Integer> lastWeekCases) {
		this.lastWeekCases = lastWeekCases;
	}
	
}
