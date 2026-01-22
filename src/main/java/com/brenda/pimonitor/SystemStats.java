package com.brenda.pimonitor;


/**
	Data Model ~ APIs will return structured data (JSON)
*/


public class SystemStats{
	private double cpuTemperature;
	private long totalMemoryMB;
	private long freeMemoryMB;
	private long usedMemoryMB;
	private double memoryUsagePercent;
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

	public String getUptime(){
		return uptime;
	}
	public void setUptime(String uptime){
		this.uptime = uptime;
	}
}
