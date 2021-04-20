/*
 Name:		ESP32_BLE_test.ino
 Created:	3/19/2021 10:40:22 AM
 Author:	dominic Kenney
*/

#include <string>
#include <Wire.h>
#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>
#include <BLEUUID.h>
#include <EEPROM.h>
#include <BLE2902.h>
using namespace std;


#define BT_NAME				"ESP32_EEPROM_Test"
#define SERVICE_UUID        "d67f8bf2-88bf-11eb-8dcd-0242ac130003"
#define CHARACTERISTIC_UUID "5c9219c4-88c2-4688-b7f5-ea07361b26a8"
#define DESCRIPTER_UUID     "5c9f59c2-88c2-31be-5dad-02f481048201"

bool deviceConnected = false;
BLECharacteristic* characteristicTX;

uint8_t val = 0;

void setup() {
    Serial.begin(115200);

    Serial.println("1- Download and install an BLE scanner app in your phone");
    Serial.println("2- Scan for BLE devices in the app");
    Serial.println("3- Connect to MyESP32");
    Serial.println("4- Go to CUSTOM CHARACTERISTIC in CUSTOM SERVICE and write something");
    Serial.println("5- See the magic =)");

    BLEDevice::init(BT_NAME);

    BLEServer* server = BLEDevice::createServer();
    server->setCallbacks(new BLEServerCallbacks());

    BLEService* service = server->createService(SERVICE_UUID);

    characteristicTX = service->createCharacteristic(
        CHARACTERISTIC_UUID,
        BLECharacteristic::PROPERTY_NOTIFY | BLECharacteristic::PROPERTY_WRITE | BLECharacteristic::PROPERTY_READ
    );

    characteristicTX->addDescriptor(new BLE2902());

    characteristicTX->setCallbacks(new testCallbacks());
}

class ServerCallbacks : public BLEServerCallbacks {
    void onConnect(BLEServer* pServer)
    {
        deviceConnected = true;
    }

    void onDisconnect(BLEServer* pServer)
    {
        deviceConnected = false;
    }
};

class testCallbacks : public BLECharacteristicCallbacks
{
    void onWrite(BLECharacteristic* characteristic)
    {
        string rxValue = characteristic->getValue();

        if (rxValue.length() > 0)
        {
            for (char c : rxValue)
                Serial.print(c);
            Serial.println();

            size_t found = rxValue.find("set: ");
            if (found != string::npos)
            {
                val = rxValue[found + 1];
                EEPROM.write(0, val);
                Serial.printf("EEPROM Val: %d\n", EEPROM.read(0));
            }

            found = rxValue.find("version");
            if (found != string::npos)
            {
                
            }
        }
    }

    void onRead(BLECharacteristic* characteristic)
    {

    }
};

void loop()
{
    delay(20);
}