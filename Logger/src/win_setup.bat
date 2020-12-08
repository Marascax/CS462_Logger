@ECHO OFF
dir /s /B *.java > sources.txt
javac @sources.txt
start cmd /k cd /d %~dp0