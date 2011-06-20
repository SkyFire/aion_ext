@echo off
title Aion X Emu Game Server Console

REM Start...
:start
echo Starting Aion X Emu Game Server.
echo.

REM SET PATH="Type here your path to java jdk/jre (including bin folder)."
REM NOTE: Remove tag REM from previous line.

REM -------------------------------------
REM Default parameters for a basic server.
java -Xms512m -Xmx1536m -ea -Xbootclasspath/p:./libs/jsr166.jar -cp ./libs/*;ax-game-1.0.1.jar gameserver.GameServer
REM -------------------------------------

SET CLASSPATH=%OLDCLASSPATH%

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
if ERRORLEVEL 0 goto end

REM Restart...
:restart
echo.
echo Administrator Restart ...
echo.
goto start

REM Error...
:error
echo.
echo Server is terminated abnormaly ...
echo.
goto end

REM End...
:end
echo.
echo Server is terminated ...
echo.
pause
