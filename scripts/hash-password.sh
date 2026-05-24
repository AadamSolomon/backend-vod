#!/bin/bash

read -s -p "Enter password: " password
echo

echo -n "$password" | sha256sum | cut -d' ' -f1
