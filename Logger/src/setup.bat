@ECHO OFF
dir /s /B *.java > sources.txt
javac @sources.txt
start rmiregistry
start cmd /k cd /d %~dp0
start cmd /k cd /d %~dp0