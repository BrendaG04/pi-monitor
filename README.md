# Raspberry Pi System Monitor 

A full stack real time system monitoring dashboard for Raspberry Pi, featuring a Java Spring Boot [backend](https://github.com/BrendaG04/pi-monitor) and React [frontend](https://github.com/BrendaG04/pi-monitor-frontend).

![Project Status](https://img.shields.io/badge/status-deployed-success)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![React](https://img.shields.io/badge/React-19-blue)

<img width="1464" height="864" alt="image" src="https://github.com/user-attachments/assets/e04e7f8e-5e29-42ab-abc7-cb90ee15b9ba" />
<img width="350" height="600" alt="image" src="https://github.com/user-attachments/assets/71f3265b-1e4d-4a84-8fe4-0d5c3aa0a57a" />
<img width="350" height="600" alt="image" src="https://github.com/user-attachments/assets/8c398609-44b7-4f38-8f34-aee3592c8808" />

## Overview

This project monitors Raspberry Pi hardware metrics in near real-time (2second polling), including:
- CPU Temperature
- Memory Usage (Total, Used, Free, %)
- Disk Space (Total, Used, Free, %)
- System Uptime
- Live Temperature history chart

## Architecture
```
┌─────────────────┐
│   Web Browser   │
└────────┬────────┘
         │ HTTP (Port 80)
         ↓
┌─────────────────┐
│     nginx       │  ← Serves React, Proxies API
└────────┬────────┘
         │ /api/* → :8080
         ↓
┌─────────────────┐
│  Spring Boot    │  ← REST API
│   (Backend)     │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  Linux System   │  ← Reads /proc, /sys
│    (/proc/*)    │
└─────────────────┘
```

## Tech Stack

### Backend
- **Java 21** - Core language
- **Spring Boot 3.2** - REST API framework
- **Maven** - Build tool
- **JUnit 5** - Testing framework (8 tests, Full coverage of core services and controllers)

### Frontend
- **React 19** - UI framework
- **Chart.js** - Data visualization
- **CSS3** - Styling (glassmorphism design)

### Deployment
- **systemd** - Service management
- **nginx** - Web server & reverse proxy
- **Bash** - Deployment automation


## Features
- ✅ Real-time monitoring (2-second refresh rate)
- ✅ RESTful API with JSON responses
- ✅ Responsive design (mobile-friendly)
- ✅ Auto-restart on failure
- ✅ Boots automatically on system start
- ✅ Connection status indicator
- ✅ Temperature history chart (60 seconds)
- ✅ Clean, modern UI
- ✅ Comprehensive test coverage



## 📦 Installation

### Setup

1. **Clone the repository:**
```bash
git clone 
cd pi-monitor
```

2. **Builds & Deployment:**
```bash
chmod +x ~/deploy-pi-monitor.sh
~/deploy-pi-monitor.sh
```

3. **Access the dashboard:**
```
http://your-pi-ip
```



## 🧪 Testing
Run backend tests:
```bash
cd ~/pi-monitor
mvn test
```

Test coverage:
- Unit tests: `SystemMonitorServiceTest` (5 tests)
- Integration tests: `SystemControllerTest` (3 tests)
- Total: 8 tests

## 📡 API Documentation

### GET /stats
Returns current system statistics.

**Response:**
```json
{
  "cpuTemperature": 45.3,
  "totalMemoryMB": 3796,
  "freeMemoryMB": 2100,
  "usedMemoryMB": 1696,
  "memoryUsagePercent": 44.68,
  "totalDiskGB": 29,
  "freeDiskGB": 20,
  "usedDiskGB": 8,
  "diskUsagePercent": 27.59,
  "uptime": "6 hours, 42 minutes"
}
```

## 🔧 Project Structure
```
pi-monitor/
├── src/
│   ├── main/java/...              # Springboot backend
│   └── test/java/...              # Unit & Integration tests
├── pom.xml                                
└── README.md

pi-monitor-frontend/
├── src/                           # React Components
└── package.json
```

## 🔄 Deployment

The project includes automated deployment:
```bash
~/deploy-pi-monitor.sh
```

This script:
1. Builds backend (Maven)
2. Runs tests
3. Builds frontend (React)
4. Stops services
5. Deploys new versions
6. Restarts services


## Future Enhancements

- [ ] Authentication (JWT)
- [ ] HTTPS/SSL

## What I Learned

- Full-stack development (Java + React)
- RESTful API design
- Test-Driven Development (TDD)
- Linux system administration
- systemd service management
- nginx configuration
- Bash scripting
- Deployment automation
- Real-time data visualization

## 👤 Author

**Brenda** 

## 📝 License

This project is for educational purposes.
