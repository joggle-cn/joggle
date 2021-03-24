@echo off

echo "                           _ooOoo_                      "
echo "                           o8888888o                    "
echo "                           88" . "88                    "
echo "                           (| -_- |)                    "
echo "                           O\  =  /O                    "
echo "                        ____/`---'\____                 "
echo "                      .'  \\|     |//  `.               "
echo "                     /  \\|||  :  |||//  \              "
echo "                    /  _||||| -:- |||||_  \             "
echo "                    |   | \\\  -  /'| |   |             "
echo "                    | \_|  `\`---'//  |_/ |             "
echo "                    \  .-\__ `-. -'__/-.  /             "
echo "                  ___`. .'  /--.--\  `. .'___           "
echo "               ."" '<  `.___\_<|>_/___.' _> \"".        "
echo "              | | :  `- \`. ;`. _/; .'/ /  .' ; |       "
echo "              \  \ `-.   \_\_`. _.'_/_/  -' _.' /       "
echo "            ===`-.`___`-.__\ \___  /__.-'_.'_.-'===     "
setlocal

set "BULLET_HOME=%~dp0.."



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

echo "Bullet Home %BULLET_HOME%"

set NGROK_BIN="%BULLET_HOME%\bin\ngrok.exe"
set BULLER_CONF_DIR="%BULLET_HOME%\conf"
set BULLER_LOGS_DIR="%BULLET_HOME%\logs\bullet.log"
echo "Bullet config: %BULLER_CONF_DIR%"



%NGROK_BIN% -log=%BULLER_LOGS_DIR%  -config=%BULLER_CONF_DIR%/ngrok.yml  start-all