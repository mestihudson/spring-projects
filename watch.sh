#!/bin/sh +x
apk update && apk add inotify-tools
place="$1"
shift
rest="$@"
cd "$place"
while true; do
  echo '' &&
  echo '--------------------------------------------------------------------' &&
  echo '' &&
  sleep 1 &&
  inotifywait -m --exclude "target" "$place" -r -e modify|mvn ${rest:-test}
done
cd -

