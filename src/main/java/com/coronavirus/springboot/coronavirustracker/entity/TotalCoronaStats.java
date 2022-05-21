package com.coronavirus.springboot.coronavirustracker.entity;

import java.util.List;

public class TotalCoronaStats {
	private long totalReportedCasesAsOfNow;
	private List<LocationStats> coronaLocationStats;
	
	public List<LocationStats> getCoronaLocationStats() {
		return coronaLocationStats;
	}
	public void setCoronaLocationStats(List<LocationStats> coronaLocationStats) {
		this.coronaLocationStats = coronaLocationStats;
	}
	public long getTotalReportedCasesAsOfNow() {
		return totalReportedCasesAsOfNow;
	}
	public void setTotalReportedCasesAsOfNow(long totalReportedCasesAsOfNow) {
		this.totalReportedCasesAsOfNow = totalReportedCasesAsOfNow;
	}
	
	
}
