/**
 * File Name: Proof_Of_Bluetooth_Connectivity.ino
 *
 * Author: Dominic Kenney
 *
 * Purpose: Have a basic BT connection to run against for mobile app development.
 *
 * Notes: This file is a basic project for use in testing the functionality of the mobile app's
 *  Bluetooth connection functionality.
 *
 * Revisions: sends direct feedback to connected device for use in confirming design.
 *
 **/

#include "BluetoothSerial.h"

BluetoothSerial SerialBT;

String MACadd = "AA:BB:CC:11:22:33";
uint8_t address[6] = {0xAA, 0xBB, 0xCC, 0x11, 0x22, 0x33};
//uint8_t address[6]  = {0x00, 0x1D, 0xA5, 0x02, 0xC3, 0x22};
String name = "OBDII";
char *pin = "1234"; //<- standard pin would be provided by default
bool connected;

void setup()
{
  Serial.begin(115200);
  //SerialBT.setPin(pin);
  SerialBT.begin("ESP32test", true);
  //SerialBT.setPin(pin);
  Serial.println("The device started in master mode, make sure remote BT device is on!");

  // connect(address) is fast (upto 10 secs max), connect(name) is slow (upto 30 secs max) as it needs
  // to resolve name to address first, but it allows to connect to different devices with the same name.
  // Set CoreDebugLevel to Info to view devices bluetooth address and device names
  connected = SerialBT.connect(name);
  //connected = SerialBT.connect(address);

  if (connected)
  {
    Serial.println("Connected Succesfully!");
  }
  else
  {
    while (!SerialBT.connected(10000))
    {
      Serial.println("Failed to connect. Make sure remote device is available and in range, then restart app.");
    }
  }
  // disconnect() may take upto 10 secs max
  if (SerialBT.disconnect())
  {
    Serial.println("Disconnected Succesfully!");
  }
  // this would reconnect to the name(will use address, if resolved) or address used with connect(name/address).
  SerialBT.connect();
}

//Checks the message for the values of the given buttons.
void findBtnVal(String receivedMsg)
{
  //For each button identified
  for (int i = 0; i < 6; i++)
  {
    String subStr = String((char)(i + 1) - '0') + ":";

    //find index this substring starts at, and set the capture index to the character after the substring
    int strIndex = receivedMsg.indexOf(subStr) + 2;

    // if substring was found
    if (strIndex > 0)
    {
      //store chracter as raw byte to memory, return value to bluetooth connection as confirmation
      byte val = receivedMsg.charAt(strIndex);
      EEPROM.write(i, val)
      SerialBT.write("Btn %d Stored", (i + 1));
    }
  }
}

void loop()
{
  //If Bluetooth Serial connection is established, check for read message
  if (SerialBT.available())
  {
    String receivedMsg = SerialBT.readString();
    findBtnVal(receivedMsg);
  }

  //delay for use in progressing time
  delay(20);
}
