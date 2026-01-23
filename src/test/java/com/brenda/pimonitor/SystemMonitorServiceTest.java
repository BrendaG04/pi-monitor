package com.brenda.pimonitor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
	Testing & Debugging for the Service Class
*/


public class SystemMonitorServiceTest{

	private SystemMonitorService service;

	@BeforeEach
	public void setUp() {
		service = new SystemMonitorService();
	}

	@Test
	public void testGetCpuTemperature() throws Exception{
		double temp = service.getCpuTemperature();

		//Ensures the temp value is reasonable
		assertTrue(temp > 20.0, "Temperature should be above 20°C");
		assertTrue(temp < 85.0, "Temperature should be below 85°C");
		System.out.println("✔ CPU Temperature test passed: " + temp + "°C");
	}

	@Test
	public void testGetMemoryInfo() throws Exception {
		long[] memInfo = service.getMemoryInfo();

		//Enures list has 3 elements ~ [0] = total, [1] = available, [2] = used
		assertEquals(3, memInfo.length, "Should return array of 3 elements");

		assertTrue(memInfo[0] > 512, "Total memory should be at least 512 MB");
		assertTrue(memInfo[1] > 0, "Available memory should be positive");
		assertTrue(memInfo[2] > 0, "Used memory should be positive");

		long sum = memInfo[1] + memInfo[2];
		assertTrue(Math.abs(sum - memInfo[0]) < memInfo[0] * 0.1, "Available mem + Used mem should be almost equal to Total mem");

		System.out.println("✔ Memory test passed - Total: "+memInfo[0] + " MB, Available: " + memInfo[1]+ " MB, Used: " + memInfo[2] + " MB");
	}

	@Test
	public void testGetUptime() throws Exception{
		String uptime = service.getUptime();

		assertNotNull(uptime, "Uptime should not be null");
		assertFalse(uptime.isEmpty(), "Uptime should not be empty");

		assertTrue(uptime.contains("hours"), "Uptime should contain 'hours'");
		assertTrue(uptime.contains("minutes"), "Uptime should contain 'minutes'");

		System.out.println("✔ Uptime test passed: " + uptime);
	}

	@Test
	public void testGetSystemStats() throws Exception {
		SystemStats stats = service.getSystemStats();

		assertNotNull(stats, "SystemStats should not be null");

		assertTrue(stats.getCpuTemperature() > 20.0, "CPU temp should be reasonable");
		assertTrue(stats.getTotalMemoryMB() > 512, "Total memory should be reasonable");
		assertTrue(stats.getFreeMemoryMB() >= 0, "Free memory should be non-negative");
		assertTrue(stats.getMemoryUsagePercent() >= 0 && stats.getMemoryUsagePercent() <=100, "Memory should be between 0-100");
		assertNotNull(stats.getUptime(), "Uptime should not be null");

		assertTrue(stats.getTotalDiskGB() > 4, "Total disk should be reasonable");
		assertTrue(stats.getFreeDiskGB() > 0, "Free disk should be positive");
		assertTrue(stats.getDiskUsagePercent() >= 0 && stats.getDiskUsagePercent() <= 100 , "Disk usage should be between 0-100%");

		System.out.println("✔ Full SystemStats test passed!");
	}

	@Test
	public void testGetDiskSpace() throws Exception {
		long[] diskInfo = service.getDiskSpace();

		//diskInfo has 3 elements, diskInfo[0] = total GB, diskInfo[1] = available GB, diskInfo[2] = used GB
		assertEquals(3, diskInfo.length, "Should return array of 3 elements");

		assertTrue(diskInfo[0] > 4, "Total disk space should be at least 4 GB");
		assertTrue(diskInfo[1] > 0, "Available disk space should be positive");
		assertTrue(diskInfo[2] > 0, "Used disk space should be positive");
		assertTrue(diskInfo[2] < diskInfo[0], "Used disk space should be less than total disk space");

		System.out.println("✔ Disk space test passed - Total: " + diskInfo[0] + " GB, Available: " + diskInfo[1] + " GB, Used: " + diskInfo[2] + " GB");
	}
}

