#!/bin/bash

cd $(dirname $0)
set -Eeuo pipefail


OS=$(uname -s)


#!/bin/bash


case "$OS" in
  "Linux")
    echo "You are running Linux."
    echo "Check location for javaFX."
    xterm -e java --module-path /home/andrei/Soft/javafx-sdk-17.0.8/lib --add-modules javafx.controls -jar build/libs/client-0.2.7.jar --spring.profiles.active=stage
    ;;
  "Darwin")
    echo "You are running macOS."
    java --module-path /Users/atetka/Programs/javafx-sdk-19.0.2.1/lib --add-modules javafx.controls -jar ./client/build/libs/client-0.2.7.jar --spring.profiles.active=stage
    ;;
  "FreeBSD")
    echo "You are running FreeBSD."
    # Вставьте здесь команды для FreeBSD
    ;;
  "Windows")
    echo "You are running Windows."
    # Вставьте здесь команды для Windows
    ;;
  *)
    echo "Unknown operating system."
    ;;
esac
