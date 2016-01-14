/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.ui.ctrl;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import fxlab.win32.Kernel32;
import fxlab.win32.WINAPI;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author srey
 */
public class FXMLLabController implements Initializable {
    @FXML
    private GridPane gpn_formContainer;
    @FXML
    private Label lbl_application;
    @FXML
    private ComboBox<String> cmb_application;
    @FXML
    private TextArea txta_log;
    @FXML
    private Button btn_analyze;
    @FXML
    private Button btn_clear;
    @FXML
    private Button btn_export;
    
    private enum ContsantsMessages {
        DOT_NET_GET_CONTROL_NAME("WM_GETCONTROLNAME"),
        VB6_GET_CONTROL_NAME("Get_CONTROLNAME");
        
        private String value;

        private ContsantsMessages(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    };
    
    private enum PageProtection {
        NO_ACCESS(0x01), READ_WRITE(0x04);
        
        private final int value;

        private PageProtection(int value) {
            this.value = value;
        }
        
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadApplications();
    }    
    
    private void loadApplications() {
        ObservableList<String> applications= FXCollections.observableArrayList();
        
        WINAPI.INSTANCE_API.EnumWindows((WinDef.HWND hwnd, Pointer pntr) -> {
            char[] bufferApp= new char[1024];
            String nameApp= null;
            
            WINAPI.INSTANCE_API.GetWindowText(hwnd, bufferApp, bufferApp.length);
            nameApp= Native.toString(bufferApp);
            
            if (nameApp != null && !nameApp.isEmpty())
                applications.add(nameApp);
            
            return true;
        }, Pointer.NULL);
        
        if (applications.size() > 0)
            this.cmb_application.setItems(applications);
    }
    
    private WinDef.HWND getHandlerActiveWindow(String name) {
        return WINAPI.INSTANCE_API.FindWindow(null, name);
    }
    
    private void clear() {
        this.txta_log.clear();
    }
    
    private void getChildWindows(WinDef.HWND parentHandler, IntByReference pid) {
        WinDef.HWND childHandle= WINAPI.INSTANCE_API.FindWindowEx(parentHandler, null, null, null);
        int i= 0;
        final int MAX= 100;
        char[] buffer= new char[1024];
        String nameControl= null;
        
        while (i++ < MAX) {            
            childHandle= WINAPI.INSTANCE_API.FindWindowEx(parentHandler, childHandle, null, null);
            
            if (childHandle != null && childHandle.getPointer() != Pointer.NULL) {
                WINAPI.INSTANCE_API.GetClassName(childHandle, buffer, buffer.length);
                nameControl= Native.toString(buffer);
                
                if (nameControl != null && !nameControl.isEmpty())
                        this.txta_log.appendText(String.format("\tControl Child Name: %s%s", 
                                nameControl, System.getProperty("line.separator")));
                
                this.txta_log.appendText(String.format("\tProperty Name: %s%n", getNameProperty(childHandle, pid)));
            }
        }
    }
    
    private String getNameProperty(WinDef.HWND hWnd, IntByReference pid) {
        String result= "";
        
        int size= 65535; //size of memory to be allocated
        int retLength= 0;
        int msg= 0;
        
        boolean retVal= false;
        
        char[] nameProperty= new char[size];
        
        WinNT.HANDLE bufferMen= null;
        WinNT.HANDLEByReference written= null;
        WinNT.HANDLE processHandle= null;
        
        try {
            msg= WINAPI.INSTANCE_API.
                RegisterWindowMessage(ContsantsMessages.DOT_NET_GET_CONTROL_NAME.
                        toString());
        
            processHandle= Kernel32.INSTANCE_API.
                                OpenProcess(Kernel32.PROCESS_VM_OPERATION | 
                                        Kernel32.PROCESS_VM_READ | 
                                        Kernel32.PROCESS_VM_WRITE, false, pid.getValue());

            if (processHandle != null) {
                bufferMen= Kernel32.INSTANCE_API.VirtualAllocEx(processHandle, 0, size + 1, 
                        Kernel32.MEM_RESERVE | Kernel32.MEM_COMMIT, Kernel32.PAGE_READWRITE);

                if (bufferMen != null) {
                    retLength= WINAPI.INSTANCE_API.SendMessage(hWnd, msg, size + 1, bufferMen);

                    retVal= Kernel32.INSTANCE_API.ReadProcessMemory(processHandle, bufferMen, nameProperty, size + 1, written);

                    if (retVal)
                        result= Native.toString(nameProperty);
                    
                    else
                        System.err.println(String.format("Error message when try to read process: %s", Kernel32Util.getLastErrorMessage()));
                    
                } else
                    System.err.println(String.format("Error message when try to allocate memory: %s", Kernel32Util.getLastErrorMessage()));
                
            } else
                System.err.println(String.format("Error message when try to open new process: %s", Kernel32Util.getLastErrorMessage()));
            
        } finally {
            if (processHandle != null) {
                retVal= Kernel32.INSTANCE_API.VirtualFreeEx(processHandle, bufferMen, 0, Kernel32.MEM_RELEASE);
            
                if (!retVal)
                    System.err.println(String.format("Error message free allocate memory: %s", Kernel32Util.getLastErrorMessage()));
            }
        }
        
        return result;
    }
    
