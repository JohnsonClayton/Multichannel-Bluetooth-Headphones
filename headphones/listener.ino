#include <Arduino.h>
#include <WiFi.h>
#include <driver/dac.h>


const int  buttonPin = 26;    // the pin that the pushbutton is attached to
//const int ledPin = 2;       // the pin that the LED is attached to

// Variables will change:
int buttonPushCounter = 0;   // counter for the number of button presses
int buttonState = 0;         // current state of the button
int lastButtonState = 0;    

const char* ssid     = "claytonsnetwork";
const char* password = "FFDFBFF3FFEFF237F80B598902";
const char* host     = "192.168.0.36"; 

WiFiClient client;

hw_timer_t * timer = NULL; 
portMUX_TYPE timerMux = portMUX_INITIALIZER_UNLOCKED; 

#define BUFFFERMAX 8000

uint8_t dataBuffer[BUFFFERMAX];
int readPointer = 0, writePointer = 1;

bool play = false;

void IRAM_ATTR onTimer() {
  portENTER_CRITICAL_ISR(&timerMux);
  
  // play data: 
  if (play) {
    dac_output_voltage(DAC_CHANNEL_1, dataBuffer[readPointer]);

    readPointer++;
    if (readPointer == BUFFFERMAX) {
      readPointer = 0;
    }

    if ( getAbstand() == 0 ) {
      Serial.println("Buffer underrun!!!");
      play = false;
    }
  }


  portEXIT_CRITICAL_ISR(&timerMux);
}

int getAbstand() {
  int abstand = 0;
  if (readPointer < writePointer ) abstand =  BUFFFERMAX - writePointer + readPointer;
  else if (readPointer > writePointer ) abstand = readPointer - writePointer;
  return abstand;
}

int port = 0;

void setup() {
  Serial.begin(115200);

  dac_output_enable(DAC_CHANNEL_1);
  pinMode(33, INPUT_PULLUP);
  pinMode(32, INPUT_PULLUP);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());


  port = 4444;
  while (!client.connect(host, port)) {
    Serial.println("connection failed");
    delay(1000);
  }

  timer = timerBegin(0, 2, true); // use a prescaler of 2
  timerAttachInterrupt(timer, &onTimer, true);
  timerAlarmWrite(timer, 5000, true);
  timerAlarmEnable(timer);


pinMode(buttonPin, INPUT_PULLDOWN);
  // initialize the LED as an output:
 // pinMode(ledPin, OUTPUT);

   // pinMode(32, INPUT_PULLUP);

  
}

void loop() {
  int abstand = getAbstand();
  if (abstand <= 800) play = true;

  if ( abstand >= 800) {
    client.write( B11111111 ); // send the command to send new data
    
    // read new data: 
    while (client.available() == 0);
    while (client.available() >= 1) {
      uint8_t value = client.read();
      dataBuffer[writePointer] = value;
      writePointer++;
      if (writePointer == BUFFFERMAX) writePointer = 0;
    }

  }

  buttonState = digitalRead(buttonPin);
  if (buttonState == HIGH && port == 4444) {
    // Change port to 4445
    port = 4445;
    while (!client.connect(host, port)) {
      Serial.println("connection failed");
      delay(1000);
    }
  } else if (buttonState == LOW && port == 4445) {
    // Change port to 4444
    port = 4444;
    while (!client.connect(host, port)) {
      Serial.println("connection failed");
      delay(1000);
    }
  }
  // just added from https://www.arduino.cc/en/Tutorial/StateChangeDetection
  // read the pushbutton input pin:
  /*buttonState = digitalRead(buttonPin);

  // compare the buttonState to its previous state
  if (buttonState != lastButtonState) {
    // if the state has changed, increment the counter
    //if (buttonState == LOW) {
      // if the current state is HIGH then the button went from off to on:
      port = 4445;
      while (!client.connect(host, port)) {
    Serial.println("connection failed");
    delay(1000);
  }
      buttonPushCounter++;
      Serial.println("on");
      Serial.print("number of button pushes: ");
      Serial.println(buttonPushCounter);
   // } else {
      // if the current state is LOW then the button went from on to off:
    //  Serial.println("off");
   // }
    // Delay a little bit to avoid bouncing
    delay(50);
  }

  // save the current state as the last state, for next time through the loop
  lastButtonState = buttonState;


  // turns on the LED every four button pushes by checking the modulo of the
  // button push counter. the modulo function gives you the remainder of the
  // division of two numbers:
  //if (buttonPushCounter % 4 == 0) {
  //  digitalWrite(ledPin, HIGH);
  //} else {
  //  digitalWrite(ledPin, LOW);
  //}*/



}
