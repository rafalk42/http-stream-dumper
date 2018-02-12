#!/bin/bash

DIR=$(dirname $(readlink -f $0))

cd $DIR

java -cp "../target/*:../target/lib/*" -Dlog4j.configuration=file:log4j.properties rafalk42.http.stream.dumper.Main
