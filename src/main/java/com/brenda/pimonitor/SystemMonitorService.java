package com.brenda.pimonitor;

import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
	Service/Business logic 
*/

@Service
public class SystemMonitorService{
	
	//Reads CPU Temp pf the PI
	public double getCpuTemperature() throws Exception {
		String temp = Files.readString(Paths.get("/sys/class/thermal/thermal_zone0/temp"));
		return Double.parseDouble(temp.trim())/1000.0;
	}

	//Reads Memory info from Pi
	public long[] getMemoryInfo() throws Exception {
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

	//Reads system uptime
	public String getUptime() throws Exception {
		String uptimeData = Files.readString(Paths.get("/proc/uptime"));
		double uptimeSeconds = Double.parseDouble(uptimeData.split(" ")[0]);

		long hours = (long) (uptimeSeconds / 3600);
		long minutes = (long) ((uptimeSeconds % 3600) / 60);

		return String.format("%d hours, %d minutes", hours, minutes);
	}

	//COmbining everything into a SystemStats object
	public SystemStats getSystemStats() throws Exception {
		SystemStats stats = new SystemStats();

		stats.setCpuTemperature(getCpuTemperature());

		long[] memInfo = getMemoryInfo();
		stats.setTotalMemoryMB(memInfo[0]);
		stats.setFreeMemoryMB(memInfo[1]);
		stats.setUsedMemoryMB(memInfo[2]);
		stats.setMemoryUsagePercent((double) memInfo[2] / memInfo[0] * 100);

		stats.setUptime(getUptime());

		return stats;
	}
}
