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



struct Button
{
  int pin;
  int oldState;
  int state;
  int relayStates[8];
};

Button Btn1 = {34, false, false, {HIGH, LOW, HIGH, LOW, HIGH, LOW, LOW, LOW}};
Button Btn2 = {35, false, false, {LOW, HIGH, LOW, HIGH, LOW, HIGH, LOW, LOW}};
Button Btn3 = {36, false, false, {HIGH, HIGH, HIGH, HIGH, HIGH, HIGH, HIGH, LOW}};
Button Btn4 = {39, false, false, {LOW, LOW, LOW, LOW, LOW, LOW, LOW, LOW}};
Button Btn5 = {14, false, false, {HIGH, HIGH, LOW, LOW, HIGH, HIGH, LOW, LOW}};
Button Btn6 = {16, false, false, {HIGH, LOW, HIGH, HIGH, LOW, HIGH, LOW, LOW}};

int Relays[] = {18, 19, 21, 22, 23, 25, 26, 27};

void setup()
{

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


}

/// Primary loop function for use in Arduino-based development.
void loop()
{

  // Read States of each button for this loop
  Btn1.state = digitalRead(Btn1.pin);
  Btn2.state = digitalRead(Btn2.pin);
  Btn3.state = digitalRead(Btn3.pin);
  Btn4.state = digitalRead(Btn4.pin);
  Btn5.state = digitalRead(Btn5.pin);
  Btn6.state = digitalRead(Btn6.pin);


  // Update relay outputs based on new state information
  if (Btn1.oldState != Btn1.state && Btn1.state == LOW)
    for (int i = 0; i < 8; i++)
      digitalWrite(Relays[i], Btn1.relayStates[i]);

  if (Btn2.oldState != Btn2.state && Btn2.state == LOW)
    for (int i = 0; i < 8; i++)
      digitalWrite(Relays[i], Btn2.relayStates[i]);

  if (Btn3.oldState != Btn3.state && Btn3.state == LOW)
    for (int i = 0; i < 8; i++)
      digitalWrite(Relays[i], Btn3.relayStates[i]);

  if (Btn4.oldState != Btn4.state && Btn4.state == LOW)
    for (int i = 0; i < 8; i++)
      digitalWrite(Relays[i], Btn4.relayStates[i]);

  if (Btn5.oldState != Btn5.state && Btn5.state == LOW)
    for (int i = 0; i < 8; i++)
      digitalWrite(Relays[i], Btn5.relayStates[i]);

  if (Btn6.oldState != Btn6.state && Btn6.state == LOW)
    for (int i = 0; i < 8; i++)
      digitalWrite(Relays[i], Btn6.relayStates[i]);



  // Set stored values of the state to the previous state record.
  Btn1.oldState = Btn1.state;
  Btn2.oldState = Btn2.state;
  Btn3.oldState = Btn3.state;
  Btn4.oldState = Btn4.state;
  Btn5.oldState = Btn5.state;
  Btn6.oldState = Btn6.state;

  //runs a basic delay for sake of microcontroller processing flow.
  delay(1);
}