    @FXML
    private void handleBtn_analyzeAction(ActionEvent evt) {
        this.clear();
        
        if (!this.cmb_application.getSelectionModel().isEmpty()) {
            WinDef.HWND hActive= getHandlerActiveWindow(this.cmb_application.getSelectionModel().getSelectedItem());
                    
            if (hActive != null) {
                IntByReference pid= new IntByReference();
                WINAPI.INSTANCE_API.GetWindowThreadProcessId(hActive, pid);

                this.txta_log.appendText(String.format("Process ID: %d%s", pid.getValue(), System.getProperty("line.separator")));
            
                WINAPI.INSTANCE_API.EnumChildWindows(hActive, (WinDef.HWND hwnd, Pointer pntr) -> {
                    char[] buffer= new char[1024];
                    String nameControl= null;
                    
                    if (WINAPI.INSTANCE_API.IsWindowVisible(hwnd)) {
                        WINAPI.INSTANCE_API.GetClassName(hwnd, buffer, buffer.length);
                        nameControl= Native.toString(buffer);

                        if (nameControl != null && !nameControl.isEmpty())
                            this.txta_log.appendText(String.format("Control Name: %s%s", 
                                    nameControl, System.getProperty("line.separator")));
                        
                        WINAPI.INSTANCE_API.RealGetWindowClass(hwnd, buffer, buffer.length);
                        nameControl= Native.toString(buffer);
                        
                        if (nameControl != null && !nameControl.isEmpty())
                            this.txta_log.appendText(String.format("\tReal Control Name: %s%s", 
                                    nameControl, System.getProperty("line.separator")));
                        
                        this.txta_log.appendText(String.format("\tName Property: %s%n", getNameProperty(hwnd, pid)));
                        
                        getChildWindows(hwnd, pid);
                        /*
                        WINAPI.INSTANCE_API.EnumPropsEx(hwnd, (WinDef.HWND hWnd, WString lpszString, WinNT.HANDLE hData, BaseTSD.ULONG_PTR dwData) -> {
                            this.txta_log.appendText(String.format("\tProperty Ex: %s%n", lpszString));
                            
                            return true;
                        }, null); */
                    }
                    
                    return true;
                }, Pointer.NULL);
            }
        }
    }
    
    @FXML
    private void handleBtn_ClearAction(ActionEvent evt) {
        this.clear();
    }
    
    @FXML
    private void handleBtn_Export(ActionEvent evt) {
        if (!this.txta_log.getText().isEmpty()) {
            FileChooser fc= new FileChooser();
            File selected= null;
            
            fc.setTitle("Save Log");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Terxt File (*.txt)", "*.txt"));
            
            selected= fc.showSaveDialog(this.txta_log.getScene().getWindow());
            
            if (selected != null) {
                try (FileWriter fw= new FileWriter(selected)) {
                    fw.write(this.txta_log.getText());
                } catch (IOException ex) {
                    Logger.getLogger(FXMLLabController.class.getName()).
                            log(Level.SEVERE, String.format("Ocurred an error when try to export control name log in file: %s", 
                                    selected.getPath()), ex);
                }
            }
        }
    }
}