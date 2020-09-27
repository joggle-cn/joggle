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

set BULLER_CONF_DIR="%BULLET_HOME%\conf"
echo "Bullet config: %BULLER_CONF_DIR%"









 

java -Djava.bullet.conf.dir="%BULLER_CONF_DIR%" -Djava.bullet.home.dir="%BULLET_HOME%" -Djava.security.egd=file:/dev/./urandom  -jar %BULLET_HOME%/lib/bullet-client.jar --spring.profiles.active=prod