@ECHO off
rem Ensure Launch4j is installed and is in your path
rem Hit enter when ready to start ...
pause
launch4jc sudokualex.xml
rem Ensure EXE file with current date / time was created in installation folder
pause
rem Ensure InnoSetup is in path
iscc /O+ sudokualex.iss
rem Check to ensure installation file has been created
pause
exit