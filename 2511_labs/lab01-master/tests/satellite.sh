#!/bin/bash
cd src
# Satellite
javac satellite/Satellite.java
diff <(java satellite/Satellite) <(printf "I am Satellite A at position 122.0 degrees, 10000 km above the centre of the earth and moving at a velocity of 55.0 metres per second\n2.129301687433082\n0.04303052592865024\n4380.0\n")
