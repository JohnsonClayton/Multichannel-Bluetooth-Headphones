# Multichannel-Bluetooth-Headphones
Server and Arduino code for the Networks course

## Server
### Build
```
$ javac -d . server/VoiceServer.java server/Helper.java server/ClientHandler.java
$ jar cvmf MANIFEST.MF server.jar server/voice/server/*
```
### Run
```
$ java -jar server.jar
```

## Listener

### Resources
This project is based off of a blog post on [hackster.io](https://www.hackster.io/julianfschroeter/stream-your-audio-on-the-esp32-2e4661#code). Modifications have been made.
