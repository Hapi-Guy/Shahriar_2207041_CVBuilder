@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup script for Windows
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set MAVEN_WRAPPER_DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

set WRAPPER_JAR=.mvn\wrapper\maven-wrapper.jar
set WRAPPER_PROPERTIES=.mvn\wrapper\maven-wrapper.properties
set DOWNLOAD_DIR=.mvn\wrapper

if not exist "%WRAPPER_JAR%" (
  if not exist "%DOWNLOAD_DIR%" mkdir "%DOWNLOAD_DIR%"
  echo Downloading Maven Wrapper JAR...
  powershell -Command "[Net.ServicePointManager]::SecurityProtocol='Tls12'; (New-Object Net.WebClient).DownloadFile('%MAVEN_WRAPPER_DOWNLOAD_URL%', '%WRAPPER_JAR%')" || (
    echo Failed to download Maven Wrapper JAR
    exit /b 1
  )
)

set JAVA_EXE=java.exe
where %JAVA_EXE% >nul 2>&1 || (
  echo JAVA not found on PATH. Please install a JDK and add it to PATH.
  exit /b 1
)

%JAVA_EXE% -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%CD%" org.apache.maven.wrapper.MavenWrapperMain %*
endlocal
