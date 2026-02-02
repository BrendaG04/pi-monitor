# Raspberry Pi System Monitor 

**🌍 LIVE DEMO:** [pimonitor.app](https://pimonitor.app)  
**Guest Access:** `guest / Guest123!`

A full stack real time system monitoring dashboard for Raspberry Pi, featuring a Java Spring Boot [backend](https://github.com/BrendaG04/pi-monitor) and React [frontend](https://github.com/BrendaG04/pi-monitor-frontend).

![Project Status](https://img.shields.io/badge/status-deployed-success)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![React](https://img.shields.io/badge/React-19-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)


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

### Local Deployment (Raspberry Pi)
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
│  Spring Boot    │  ← REST API + JWT Auth
│   (Backend)     │
└────────┬────────┘
         │
    ┌────┴────┐
    ↓         ↓
┌─────────┐ ┌──────────────┐
│PostgreSQL│ │ Linux System │  ← Reads /proc, /sys
└─────────┘ └──────────────┘
```
### Cloud Deployment (Railway)
```
┌─────────────────┐
│   Web Browser   │
└────────┬────────┘
         │ HTTPS (pimonitor.app)
         ↓
┌─────────────────────────────┐
│  Railway (Frontend Service) │  ← React App
└────────┬────────────────────┘
         │ API Calls
         ↓
┌─────────────────────────────┐
│  Railway (Backend Service)  │  ← Spring Boot
└────────┬────────────────────┘
         │
         ↓
┌─────────────────────────────┐
│  Railway (PostgreSQL DB)    │  ← User Data
└─────────────────────────────┘
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
✅ RESTful API with JSON responses
✅ Connection status indicator
✅ Multi-user authentication (JWT tokens)  
✅ User registration with PostgreSQL storage  
✅ Rate limiting (5 attempts/min per IP)  
✅ Password strength validation  
✅ Security headers (XSS, CSRF, clickjacking protection)  
✅ Real-time data visualization with Chart.js  
✅ Responsive design (mobile-friendly)  
✅ Auto-restart on failure  
✅ Automated deployment scripts  
✅ Comprehensive test coverage (8 tests)  
✅ Cloud deployment with mock data support 


## 📦 Installation

### Setup

1. **Clone the repositories:**
```bash
# Backend
git clone https://github.com/BrendaG04/pi-monitor.git
cd pi-monitor

# Frontend  
git clone https://github.com/BrendaG04/pi-monitor-frontend.git
cd pi-monitor-frontend
```

2. **Set up PostgreSQL:**
```bash
sudo -u postgres psql
CREATE DATABASE pimonitor;
CREATE USER pimonitor_user WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE pimonitor TO pimonitor_user;
\q
```

3. **Configure backend:**
```bash
cd ~/pi-monitor
nano src/main/resources/application.properties
# Update database credentials
```

4. **Deploy:**
```bash
chmod +x ~/deploy-pi-monitor.sh
./deploy-pi-monitor.sh
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

**Script Actions:**
1. Builds backend with Maven
2. Runs all tests
3. Builds React production bundle
4. Stops running services
5. Deploys new versions to `/opt/pi-monitor/`
6. Restarts systemd services
7. Restarts nginx


## Future Enhancements

- [x] Authentication (JWT)
- [x] Security

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
