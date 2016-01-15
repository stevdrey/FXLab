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
import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
import fxlab.win32.Kernel32;
import fxlab.win32.User32;
import fxlab.win32.enu.ContsantsMessages;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author srey
 */
public class FXMLWindowPropertiesController implements Initializable {
    private User32 user32;
    private Kernel32 kernel32;
    
    @FXML
    private GridPane gpn_formContainer;
    @FXML
    private Label lbl_className;
    @FXML
    private TextField txt_className;
    @FXML
    private Label lbl_id;
    @FXML
    private TextField txt_id;
    @FXML
    private Label lbl_caption;
    @FXML
    private TextField txt_caption;
    @FXML
    private Label lbl_text;
    @FXML
    private TextField txt_text;
    @FXML
    private Label lbl_application;
    @FXML
    private ComboBox<String> cmb_application;
    @FXML
    private Button btn_record;
    @FXML
    private Button btn_clear;
    @FXML
    private Button btn_cancel;    
    @FXML
    private Button btn_update;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.user32= User32.INSTANCE_API;
        this.kernel32= Kernel32.INSTANCE_API;
        
        this.cmb_application.getItems().setAll(getListOfApps());
    }    
    
    // section of private methods
    
    /**
     * This method clear the controls in form
     * 
     * @param all 
     *          If this parameter is true clear all the controls, 
     *          else just clear text field controls.
     */
    private void clearControls(boolean all) {
        if (all)
            this.cmb_application.getItems().clear();
        
        this.txt_caption.clear();
        this.txt_className.clear();
        this.txt_id.clear();
        this.txt_text.clear();
    }
    
    /**
     * This method looking for an application is running in Operating System
     * 
     * @return 
     *      Return a list with all the names of applications running in Operating System.
     */
    private ObservableList<String> getListOfApps() {
        ObservableList<String> listOfApps= FXCollections.observableArrayList();
        
        user32.EnumWindows((WinDef.HWND hwnd, Pointer pntr) -> {
            char[] bufferApp= new char[1024];
            String nameApp= null;
            
            user32.GetWindowText(hwnd, bufferApp, bufferApp.length);
            nameApp= Native.toString(bufferApp);
            
            if (nameApp != null && !nameApp.isEmpty())
                listOfApps.add(nameApp);
            
            return true;
        }, Pointer.NULL);
        
        return listOfApps;
    }
    
    /**
     * This method get and return the class name of the Window or Control, for Handler
     * or Pointer pass in the parameter.
     * 
     * @param handler
     *          Pointer or Handler of the Window (Control) we want to know class name.
     * 
     * @return 
     *      Return the class name of specific Window or Control.
     */
    private String getClassName(WinDef.HWND handler) {
        char[] buffer= new char[1024];
        String nameControl= "";
        
        if (handler != null) {
            user32.GetClassName(handler, buffer, buffer.length);
            nameControl= Native.toString(buffer);
        }
        
        return nameControl;
    }
    
    /**
     * This method get the number of Process ID of specific application.
     * 
     * @param hwnd
     *          Handler or Pointer that we want to know its Number of Process ID
     * 
     * @return 
     *      Return the number of process ID of specific application.
     */
    private IntByReference getProcessID(WinDef.HWND hwnd) {
        IntByReference pid= new IntByReference();
        
        if (hwnd != null)
            user32.GetWindowThreadProcessId(hwnd, pid);
        
        return pid;
    }
    
    /**
     * This method get a Pointer or Handler of applications by name pass in parameter.
     * 
     * @param name
     *          The name of application we want to know the Handler.
     * 
     * @return 
     *      A Pointer or Window Handler by it's name.
     */
    private WinDef.HWND getWindowHandler(String name) {
        return user32.FindWindow(null, name);
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
            msg= registerMessage(ContsantsMessages.DOT_NET_GET_CONTROL_NAME.toString());
        
            // create a process with shared memory.
            processHandle= openProcessShared(pid);

            if (processHandle != null) {
                // allocate an space of memory that will contains the result of .Name property
                bufferMen= allocateMemory(processHandle, size + 1);

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
    
    /**
     * This method register a new message and return an unique ID.
     * 
     * @param message
     *          String with the name of new message we want to register.
     * 
     * @return 
     *      Return an unique ID for the message registered.
     */
    private int registerMessage(String message) {
        return user32.RegisterWindowMessage(message);
    }
    
    /**
     * This method create and open new process with shared memory.
     * 
     * @param pid
     *          Reference to Process ID of application we want to shared memory.
     * 
     * @return 
     *      Return a Pointer or Handler for the new process.
     */
    private WinNT.HANDLE openProcessShared(IntByReference pid) {
        return kernel32.OpenProcess(Kernel32.PROCESS_VM_OPERATION | 
                                        Kernel32.PROCESS_VM_READ | 
                                        Kernel32.PROCESS_VM_WRITE, false, pid.getValue());
    }
    
    /**
     * This method allocate space of memory than user can write or read from that.
     * 
     * @param handle
     *          Pointer or Handler for allocates memory within the virtual address space of this process.
     * 
     * @param size
     *          The size of memory we want to allocate or reserve.
     * 
     * @return 
     *      Return a Pointer or Handler of reserve memory.
     */
    private WinNT.HANDLE allocateMemory(WinNT.HANDLE handle, int size) {
        return kernel32.VirtualAllocEx(handle, 0, size, 
                        Kernel32.MEM_RESERVE | Kernel32.MEM_COMMIT, Kernel32.PAGE_READWRITE);
    }
    
    /**
     * This method get the caption or the text value in a specific Window (Control),
     * depends of its class name or type of Window (Control).
     * 
     * @param hwnd
     *          The pointer or handler of the Window (Control) we want to know its
     *          caption or text value.
     * 
     * @return 
     *      Return the text value or caption, depends of its class name or type.
     */
    private String getText(WinDef.HWND hwnd) {
        String caption= "";
        int size= getTextLength(hwnd);
        char[] buffer= new char[size == 0 ? 1024 : size];
        
        if (hwnd != null) {
            user32.SendMessage(hwnd, User32.WM_GETTEXT, buffer.length, buffer);
            caption= Native.toString(buffer);
        }
        
        return caption;
    }
    
    private int getTextLength(WinDef.HWND hwnd) {
        int length= 0;
        
        if (hwnd != null)
            length= user32.SendMessage(hwnd, User32.WM_GETTEXTLENGTH, 0, 
                    new WinNT.HANDLE(Pointer.NULL));
        
        return length;
    }
    
    private int getListBoxTextLength(WinDef.HWND hwnd) {
        int length= 0;
        
        if (hwnd != null)
            length= user32.SendMessage(hwnd, User32.LB_GETTEXTLEN, 0, 
                    new WinNT.HANDLE(Pointer.NULL));
        
        return length;
    }
    
    // section of event handlers
    
    @FXML
    private void handleBtn_updateAction(ActionEvent evt) {
        this.cmb_application.getItems().setAll(getListOfApps());
    }
    
    @FXML
    private void handleBtn_clear(ActionEvent evt) {
        this.clearControls(true);
    }
}