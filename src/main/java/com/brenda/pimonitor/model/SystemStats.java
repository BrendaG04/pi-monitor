package com.brenda.pimonitor.model;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
	Data Model ~ APIs will return structured data (JSON)
*/


@JsonPropertyOrder({
	"cpuTemperature",
	"totalMemoryMB",
	"freeMemoryMB",
	"usedMemoryMB",
	"memoryUsagePercent",
	"totalDiskGB",
	"freeDiskGB",
	"usedDiskGB",
	"diskUsagePercent",
	"uptime"
})
public class SystemStats{
	private double cpuTemperature;
	private long totalMemoryMB;
	private long freeMemoryMB;
	private long usedMemoryMB;
	private double memoryUsagePercent;

	private long totalDiskGB;
	private long freeDiskGB;
	private long usedDiskGB;
	private double diskUsagePercent;

	private String uptime;

	//Constructors
	public SystemStats(){

	}

	//Getters & Setters in order to convert to JSON
	public double getCpuTemperature(){
		return cpuTemperature;
	}
	public void setCpuTemperature(double cpuTemperature){
		this.cpuTemperature = cpuTemperature;
	}

	public long getTotalMemoryMB(){
		return totalMemoryMB;
	}
	public void setTotalMemoryMB(long totalMemoryMB){
		this.totalMemoryMB = totalMemoryMB;
	}

	public long getFreeMemoryMB(){
		return freeMemoryMB;
	}
	public void setFreeMemoryMB(long freeMemoryMB){
		this.freeMemoryMB = freeMemoryMB;
	}

	public long getUsedMemoryMB(){
		return usedMemoryMB;
	}
	public void setUsedMemoryMB(long usedMemoryMB){
		this.usedMemoryMB = usedMemoryMB;
	}

	public double getMemoryUsagePercent(){
		return memoryUsagePercent;
	}
	public void setMemoryUsagePercent(double memoryUsagePercent){
		this.memoryUsagePercent = memoryUsagePercent;
	}

	public long getTotalDiskGB(){
		return totalDiskGB;
	}
	public void setTotalDiskGB(long totalDiskGB){
		this.totalDiskGB = totalDiskGB;
	}

	public long getFreeDiskGB(){
		return freeDiskGB;
	}
	public void setFreeDiskGB(long freeDiskGB){
		this.freeDiskGB = freeDiskGB;
	}

	public long getUsedDiskGB(){
		return usedDiskGB;
	}
	public void setUsedDiskGB(long usedDiskGB){
		this.usedDiskGB = usedDiskGB;
	}

	public double getDiskUsagePercent(){
		return diskUsagePercent;
	}
	public void setDiskUsagePercent(double diskUsagePercent){
		this.diskUsagePercent = diskUsagePercent;
	}

	public String getUptime(){
		return uptime;
	}
	public void setUptime(String uptime){
		this.uptime = uptime;
	}
}
