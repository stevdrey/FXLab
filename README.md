# FXLab
Lab to UI Atomatization in WinForms

**Dependencies:**
  1. JNA: https://github.com/java-native-access/jna
  2. JNativeHook: https://github.com/kwhat/jnativehook
  3. JDK version: Java 8u66 http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
  4. Scene Builder 2.0: http://www.oracle.com/technetwork/java/javafxscenebuilder-1x-archive-2199384.html

Tasks:
  1. Create a Form with the following specifications:
      1. One Label and Combo-Box for user can select the target application
      2. One Label and Textbox for Class Name of the Window (Control) select by User
      3. One Label and TextBox for ID of Window (Control) select by User
      4. One Label and TextBox for Captions of Window (Control) select by User, this filed just fill in case the Window (Control) is
         a Label (class name: Static)
      5. One Label and TextBox for text of Window (Control) select by User, this apply if Window is a TextBox, Combo-Box or ListBox
      6. One Button for "Record" or Listening click Events of Mouse

*Notes:
  1. If your Architecture is x64 you must download JDK for architecture x64, because JVM not responds well in compatibility mode.
