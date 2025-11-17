@echo off
REM Clean compile/run script for CV Builder using JavaFX SDK
REM Usage: run.bat [path_to_javafx_lib]

setlocal

REM Always work from script directory
pushd "%~dp0"

REM Resolve JavaFX lib path
if "%~1"=="" (
  if "%JAVAFX_LIB%"=="" (
    echo JavaFX lib path missing.
    echo Usage: run.bat "C:\javafx-sdk-20\lib"
    echo Or set env var: set JAVAFX_LIB=C:\javafx-sdk-20\lib
    popd
    exit /b 1
  ) else (
    set "JFXLIB=%JAVAFX_LIB%"
  )
) else (
  set "JFXLIB=%~1"
)

echo Using JavaFX lib: %JFXLIB%

if not exist "%JFXLIB%\javafx-controls.jar" (
  echo ERROR: Could not find javafx-controls.jar in "%JFXLIB%".
  echo Verify the path points to the JavaFX SDK lib directory.
  popd
  exit /b 1
)

REM Prepare output folder
if not exist out mkdir out

REM Build sources list
del /q sources.txt 2>nul
for /r "src\main\java" %%f in (*.java) do (
  echo %%f>>sources.txt
)

REM Verify we found sources
for %%A in (sources.txt) do (
  if %%~zA lss 10 (
    echo ERROR: No Java source files found. Check project structure.
    popd
    exit /b 1
  )
)

echo Compiling...
javac --module-path "%JFXLIB%" --add-modules javafx.controls,javafx.fxml -d out @sources.txt
if errorlevel 1 (
  echo Compilation failed.
  popd
  exit /b 1
)

echo Running application...
java --module-path "%JFXLIB%;out" --add-modules javafx.controls,javafx.fxml -cp out cvbuilder.MainApp

popd
endlocal
