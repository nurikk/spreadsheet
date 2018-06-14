#!/bin/bash


for file in ./mocks/*.csv; do
    [ -e "$file" ] || continue
    echo $file
    time java -jar target/pw-cucumber-dbs-1.0-SNAPSHOT-jar-with-dependencies.jar -i $file  -o /dev/null
done