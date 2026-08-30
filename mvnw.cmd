@echo off
setlocal
set "BASE_DIR=%~dp0"
java -Dmaven.multiModuleProjectDirectory="%BASE_DIR%" -classpath "%BASE_DIR%.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
exit /b %ERRORLEVEL%