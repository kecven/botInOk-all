#!/bin/bash

cd $(dirname $0)
set -Eeuo pipefail

OS=$(uname -s)



cd ..
git pull
./gradlew :client:bootJar
cd client





case "$OS" in
  "Linux")
    echo "You are running Linux."
    xterm -e java --module-path /home/andrei/Soft/javafx-sdk-17.0.8/lib --add-modules javafx.controls -jar build/libs/client-0.2.7.jar --spring.profiles.active=stage
    ;;
  "Darwin")
    echo "You are running macOS."
    java --module-path /Users/atetka/Programs/javafx-sdk-19.0.2.1/lib --add-modules javafx.controls -jar ./build/libs/client-0.2.7.jar --spring.profiles.active=stage
    ;;
  "FreeBSD")
    echo "You are running FreeBSD."
    ;;
  "Windows")
    echo "You are running Windows."
    ;;
  *)
    echo "Unknown operating system."
    ;;
esac
