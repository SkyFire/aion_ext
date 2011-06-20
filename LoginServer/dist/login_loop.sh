#!/bin/bash

err=1
until [ $err == 0 ];
do
	java -Xms8m -Xmx32m -ea -Xbootclasspath/p:./libs/jsr166.jar -cp ./libs/*:ax-login-1.0.1.jar loginserver.LoginServer > log/stdout.log 2>&1
	err=$?
	sleep 10
done
