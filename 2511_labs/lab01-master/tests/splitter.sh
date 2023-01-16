#!/bin/bash
cd src
# Splitter
javac splitter/Splitter.java
diff <(java splitter/Splitter <<< "Welcome to my humble abode") <(printf "Enter a sentence specified by spaces only: \nWelcome\nto\nmy\nhumble\nabode\n")
