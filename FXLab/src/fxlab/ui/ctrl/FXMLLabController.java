/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.ui.ctrl;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import fxlab.win32.Kernel32;
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
import fxlab.win32.User32;
import fxlab.win32.enu.ConstantsMessages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    @FXML
    private Button btn_openWindowProperties;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadApplications();
    }    
    
    /**
     * This method get all the applications is current load in the Operating system
     * and store the name in the combo-box control, to show user
     */
    private void loadApplications() {
        ObservableList<String> applications= FXCollections.observableArrayList();
        
        User32.INSTANCE_API.EnumWindows((WinDef.HWND hwnd, Pointer pntr) -> {
            char[] bufferApp= new char[1024];
            String nameApp= null;
            
            User32.INSTANCE_API.GetWindowText(hwnd, bufferApp, bufferApp.length);
            nameApp= Native.toString(bufferApp);
            
            if (nameApp != null && !nameApp.isEmpty())
                applications.add(nameApp);
            
            return true;
        }, Pointer.NULL);
        
        if (applications.size() > 0)
            this.cmb_application.setItems(applications);
    }
    
    /**
     * This method get a Pointer or Handler of applications selected by the user
     * in combo-box.
     * 
     * @param name
     *          The name of application we want to know the Handler.
     * 
     * @return 
     *      A Pointer or Window Handler by it's name.
     */
    private WinDef.HWND getHandlerActiveWindow(String name) {
        return User32.INSTANCE_API.FindWindow(null, name);
    }
    
    /**
     * This method just clear the text area, that use for logging purpose
     */
    private void clear() {
        this.txta_log.clear();
    }
    
    /**
     * This method lookups for children in parent window or control and logging if
     * find any.
     * 
     * @param parentHandler
     *          Pointer or window Handler of the parent window (control) that we 
     *          want to looking for children.
     * 
     * @param pid 
     *          The number of the Process ID is run the target application.
     */
    private void getChildWindows(WinDef.HWND parentHandler, IntByReference pid) {
        WinDef.HWND childHandle= User32.INSTANCE_API.FindWindowEx(parentHandler, null, null, null);
        int i= 0;
        final int MAX= 100;
        char[] buffer= new char[1024];
        String nameControl;
        
        while (i++ < MAX) {            
            childHandle= User32.INSTANCE_API.FindWindowEx(parentHandler, childHandle, null, null);
            
            if (childHandle != null && childHandle.getPointer() != Pointer.NULL) {
                User32.INSTANCE_API.GetClassName(childHandle, buffer, buffer.length);
                nameControl= Native.toString(buffer);
                
                if (nameControl != null && !nameControl.isEmpty())
                        this.txta_log.appendText(String.format("\tControl Child Name: %s%s", 
                                nameControl, System.getProperty("line.separator")));
                
                this.txta_log.appendText(String.format("\tProperty Name: %s%n", getNameProperty(childHandle, pid)));
            }
        }
    }
    
    /**
     * This method return the ID of an specific window or control
     * 
     * @param hWnd
     *          Handler or Pointer of window (Control) we want to know the ID ({@code .Name} property in WinForms)
     * 
     * @param pid
     *          The number of the Process ID is run the target application.
     * 
     * @return 
     *      This method return the ID ({@code .Name} property in WinForms) of an 
     *      specific window or control
     */
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
            // Register message for know the .Name property of the window Handler pass in parameter
            msg= User32.INSTANCE_API.
                RegisterWindowMessage(ConstantsMessages.DOT_NET_GET_CONTROL_NAME.
                        toString());
        
            // create a process with shared memory.
            processHandle= Kernel32.INSTANCE_API.
                                OpenProcess(Kernel32.PROCESS_VM_OPERATION | 
                                        Kernel32.PROCESS_VM_READ | 
                                        Kernel32.PROCESS_VM_WRITE, false, pid.getValue());

            if (processHandle != null) {
                // allocate an space of memory that will contains the result of .Name property
                bufferMen= Kernel32.INSTANCE_API.VirtualAllocEx(processHandle, 0, size + 1, 
                        Kernel32.MEM_RESERVE | Kernel32.MEM_COMMIT, Kernel32.PAGE_READWRITE);

                if (bufferMen != null) {
                    // send the message to target application and specific window (Control)
                    retLength= User32.INSTANCE_API.SendMessage(hWnd, msg, size + 1, bufferMen);

                    // read the shared memory and get the value of .name property.
                    retVal= Kernel32.INSTANCE_API.ReadProcessMemory(processHandle, bufferMen, nameProperty, size + 1, written);

                    // if read was successfull then converto to String the value.
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
                // realese the memory allocate
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
            // get the Handler or Pointer of application selected by user
            WinDef.HWND hActive= getHandlerActiveWindow(this.cmb_application.getSelectionModel().getSelectedItem());
                    
            if (hActive != null) {
                IntByReference pid= new IntByReference();
                
                // get the number of Process ID of target application
                User32.INSTANCE_API.GetWindowThreadProcessId(hActive, pid);

                this.txta_log.appendText(String.format("Process ID: %d%s", pid.getValue(), System.getProperty("line.separator")));
            
                // looking for any child window (Control) of the target application
                User32.INSTANCE_API.EnumChildWindows(hActive, (WinDef.HWND hwnd, Pointer pntr) -> {
                    char[] buffer= new char[1024];
                    String nameControl= null;
                    
                    User32.INSTANCE_API.RealGetWindowClass(hActive, buffer, buffer.length);
                    nameControl= Native.toString(buffer);
                    
                    this.txta_log.appendText(String.format("Parent class name: %s%n", 
                            nameControl));
                    
                    if (User32.INSTANCE_API.IsWindowVisible(hwnd)) {
                        // get the name of the class of Control child
                        User32.INSTANCE_API.GetClassName(hwnd, buffer, buffer.length);
                        nameControl= Native.toString(buffer);

                        if (nameControl != null && !nameControl.isEmpty())
                            this.txta_log.appendText(String.format("Control Name: %s%s", 
                                    nameControl, System.getProperty("line.separator")));
                        
                        User32.INSTANCE_API.RealGetWindowClass(hwnd, buffer, buffer.length);
                        nameControl= Native.toString(buffer);
                        
                        if (nameControl != null && !nameControl.isEmpty())
                            this.txta_log.appendText(String.format("\tReal Control Name: %s%s", 
                                    nameControl, System.getProperty("line.separator")));
                        
                        this.txta_log.appendText(String.format("\tName Property: %s%n", getNameProperty(hwnd, pid)));
                        
                        getChildWindows(hwnd, pid);
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
    
    @FXML
    private void handleBtn_openWindowProperties(ActionEvent evt) {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/fxlab/ui/FXMLWindowProperties.fxml"));
        Parent root;
        Stage stage;
        
        try {
            stage= (Stage) this.gpn_formContainer.getScene().getWindow();
            root = loader.load();
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.setTitle("Window Properties");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLLabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}