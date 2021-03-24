@echo off

setlocal
  
 
cd %~dp0
cd ..

set BULLET_HOME=%cd%

set "CURRENT_DIR=%cd%"
if not "%BULLET_HOME%" == "" goto gotHome
set "BULLET_HOME=%CURRENT_DIR%"
if exist "%BULLET_HOME%\bin\bullet.bat" goto okHome
cd ..
set "BULLET_HOME=%cd%"
cd "%CURRENT_DIR%"
if exist "%BULLET_HOME%\bin\bullet.bat" goto okHome
set "BULLET_HOME=%~dp0\.."
:gotHome

if exist "%BULLET_HOME%\bin\bullet.bat" goto okHome
echo The BULLET_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okHome



echo %BULLET_HOME%




echo "Bullet Home %BULLET_HOME%"

set NGROK_BIN="%BULLET_HOME%\bin\ngrok.exe"
set BULLER_CONF_DIR="%BULLET_HOME%\conf"
echo "Bullet config: %BULLER_CONF_DIR%"



$NGROK_BIN -log  -config=$BULLER_CONF_DIR/ngrok.yml  start-all