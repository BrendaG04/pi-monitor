# Raspberry Pi System Monitor 

A full stack real time system monitoring dashboard for Raspberry Pi, featuring a Java Spring Boot backend and React frontend.

![Project Status](https://img.shields.io/badge/status-deployed-success)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![React](https://img.shields.io/badge/React-19-blue)

## Overview

This project monitors Raspberry Pi hardware metrics in real time, including:
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
- **JUnit 5** - Testing framework (8 tests, 100% coverage)

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

### Prerequisites
- Raspberry Pi (any model with Raspberry Pi OS)
- Java 17
- Node.js 20+
- Maven
- nginx

### Setup

1. **Clone the repository:**
```bash
git clone 
cd pi-monitor
```

2. **Build backend:**
```bash
mvn clean package
```

3. **Build frontend:**
```bash
cd ../pi-monitor-frontend
npm install
npm run build
```

4. **Deploy:**
```bash
chmod +x ~/deploy-pi-monitor.sh
~/deploy-pi-monitor.sh
```

5. **Access the dashboard:**
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
│   ├── main/java/com/brenda/pimonitor/
│   │   ├── PiMonitorApplication.java      # Main entry point
│   │   ├── SystemController.java          # REST controller
│   │   ├── SystemMonitorService.java      # Business logic
│   │   ├── SystemStats.java               # Data model
│   │   └── CorsConfig.java                # CORS configuration
│   └── test/java/com/brenda/pimonitor/
│       ├── SystemMonitorServiceTest.java  # Unit tests
│       └── SystemControllerTest.java      # Integration tests
├── pom.xml                                # Maven configuration
└── README.md

pi-monitor-frontend/
├── src/
│   ├── components/
│   │   ├── StatCard.jsx                   # Reusable stat card
│   │   ├── ConnectionStatus.jsx           # Connection indicator
│   │   └── TemperatureChart.jsx           # Chart component
│   ├── App.js                             # Main React component
│   └── App.css                            # Styles
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

### Service Management
```bash
# Check status
sudo systemctl status pi-monitor-backend

# View logs
tail -f /opt/pi-monitor/logs/backend.log

# Restart
sudo systemctl restart pi-monitor-backend
sudo systemctl restart nginx
```

## Future Enhancements

- [ ] Authentication (JWT)
- [ ] HTTPS/SSL
- [ ] Historical data storage
- [ ] Email alerts for critical metrics
- [ ] Network traffic monitoring
- [ ] Process list viewer
- [ ] Dark/light theme toggle

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
