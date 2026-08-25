# Swapnil Security Lab

Android cybersecurity learning lab for ARM64 devices.

## Version

**1.0.0**

## Design goals

- Rootless-first Android app
- ARM64 / arm64-v8a target
- Isolated Debian + QEMU architecture
- Verified security components with SHA-256
- Safe lab workflow for systems the user owns or is authorized to test
- GitHub Actions APK build

## Current release

v1.0.0 establishes the Android application shell and rootless-engine readiness checks. The next milestone adds the verified component downloader, local engine installation, QEMU lifecycle management, terminal, and controlled lab tools.

The rootless component URLs and checksums are based on the public StrykerOSS v6 rootless manifest. StrykerOSS is GPLv3; this project does not bundle its binaries in the repository. Components are fetched only when the user chooses to install the rootless environment.

## Build

Use the GitHub Actions workflow or run Gradle 8.10 with JDK 17.

## Safety

Use scanning, enumeration, exploitation research, and other security capabilities only against systems you own or have explicit permission to test.
