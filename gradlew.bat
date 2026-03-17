@ECHO OFF
set DIR=%~dp0
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if exist "%JAVA_EXE%" goto execute
set JAVA_EXE=java.exe
:execute
"%JAVA_EXE%" -classpath "%DIR%\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
