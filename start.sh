#!/bin/bash

cd $(dirname $0)
set -Eeuo pipefail

OS=$(uname -s)

case "$OS" in
  "Linux")
    echo "You are running Linux."
    java --module-path ./libs/openjfx-17.0.7_linux-x64_bin-sdk/lib --add-modules javafx.controls -jar ./client/build/libs/client-0.2.7.jar --spring.profiles.active=stage
    ;;
  "Darwin")
    echo "You are running macOS."
    java --module-path ./libs/openjfx-17.0.7_osx-x64_bin-sdk/lib --add-modules javafx.controls -jar ./client/build/libs/client-0.2.7.jar --spring.profiles.active=stage
    ;;
  "FreeBSD")
    echo "You are running FreeBSD."
    # Вставьте здесь команды для FreeBSD
    ;;
  "Windows")
    echo "You are running Windows."
    java --module-path ./libs/openjfx-17.0.7_windows-x64_bin-sdk/lib --add-modules javafx.controls -jar ./client/build/libs/client-0.2.7.jar --spring.profiles.active=stage
    ;;
  *)
    echo "Unknown operating system."
    ;;
esac