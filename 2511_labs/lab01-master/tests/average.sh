#!/bin/bash
cd src
# Average
javac average/Average.java
diff <(java average/Average) <(echo "The average is 3.5")
