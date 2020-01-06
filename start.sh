#!/bin/sh

rm /home/chromium/prj/PicFilter/build/*

banner 24Circles

echo "building the project"

javac -d /home/chromium/prj/PicFilter/build/ /home/chromium/prj/PicFilter/src/*.java

echo "running the server"

java -cp /home/chromium/prj/PicFilter/build Server

