#!/bin/bash

cd $(dirname $0)
set -Eeuo pipefail


java --module-path /home/andrei/Soft/javafx-sdk-17.0.7/lib --add-modules javafx.controls -jar ./client/build/libs/client-0.2.7.jar