#include "BLEUtils.h"
#include "BLEServer.h"
#include <esp_log.h>
#include <string>

#define DEVICE_NAME "ESP_TEST"
#define SERVICE_UUID "72706a48-b435-47d0-b15b-b6f3181e4765"
#define CHARACTERISTIC_UUID "60355f8a-5626-4296-861d-2b11a025f2f9"

static void run()
{
    BLE::initServer(DEVICE_NAME);

    BLEServer *pServer = new BLEServer();
    BLEService *pService = pServer->createService(BLEUUID(SERVICE_UUID));

    BLECharacteristic *pCharacteristic = pService->createCharacteristic(
        BLEUUID(CHARACTERISTIC_UUID),
        BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_WRITE
    );

    pCharacteristic->setValue("Hello World from Dot");

    pService->start();

    BLEAdvertising *pAdvertising = pServer->getAdvertising()
    pAdvertising->start();
}