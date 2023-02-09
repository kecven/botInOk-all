#!/bin/bash

# add cron job to run this script every day
# 30 15 * * * /home/andrei/Projects/boty/start.sh"

cd $(dirname $0)
set -Eeuo pipefail


#git pull

#./gradlew bootJar

#echo "FINISHED BUILDING"
#echo "STARTING BOTY"

java --module-path C:\Users\andte\Soft\javafx-sdk-19.0.2.1\lib --add-modules javafx.controls -jar build/libs/client-0.2.7.jar --spring.profiles.active=stage
