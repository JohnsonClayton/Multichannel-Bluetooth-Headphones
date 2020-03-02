# Multichannel-Bluetooth-Headphones
Server and Arduino code for the Networks course

## Server
### Build
Execute the commands below or use the `build.sh` file to build the `jar` file.  
```
$ javac -d . server/VoiceServer.java server/Helper.java server/ClientHandler.java
$ jar cvmf server/MANIFEST.MF server.jar server/voice/server/*
```
### Run
```
$ java -jar server.jar
```

### Script
I've made a script that automatically kicks off each server into its own process. Log files are created for each server and them monitored through the `multitail` program, which you may need to install. When you tell the servers to stop, the PIDs are retrieved from a file and `kill`'ed.  
Install multitail through `apt install multitail` and run the script:
```
$ servers.sh [start|stop]
```

## Listener
The headphones are connecting to the audio server through the network. The device I'm using is an ESP32 DevKit. Through the current set up, we have to configure specific data:
```
const char* ssid     = "YOUR SSID";
const char* password = "YOUR PASSWORD";
const char* host     = "YOUR SERVER IP ADDRESS"; 
```
This technique is sub-optimal since we have the network password in **clear text**.  
The ESP32 is listening with the loop:
```
while (client.available() == 0);
while (client.available() >= 1) {
  uint8_t value = client.read();
  dataBuffer[writePointer] = value;
  writePointer++;
  if (writePointer == BUFFFERMAX) writePointer = 0;
}
```
and we play to the speaker with the following:
```
if (play) {
  dac_output_voltage(DAC_CHANNEL_1, dataBuffer[readPointer]);

  readPointer++;
  if (readPointer == BUFFFERMAX) { //BUFFERMAX = 8000
    readPointer = 0;
  }

  if ( getAbstand() == 0 ) {
    Serial.println("Buffer underrun!!!");
    play = false;
  }
}
```
### Wiring

### Resources
This project is based off of a blog post on [hackster.io](https://www.hackster.io/julianfschroeter/stream-your-audio-on-the-esp32-2e4661#code), however significant modifications have been made.
