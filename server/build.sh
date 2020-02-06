#!/bin/bash

# Cleans the workspace
rm -r voice/
rm server.jar

# Builds the jar file
javac -d . VoiceServer.java Helper.java ClientHandler.java
jar cvmf MANIFEST.MF server.jar voice/server/*
