#!/bin/bash

javac -d . VoiceServer.java Helper.java ClientHandler.java
jar cvmf MANIFEST.MF server.jar voice/server/*
