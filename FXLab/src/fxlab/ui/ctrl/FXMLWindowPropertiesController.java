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
import fxlab.ui.enu.TypeControl;
import fxlab.ui.evt.MouseListerImpl;
import fxlab.win32.Kernel32;
import fxlab.win32.POINTByValue;
import fxlab.win32.User32;
import fxlab.win32.enu.ConstantsMessages;
import java.net.URL;
import java.util.EventListener;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseListener;

/**
 * FXML Controller class
 *
 * @author srey
 */
public class FXMLWindowPropertiesController implements Initializable {
    private User32 user32;
    private Kernel32 kernel32;
    private ObservableList<EventListener> eventsList;
    private AtomicBoolean isRecord;
    
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
        this.eventsList= FXCollections.observableArrayList();
        this.isRecord= new AtomicBoolean(false);
        
        this.cmb_application.setItems(getListOfApps());
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
            user32.RealGetWindowClass(handler, buffer, buffer.length);
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
            msg= registerMessage(ConstantsMessages.DOT_NET_GET_CONTROL_NAME.toString());
        
            // create a process with shared memory.
            processHandle= openProcessShared(pid);

            if (processHandle != null) {
                // allocate an space of memory that will contains the result of .Name property
                bufferMen= allocateMemory(processHandle, size + 1);

                if (bufferMen != null) {
                    // send the message to target application and specific window (Control)
                    retLength= user32.SendMessage(hWnd, msg, size + 1, bufferMen);

                    // read the shared memory and get the value of .name property.
                    retVal= kernel32.ReadProcessMemory(processHandle, bufferMen, 
                            nameProperty, size + 1, written);

                    // if read was successfull then converto to String the value.
                    if (retVal)
                        result= Native.toString(nameProperty);
                    
                    else
                        System.err.println(String.format("Error message when try to read process: %s", 
                                Kernel32Util.getLastErrorMessage()));
                    
                } else
                    System.err.println(String.format("Error message when try to allocate memory: %s", 
                            Kernel32Util.getLastErrorMessage()));
                
            } else
                System.err.println(String.format("Error message when try to open new process: %s", 
                        Kernel32Util.getLastErrorMessage()));
            
        } finally {
            if (processHandle != null) {
                // realese the memory allocate
                retVal= kernel32.VirtualFreeEx(processHandle, bufferMen, 0, 
                        Kernel32.MEM_RELEASE);
            
                if (!retVal)
                    System.err.println(String.format("Error message free allocate memory: %s", 
                            Kernel32Util.getLastErrorMessage()));
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
     * @param type 
     *          Enum indicate the type of control we want to know text or caption
     *          of specific Window (Control).
     * 
     * @return 
     *      Return the text value or caption, depends of its class name or type.
     */
    private String getText(WinDef.HWND hwnd, TypeControl type) {
        String text= "";
        int size= getTextLength(hwnd, type);
        int message= 0;
        char[] buffer= new char[size == 0 ? 1024 : size];
        
        if (hwnd != null) {
            switch (type) {
                case COMBO_BOX:
                    message= User32.CB_GETLBTEXT;
                    
                    break;
                    
                case LIST_BOX:
                    message= User32.LB_GETTEXT;
                    
                    break;
                    
                default:
                    message= User32.WM_GETTEXT;
                    
                    break;
            }
            
            if (type == TypeControl.COMBO_BOX || type == TypeControl.LIST_BOX)
                user32.SendMessage(hwnd, message, getSelectedIndex(hwnd, type), 0);
            
            else
                user32.SendMessage(hwnd, message, buffer.length, buffer);
            
            text= Native.toString(buffer);
        }
        
        return text;
    }
    
    /**
     * This method get the size or length of text/caption of specific Window (Control)
     * 
     * @param hwnd
     *          The pointer or handler of the Window (Control) we want to know its
     *          length of text/caption.
     * 
     * @param type
     *          Enum indicate the type of control we want to know text or caption
     *          of specific Window (Control).
     * 
     * @return 
     *      Return the size or length of text/caption of specific Window (Control).
     */
    private int getTextLength(WinDef.HWND hwnd, TypeControl type) {
        int length= 0;
        int message= 0;
        
        if (hwnd != null) {
            switch (type) {
                case COMBO_BOX:
                    message= User32.CB_GETLBTEXTLEN;

                    break;

                case LIST_BOX:
                    message= User32.LB_GETTEXTLEN;

                    break;

                default:
                    message= User32.WM_GETTEXTLENGTH;

                    break;
            }
            
            length= user32.SendMessage(hwnd, message, 0, 0) + 1;
        }
        
        return length;
    }
    
    private int getSelectedIndex(WinDef.HWND hwnd, TypeControl type) {
        int index= 0;
        int message= 0;
        
        switch (type) {
            case COMBO_BOX:
                message= User32.CB_GETCURSEL;
                
                break;
        }
        
        if (hwnd != null)
            index= user32.SendMessage(hwnd, message, 0, 0);
        
        System.out.println(String.format("Index selected: %d", index));
        
        return index;
    }
    
    private void registerListeners() {
        if (this.eventsList != null && this.eventsList.size() > 0)
            this.eventsList.forEach(evt -> {
                if (evt instanceof MouseListerImpl)
                    GlobalScreen.addNativeMouseListener((NativeMouseListener) evt);
            });
    }
    
    private void removeListerners() {
        if (this.eventsList != null && this.eventsList.size() > 0)
            this.eventsList.forEach(evt -> {
                if (evt instanceof MouseListerImpl)
                    GlobalScreen.removeNativeMouseListener((NativeMouseListener) evt);
            });
    }
    
    private TypeControl getTypeOfControl(WinDef.HWND hwnd) {
        TypeControl type= null;
        String className= getClassName(hwnd);
        
        if (!className.isEmpty()) {
            className= className.toLowerCase();
            
            if (className.contains("static"))
                type= TypeControl.LABLE;
            
            else if (className.contains("edit"))
                type= TypeControl.TEXT_BOX;
            
            else if (className.contains("button"))
                type= TypeControl.BUTTON;
            
            else if (className.contains("combobox"))
                type= TypeControl.COMBO_BOX;
            
            else if (className.contains("listbox"))
                type= TypeControl.LIST_BOX;
        }
        
        return type;
    }
    
    private MouseListerImpl createMouseLister() {
        return new MouseListerImpl((evt) -> {
            Runnable r= () -> {
                if (!this.cmb_application.getSelectionModel().isEmpty() && this.isRecord.get()) {
                    WinDef.HWND application= getWindowHandler(this.cmb_application.
                        getSelectionModel().getSelectedItem());
                    
                    WinDef.HWND currentControl= user32.
                            WindowFromPoint(new POINTByValue(evt.getX(), evt.getY()));
                    
                    this.clearControls(false);
                    
                    if (currentControl != null) {
                        TypeControl type= getTypeOfControl(currentControl);
                        
                        if (type != null) {
                            this.txt_className.setText(getClassName(currentControl));
                            this.txt_id.setText(getNameProperty(currentControl, 
                                    getProcessID(application)));

                            switch (type) {
                                case BUTTON:
                                case LABLE:
                                    this.txt_caption.setText(getText(currentControl, type));

                                    break;

                                default:
                                    this.txt_text.setText(getText(currentControl, type));

                                    break;
                            }
                        }
                    }
                } else 
                    ;
            };
            
            if (!Platform.isFxApplicationThread())
                Platform.runLater(r);
            
            else
                r.run();
        });
    }
    
    // section of event handlers
    
    @FXML
    private void handleBtn_updateAction(ActionEvent evt) {
        this.cmb_application.setItems(getListOfApps());
    }
    
    @FXML
    private void handleBtn_clear(ActionEvent evt) {
        this.clearControls(true);
    }
    
    @FXML
    private void handlerBtn_record(ActionEvent evt) {
        Stage stage= null;
        
        this.isRecord.set(true);
        this.eventsList.add(this.createMouseLister());
        this.registerListeners();
        
        stage= (Stage) this.gpn_formContainer.getScene().getWindow();
        
        if (stage != null)
            stage.setIconified(true);
    }
    
    @FXML
    private void handleBtn_cancel(ActionEvent evt) {
        this.isRecord.set(false);
        this.removeListerners();
    }
}