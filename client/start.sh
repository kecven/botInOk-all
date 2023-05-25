#!/bin/bash

# add cron job to run this script every day
# 30 15 * * * /home/andrei/Projects/boty/start.sh"

cd $(dirname $0)
set -Eeuo pipefail


#git pull

#./gradlew bootJar

#echo "FINISHED BUILDING"
#echo "STARTING BOTY"

# M6
#java --module-path C:\Users\andte\Soft\javafx-sdk-19.0.2.1\lib --add-modules javafx.controls -jar build/libs/client-0.2.7.jar --spring.profiles.active=stage


# white-pc
java --module-path /home/andrei/Soft/javafx-sdk-17.0.7/lib --add-modules javafx.controls -jar build/libs/client-0.2.7.jar --spring.profiles.active=stage
