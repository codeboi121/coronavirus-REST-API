package com.coronavirus.springboot.coronavirustracker.rest;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coronavirus.springboot.coronavirustracker.entity.LocationStats;
import com.coronavirus.springboot.coronavirustracker.entity.TotalCoronaStats;

@RestController
@RequestMapping("/api")
public class CoronaRESTController {
	//url for remote coronaVirus data.CSV file
	private static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	
	private List<LocationStats> allStats=new ArrayList<>();
	private TotalCoronaStats completeAllStats=new TotalCoronaStats();
	private long totalReportedCasesAsOfNow;
	 
	@PostConstruct
	public void loadData() throws IOException, InterruptedException{
		//for avoiding concurrency
		List<LocationStats> newStats=new ArrayList<>();
						
		//calculating the dates headings
		String [] dates=new String[7];
						
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		cal.add(Calendar.DATE, -7);
		for (int i = 0; i < dates.length; i++) {
			Date todate = cal.getTime();
			dates[i]=dateFormat.format(todate);
			cal.add(Calendar.DATE, +1);
		}
				 
		//creating an http client and making an http GET request to the remote coronaVirus data.CSV file
		HttpClient client=HttpClient.newHttpClient();
		HttpRequest request=HttpRequest.newBuilder()
								.uri(URI.create(VIRUS_DATA_URL))
								.build();
		HttpResponse<String> httpResponse=client.send(request, HttpResponse.BodyHandlers.ofString());
						
		StringReader csvBodyReader=new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		
		for (CSVRecord record : records) {
			LocationStats locationStat=new LocationStats();
//			LinkedHashMap<String, Integer> lastWeekCases= locationStat.getLastWeekCases();
			LinkedHashMap<String, Integer> lastWeekCases=new LinkedHashMap<String, Integer>();
			
			locationStat.setState(record.get("Province/State"));
			locationStat.setCountry(record.get("Country/Region"));
			locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
			
			//calculating the weekly cases
			int []weeklyCases=new int[7];
			for(int i=0;i<=6;i++) {
				weeklyCases[i]=Integer.parseInt(record.get(record.size()-((-i)+7)))-Integer.parseInt(record.get(record.size()-((-i)+8)));
				lastWeekCases.put(dates[i], weeklyCases[i]);
				locationStat.setLastWeekCases(lastWeekCases);
			} 
			newStats.add(locationStat);
		} 
		this.allStats=newStats;
		totalReportedCasesAsOfNow=allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
		
		completeAllStats.setCoronaLocationStats(allStats);
		completeAllStats.setTotalReportedCasesAsOfNow(totalReportedCasesAsOfNow);
	}
	
	@GetMapping("/corona")
	public TotalCoronaStats getCoronaList(){
		return completeAllStats;
	}

	public List<LocationStats> getCompleteAllStats() {
		return allStats;
	}

	@GetMapping("/corona/{countryId}")
	public LocationStats CountryCorona(@PathVariable("countryId") String countryId) {
		//searching for a specific string in the arrayList of objects
		for (LocationStats locationStats : allStats) {
			if(countryId.equals(locationStats.getCountry()))
				return locationStats;
		}
		return 	null;
	}
}


