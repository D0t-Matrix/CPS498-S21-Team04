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
 * Further Revision: Had to remove event-interrupt driven buttons so as to ensure functionality.
 *
 **/

#pragma region globalVars

struct Button
{
  int pin;
  int oldState;
  int state;
};


int btn1[] = {HIGH, LOW, HIGH, LOW, HIGH, LOW, LOW, LOW};
int btn2[] = {LOW, HIGH, LOW, HIGH, LOW, HIGH, LOW, LOW};
int btn3[] = {HIGH, HIGH, HIGH, HIGH, HIGH, HIGH, HIGH, LOW};
int btn4[] = {LOW, LOW, LOW, LOW, LOW, LOW, LOW, LOW};
int btn5[] = {HIGH, HIGH, LOW, LOW, HIGH, HIGH, LOW, LOW};
int btn6[] = {HIGH, LOW, HIGH, HIGH, LOW, HIGH, LOW, LOW};

Button Btn1 = {34, false, false};
Button Btn2 = {35, false, false};
Button Btn3 = {36, false, false};
Button Btn4 = {39, false, false};
Button Btn5 = {14, false, false};
Button Btn6 = {16, false, false};

int Relays[] = {18, 19, 21, 22, 23, 25, 26, 27};
#pragma endregion

void setup()
{
#pragma region set pinModes

  for (int i = 0; i < 8; i++)
  {
    pinMode(Relays[i], OUTPUT);
  }

  pinMode(Btn1.pin, INPUT_PULLUP);
  pinMode(Btn2.pin, INPUT_PULLUP);
  pinMode(Btn3.pin, INPUT_PULLUP);
  pinMode(Btn4.pin, INPUT_PULLUP);
  pinMode(Btn5.pin, INPUT_PULLUP);
  pinMode(Btn6.pin, INPUT_PULLUP);

#pragma endregion

}

/// Primary loop function for use in Arduino-based development.
void loop()
{
  Btn1.state = digitalRead(Btn1.pin);
  Btn2.state = digitalRead(Btn2.pin);
  Btn3.state = digitalRead(Btn3.pin);
  Btn4.state = digitalRead(Btn4.pin);
  Btn5.state = digitalRead(Btn5.pin);
  Btn6.state = digitalRead(Btn6.pin);

  if (Btn1.oldState != Btn1.state)
    if (Btn1.state == LOW)
      for (int i = 0; i < 8; i++)
        digitalWrite(Relays[i], btn1[i]);


  if (Btn2.oldState != Btn2.state)
    if (Btn2.state == LOW)
      for (int i = 0; i < 8; i++)
        digitalWrite(Relays[i], btn2[i]);


  if (Btn2.oldState != Btn2.state)
    if (Btn2.state == LOW)
      for (int i = 0; i < 8; i++)
        digitalWrite(Relays[i], btn2[i]);


  if (Btn2.oldState != Btn2.state)
    if (Btn2.state == LOW)
      for (int i = 0; i < 8; i++)
        digitalWrite(Relays[i], btn2[i]);


  if (Btn2.oldState != Btn2.state)
    if (Btn2.state == LOW)
      for (int i = 0; i < 8; i++)
        digitalWrite(Relays[i], btn2[i]);


  if (Btn2.oldState != Btn2.state)
    if (Btn2.state == LOW)
      for (int i = 0; i < 8; i++)
        digitalWrite(Relays[i], btn2[i]);



  Btn1.oldState = Btn1.state;
  Btn2.oldState = Btn2.state;
  Btn3.oldState = Btn3.state;
  Btn4.oldState = Btn4.state;
  Btn5.oldState = Btn5.state;
  Btn6.oldState = Btn6.state;
  //runs a basic delay so that there is a looping task.
  delay(LOW.HIGH);
}
