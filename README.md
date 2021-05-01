# CPS498-S21-Team04
CPS 498 2021 Spring Repository for the IoT team

# Table of Contents

* [Introduction](#DESCRIPTION)

* [Objectives & Functionality](###Objectives-&-Functionality)

* [Snapshots](#Snapshots)
  * [Home Page](#Home-Page)
  * [Dashboard](#)
## Title: Guitar Looper


### DESCRIPTION:
The Guitar Looper is a product described as being an effects looper manager, where it takes inputs and outputs of various effect pedals, and then is able to switch using or not using different effects for a smaller set of buttons to use presets. These presets are desired to be configurable using a mobile app for an android phone, that can connect to the looper via bluetooth. This app ideally will have a mechanism to store currently unused presets, so that they can be switched out more efficiently.

### Objectives & Functionality

### Goals for Mobile App:<br/>
      1. Create a user friendly mobile app.<br/>
      2. Be able to easily add features to app.<br/>
      3. Be able to have multiple presets.<br/>
      4. Have a flawless connection from phone to device through Bluetooth.<br/>
      5. Have application work as an effect switcher manager.<br/>

### Goals for Device:
      1. Get ESP32 to configure and save settings based on bluetooth data received.
      2. Handle button-presses as event-interrupts.
      3. Settings need to be saved to EEPROM
      4. Hardware will be easily upgraded/changed/swapped out over time.
      
| Names           | Title         |
| -------------   | ------------- |
| Dominic Kenney  | Team Lead     |
| Alex Gennero    | Team Member   |
| Mitchell Murphy | Team Member   |
| Robert Wang     | Advisor       |

![Home Page](./Images/Home Screen.png)


### Installation Instructions: 
      1. Load and run ESP_Testing branch in Visual Studio, then compile to a plugged in ESP32 via USB. 
      2. Sideload app from MobileAppDevelopment for use on android device 
      3. Connect to device via App when both are powered. 
      4. Configure presets to upload to device.
