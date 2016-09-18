#!/bin/bash
#a=$(pwd)
source /etc/profile
a="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo $a
mvn clean
#mvn assembly:assembly
mvn package
clear
echo "-----------start running program---------------"
echo
java -jar $a/target/MeasureServer*.jar
echo
echo "-----------stop  running program---------------"
