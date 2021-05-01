# CPS498-S21-Team04
## Guitar Synth Manager



# Table of Contents
* [Title](#CPS498-S21-Team04)
* [Introduction](#Introduction)

* [Objectives & Functionality](#Objectives-&-Functionality)

* [Snapshots](#Snapshots)
  * Home Page
  * Dashboard
  * Editing Page
  * Prototype Housing
  * Prototype Schematic
* [Changes Needed to Design](#Changes-Needed-to-Design)
* [Team Contributions](#Team-Contributions)
* [Installation Guides](#Installation-Guides)
  * [Installing App to Android Device](#Installing-App-to-Android-Device)
  * [Uploading code to ESP-32](#Uploading-Code-to-ESP-32)



## Introduction:


## Objectives & Functionality

## Snapshots
![Home Page](./Images/HomeScreen.png)
![Dashboard](./Images/Dashboard.png)
![Edit Page](./Images/EditingPage.png)
![Protoype Housing](./Images/HousingPrototype.jpg)
![Prototype Schematic](./Images/Schematic.png)
## Team Contributions
* Dominic Kenney
    * 150 Hours Worked
    * Worked on:
        * Hardware Schematic and Layout
        * Hardware Construction
        * Microcontroller Implementation
        * Microcontroller coding
            * Built Proof of Concept code for BT and hardware to test separately.
* Alex Generro
    * 80 Hours Worked
        * Worked on:
            * Designed format of phone application
                * Worked with advisor to design app to be intuitive to musicians and non tech-savvy users
            * App navigation
                * Bottom Nav Bar
                * Editing Screen Popup Menu
            * Designed Room Database (Database Layer on an SQLite database)
                * String value for Preset Name
                * Boolean values for each channel
                * Byte Array for storing home page image
                    * Image-to-URI to Byte Array
* Mitchell Murphy
    * 70 Hours Worked
    * Worked on:
        * Implemented Bluetooth functions for Mobile app
        * Implemented customizable home screen background.


## Installation Guides
### Installing App to Android Device
1. Ensure device has sidloading applications enabled
      1. Check in the Settings > Security > Unknown Sources checkbox.
1. Download the `.apk` file [here]() to android device
1. Open downloads folder and tap on `.apk` file
      1. Tap yes when prompted to install application
### Uploading code to ESP-32
1. Enter the `Microcontroller Code` folder
1. choose which code you want to download by entering the corresponding sub-folder
1. Download the sub-folder as a `.zip` file
1. Extract `.zip` and enter the folder.
1. Open the `.ino` file in either Arduino IDE or Visual Studio Code
1. Select the corresponding ESP-32 board unit based on your hardware
      1. If not installed, please follow guide [here]() to get the ESP-32 board manager installed for Arduino
1. Select the port the ESP-32 is connected to computer as
      1. `COM##` on Windows
      1. `/dev/cu.####` on Mac
      1. `/tty##` on Linux
1. Upload Code
      1. ESP-32's Boot button will need to be pressed when Code is being uploaded, in order for the connection to be made.
