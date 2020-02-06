#!/bin/bash

dir="voice/"
jar="server.jar"

# Cleans the workspace
if [ -d $dir ] ; then
  rm -r $dir
fi
if [ -f $jar ] ; then
  rm $jar 
fi

# Builds the jar file
javac -d . VoiceServer.java Helper.java ClientHandler.java
jar cvmf MANIFEST.MF server.jar voice/server/*
