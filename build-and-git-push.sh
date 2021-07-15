#!/bin/bash

COMMIT_MESSAGE="$1"

rm -r ./target/
./mvnw package

git add .
git commit -m "$COMMIT_MESSAGE"
git push