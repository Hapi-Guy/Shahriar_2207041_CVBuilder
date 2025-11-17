#!/usr/bin/env sh
# ----------------------------------------------------------------------------
# Maven Wrapper startup script for Unix-like systems
# ----------------------------------------------------------------------------

WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"
DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
  mkdir -p .mvn/wrapper
  echo "Downloading Maven Wrapper JAR..."
  curl -fsSL "$DOWNLOAD_URL" -o "$WRAPPER_JAR" || {
    echo "Failed to download Maven Wrapper JAR" >&2
    exit 1
  }
fi

exec java -classpath "$WRAPPER_JAR" -Dmaven.multiModuleProjectDirectory="$PWD" org.apache.maven.wrapper.MavenWrapperMain "$@"
