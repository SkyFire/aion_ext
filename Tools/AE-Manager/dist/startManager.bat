@echo off
title Manager
:start
echo Starting the Manager.
echo.
REM -------------------------------------
REM Default parameters for a basic server.
java -Xms8m -Xmx32m -ea -cp ./libs/*;manager.jar com.aionengine.manager.Manager
REM
REM -------------------------------------

SET CLASSPATH=%OLDCLASSPATH%

if ERRORLEVEL 1 goto error
goto end
:error
echo.
echo Manager Terminated Abnormaly, Please Verify Your Files.
echo.
:end
echo.
echo Manager Terminated.
echo.
pause