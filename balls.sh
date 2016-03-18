#!/bin/bash
if [ -z "$@" ]; then
    ant run
else
    ant runargs -Dargs="$@"
fi
