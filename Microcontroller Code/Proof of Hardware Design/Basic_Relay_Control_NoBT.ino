/**
 * File Name: Basic_Relay_Control_NoBT.ino
 *
 * Author: Dominic Kenney
 *
 * Purpose: Have test-code to have a baseline to test functionality of the hardware.
 *
 * Notes: This file is a basic project for use in testing the functionality of the physical
 *  Switcher, without having dependency on a bluetooth connection.
 *
 * Revisions: Set all stored values to have channel 8 stay off,
 *  due to lacking surface area for the 8th channel.
 *
 **/

#pragma region globalVars

bool btn1[] = {1, 0, 1, 0, 1, 0, 0, 0};
bool btn2[] = {0, 1, 0, 1, 0, 1, 0, 0};
bool btn3[] = {1, 1, 1, 1, 1, 1, 1, 0};
bool btn4[] = {0, 0, 0, 0, 0, 0, 0, 0};
bool btn5[] = {1, 1, 0, 0, 1, 1, 0, 0};
bool btn6[] = {1, 0, 1, 1, 0, 1, 0, 0};

int Btn1 = 34;
int Btn2 = 35;
int Btn3 = 36;
int Btn4 = 39;
int Btn5 = 14;
int Btn6 = 16;

int Relays[] = {18, 19, 21, 22, 23, 25, 26, 27};
#pragma endregion

#pragma region BTN Interrupts

void IRAM_ATTR Btn1Pushed()
{
  for (int i = 0; i < 8; i++)
    digitalWrite(Relays[i], btn1[i]);
}

void IRAM_ATTR Btn2Pushed()
{
  for (int i = 0; i < 8; i++)
    digitalWrite(Relays[i], btn2[i]);
}

void IRAM_ATTR Btn3Pushed()
{
  for (int i = 0; i < 8; i++)
    digitalWrite(Relays[i], btn3[i]);
}

void IRAM_ATTR Btn4Pushed()
{
  for (int i = 0; i < 8; i++)
    digitalWrite(Relays[i], btn4[i]);
}

void IRAM_ATTR Btn5Pushed()
{
  for (int i = 0; i < 8; i++)
    digitalWrite(Relays[i], btn5[i]);
}

void IRAM_ATTR Btn6Pushed()
{
  for (int i = 0; i < 8; i++)
    digitalWrite(Relays[i], btn6[i]);
}

#pragma endregion

void setup()
{
#pragma region set pinModes

  for (int i = 0; i < 8; i++)
  {
    pinMode(Relays[i], OUTPUT);
  }

  pinMode(Btn1, INPUT_PULLUP);
  pinMode(Btn2, INPUT_PULLUP);
  pinMode(Btn3, INPUT_PULLUP);
  pinMode(Btn4, INPUT_PULLUP);
  pinMode(Btn5, INPUT_PULLUP);
  pinMode(Btn6, INPUT_PULLUP);

#pragma endregion

#pragma region Creating event - Interrupts for buttons

  attachInterrupt(Btn1, Btn1Pushed, FALLING);
  attachInterrupt(Btn2, Btn2Pushed, FALLING);
  attachInterrupt(Btn3, Btn3Pushed, FALLING);
  attachInterrupt(Btn4, Btn4Pushed, FALLING);
  attachInterrupt(Btn5, Btn5Pushed, FALLING);
  attachInterrupt(Btn6, Btn6Pushed, FALLING);

#pragma endregion
}

/// Primary loop function for use in Arduino-based development.
void loop()
{
  //runs a basic delay so that there is a looping task.
  delay(0.1);
}
