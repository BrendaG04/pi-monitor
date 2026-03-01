package com.brenda.pimonitor.service;

import com.brenda.pimonitor.model.SystemStats;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
	Service/Business logic ~ Separation of concerns
*/

@Service
public class SystemMonitorService{

	private final boolean isCloudEnvironment;
   	private long cloudStartTime; // Tracks when service started for realistic uptime

	public SystemMonitorService() {
    		// Check for cloud environment variables
    		String railwayEnv = System.getenv("RAILWAY_ENVIRONMENT");
    		String azureEnv = System.getenv("WEBSITE_SITE_NAME");

    		// If cloud env vars exist, or /sys/class/thermal is missing, it's cloud
    		this.isCloudEnvironment = (railwayEnv != null) || (azureEnv != null) || !new java.io.File("/sys/class/thermal/thermal_zone0/temp").exists();

    		if (isCloudEnvironment) {
		        this.cloudStartTime = System.currentTimeMillis();
       	 		System.out.println("Running in cloud environment - using mock data");
    		} else {
        		System.out.println("Running on Physical Hardware - reading system files");
    		}
	}


	//Reads CPU Temp pf the PI
	public double getCpuTemperature() throws Exception {

		if (isCloudEnvironment) {
           		double baseTemp = 45.0;
            		double variation = (Math.random() * 5.0) - 2.5; // ±2.5°C variation
            		double temp = baseTemp + variation;
            		return Math.round(temp * 10.0) / 10.0; // Round to 1 decimal
		}

		String temp = Files.readString(Paths.get("/sys/class/thermal/thermal_zone0/temp"));
		double celsius = Double.parseDouble(temp.trim())/1000.0;
		return Math.round(celsius * 10.0) / 10.0;
	}

	//Reads Memory info from Pi
	public long[] getMemoryInfo() throws Exception {

		if (isCloudEnvironment) {
            		long totalMB = 3796;
            		double usagePercent = 0.60 + (Math.random() * 0.20); // 60-80% usage
            		long usedMB = (long)(totalMB * usagePercent);
            		long availableMB = totalMB - usedMB;
            		return new long[]{totalMB, availableMB, usedMB};
		}
	
		String memInfo = Files.readString(Paths.get("/proc/meminfo"));
		long totalKB = 0;
		long freeKB = 0;
		long availableKB = 0;

		for (String line : memInfo.split("\n")) {
			if (line.startsWith("MemTotal:")) {
				totalKB = Long.parseLong(line.split("\\s+")[1]);
			} else if (line.startsWith("MemAvailable:")){
				availableKB = Long.parseLong(line.split("\\s+")[1]);
			}
		}

		long totalMB = totalKB / 1024;
		long availableMB = availableKB /1024;
		long usedMB = totalMB - availableMB;

		return new long[]{totalMB, availableMB, usedMB};
	}

	//COmbining everything into a SystemStats object
	public SystemStats getSystemStats() throws Exception {
		SystemStats stats = new SystemStats();

		stats.setCpuTemperature(getCpuTemperature());

		long[] memInfo = getMemoryInfo();
		stats.setTotalMemoryMB(memInfo[0]);
		stats.setFreeMemoryMB(memInfo[1]);
		stats.setUsedMemoryMB(memInfo[2]);
		double memPercent = (double) memInfo[2] / memInfo[0] * 100;
		stats.setMemoryUsagePercent(Math.round(memPercent * 100.0) / 100.0);

		long[] diskInfo = getDiskSpace();
		stats.setTotalDiskGB(diskInfo[0]);
		stats.setFreeDiskGB(diskInfo[1]);
		stats.setUsedDiskGB(diskInfo[2]);
		double diskPercent = (double) diskInfo[2] / diskInfo[0] * 100;
		stats.setDiskUsagePercent(Math.round(diskPercent * 100.0) / 100.0);

		stats.setUptime(getUptime());
		return stats;
	}

	//Reads Disk info from Pi
	public long[] getDiskSpace() throws Exception {

		if (isCloudEnvironment) {
            		// Mock disk data for cloud
            		long totalGB = 28;
            		double usagePercent = 0.40 + (Math.random() * 0.20); // 40-60% usage
            		long usedGB = (long)(totalGB * usagePercent);
            		long availableGB = totalGB - usedGB;
            		return new long[]{totalGB, availableGB, usedGB};
        	}


		//Disk Info comes from the df command, so ProcessBuilder is needed to run a shell command
		ProcessBuilder pb = new ProcessBuilder("df" , "-B1", "/");
		Process process = pb.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line;
		long totalBytes = 0;
		long usedBytes = 0;
		long availableBytes = 0;

		reader.readLine();

		line = reader.readLine();
		if (line != null) {
			//CL format: Filesystem 1b-blocks Used Available Use% Mounted
			String[] parts = line.trim().split("\\s+");
			//[0] = filesystem, [1] = total, [2] = used, [3] = available
			totalBytes = Long.parseLong(parts[1]);
			usedBytes = Long.parseLong(parts[2]);
			availableBytes = Long.parseLong(parts[3]);
		}

		reader.close();
		process.waitFor();

		long totalGB = totalBytes / (1024 * 1024 * 1024);
		long usedGB = usedBytes / (1024 * 1024 * 1024);
		long availableGB= availableBytes / (1024 * 1024 * 1024);

		return new long[]{totalGB, availableGB, usedGB};
	}

	//Reads system uptime
	public String getUptime() throws Exception {

        	if (isCloudEnvironment) {
            		// Mock uptime for cloud
	            	long currentTime = System.currentTimeMillis();
        	    	long uptimeMillis = currentTime - cloudStartTime;
            	    	long uptimeSeconds = uptimeMillis / 1000;
            
            		long days = uptimeSeconds / 86400;
            	    	long hours = (uptimeSeconds % 86400) / 3600;
            	    	long minutes = (uptimeSeconds % 3600) / 60;
           	 // Format based on duration
            		if (days > 0) {
                		return String.format("%d days, %d hours", days, hours);
            		} else if (hours > 0) {
                		return String.format("%d hours, %d minutes", hours, minutes);
            		} else {
                		return String.format("%d minutes", minutes);
            		}
        	}

		String uptimeData = Files.readString(Paths.get("/proc/uptime"));
		double uptimeSeconds = Double.parseDouble(uptimeData.split(" ")[0]);

		long hours = (long) (uptimeSeconds / 3600);
		long minutes = (long) ((uptimeSeconds % 3600) / 60);

		return String.format("%d hours, %d minutes", hours, minutes);
	}
}

