#!/bin/bash
function run_junit() {
    exercise=$1
    rm -rf bin/$exercise
    javac -d bin -cp "$JUNIT" $(find src/$exercise -name "*.java")
    java -jar "$JUNIT" -cp bin:src/$exercise --scan-class-path --disable-ansi-colors --disable-banner 2>&1
}


JUNIT="lib/junit-platform-console-standalone-1.7.0-M1.jar"

# Scrabble
run_junit scrabble
