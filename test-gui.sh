#!/bin/bash

echo "=== GUI Components Test ==="

# Compile tests
echo "Compiling tests..."
find src/test/java -name "*.java" > test-sources.txt
javac -d build-test -cp "build:lib/*" @test-sources.txt

if [ $? -eq 0 ]; then
    echo "Running GUI tests..."
    java -jar lib/junit-platform-console-standalone-1.9.2.jar \
         --class-path "build:build-test" \
         --scan-class-path \
         --include-package com.bankingapp.view
else
    echo "Test compilation failed!"
    exit 1
fi