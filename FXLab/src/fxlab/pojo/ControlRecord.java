/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.pojo;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import fxlab.ui.enu.TypeControl;
import fxlab.win32.COMBOBOXINFOByReference;
import fxlab.win32.Kernel32;
import fxlab.win32.LOGFONT;
import fxlab.win32.User32;
import fxlab.win32.WinApiUtil;
import fxlab.win32.WinEventProc;
import fxlab.win32.enu.ComboBoxConstants;
import fxlab.win32.enu.ConstantsMessages;
import fxlab.win32.enu.EventConstants;
import fxlab.win32.enu.ObjectStateConstants;
import fxlab.win32.enu.WinEventConstants;
import fxlab.win32.enu.WindowMessagesConstants;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This object store some information of current Window (Control) that user was selected
 * or is viewing.
 * 
 * @author srey
 */
public class ControlRecord {
    private WinDef.HWND handle= null;
    private WinDef.HWND parent= null;
    private ObservableList<WinNT.HANDLE> eventListeners= null;
    private User32 user32= null;
    private Kernel32 kernel32= null;
    
    private StringProperty className= null;
    private StringProperty realClassName= null;
    private StringProperty text= null;
    private StringProperty id= null;
    
    private ObjectProperty<FontControl> fontControl= null;

    public ControlRecord(WinDef.HWND parent, WinDef.HWND handle) {
        super();
        
        this.user32= User32.INSTANCE_API;
        this.kernel32= Kernel32.INSTANCE_API;
        this.parent= parent;
        this.handle= handle;
        this.eventListeners= FXCollections.observableArrayList();
        
        this.registerEvents();
    }
    
    /**
     * This method get the class name of the Window (Control) represent this Object.
     * 
     * @return 
     *      Returns the real Class name.
     */
    public String getClassName() {
        if (this.className == null) 
            this.className= new SimpleStringProperty(this, "className", 
                    WinApiUtil.getClassName(this.handle));
                
        return this.className.getValueSafe();
    }
    
    public String getRealClassName() {
        if (this.realClassName == null)
            this.realClassName= new SimpleStringProperty(this, "realClassName", 
                    WinApiUtil.getRealClassName(this.handle));
        
        return this.realClassName.getValueSafe();
    }
    
    public String getText() {
        this.updateText();
        
        return this.text.getValueSafe();
    }
    
    public String getId() {
        if (this.id == null)
            this.id= new SimpleStringProperty(this, "id", 
                    WinApiUtil.getNameProperty(this.handle, WinApiUtil.getProcessID(this.parent)));
        
        return this.id.getValueSafe();
    }

    public WinDef.HWND getHandle() {
        return handle;
    }
    
    public FontControl getFontControl() {
        if (this.fontControl == null) {
            FontControl fc= null;
            LOGFONT lfont= WinApiUtil.getFontWindow(this.handle);
            
            if (lfont != null)
                fc= new FontControl(Native.toString(lfont.lfFaceName), lfont.lfHeight, 
                        lfont.lfWidth, lfont.lfOrientation, lfont.lfWeight, 
                        lfont.lfItalic.byteValue() != 0, lfont.lfStrikeOut.byteValue() != 0, 
                        lfont.lfUnderline.byteValue() != 0);
            
            else
                fc= new FontControl();
            
            this.fontControl= new SimpleObjectProperty<>(this, "fontControl", fc);
        }
        
        return this.fontControl.getValue();
    }
    
    public void dispose() {
        if (this.eventListeners != null && this.eventListeners.size() > 0) {
            this.eventListeners.forEach(hnd -> {
                boolean result= user32.UnhookWinEvent(hnd);
                
                if (!result)
                    System.err.println(String.format("Error when try to remove listener: %s", 
                            Kernel32Util.getLastErrorMessage()));
            });
            this.eventListeners.clear();
        } 
    }
    
    public StringProperty textProperty() {
        if (this.text == null)
            this.text= new SimpleStringProperty(this, "text", getText(getTypeOfControl()));
        
        return this.text;
    }
    
    public boolean isComboBox() {
        TypeControl type= this.getTypeOfControl();
        
        return type != null && type == TypeControl.COMBO_BOX;
    }
    
    public boolean isShowListBox(WinDef.HWND hwnd) {
        COMBOBOXINFOByReference cbi= new COMBOBOXINFOByReference();
        
        user32.SendMessage(this.handle, 
                ComboBoxConstants.CB_GETCOMBOBOXINFO.getValue(), 0, cbi);
        
        if (cbi.hwndList != null)
            return  this.user32.IsWindowVisible(cbi.hwndList) || 
                    (ObjectStateConstants.fromValue(cbi.stateButton) == ObjectStateConstants.STATE_SYSTEM_PRESSED) || 
                    cbi.hwndList.equals(hwnd);
        
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ControlRecord)
            return ((ControlRecord) obj).handle.equals(this.handle);
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.handle);
        hash = 73 * hash + Objects.hashCode(this.parent);
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.className);
        hash = 73 * hash + Objects.hashCode(this.realClassName);
        return hash;
    }
    
    // section of private method
    
    /**
     * This method gets the type of control base on its class name.
     * 
     * @return 
     *      Return the type of control base on its class name.
     */
    private TypeControl getTypeOfControl() {
        TypeControl type= null;
        String className= this.getClassName();
        
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
    
    /**
     * This method get the size or length of text/caption of specific Window (Control)
     * 
     * @param type
     *          Enum indicate the type of control we want to know text or caption
     *          of specific Window (Control).
     * 
     * @return 
     *      Return the size or length of text/caption of specific Window (Control).
     */
    private int getTextLength(TypeControl type) {
        int length= 0;
        int message= 0;
        
        if (this.handle != null && type != null) {
            switch (type) {
                case COMBO_BOX:
                    message= ComboBoxConstants.CB_GETLBTEXTLEN.getValue();

                    break;

                case LIST_BOX:
                    message= User32.LB_GETTEXTLEN;

                    break;

                default:
                    message= WindowMessagesConstants.WM_GETTEXTLENGTH.getValue();

                    break;
            }
            
            length= user32.SendMessage(this.handle, message, 0, 0).intValue() + 1;
        }
        
        return length;
    }
    
    /**
     * This method get the caption or the text value in a specific Window (Control),
     * depends of its class name or type of Window (Control).
     * 
     * @param type 
     *          Enum indicate the type of control we want to know text or caption
     *          of specific Window (Control).
     * 
     * @return 
     *      Return the text value or caption, depends of its class name or type.
     */
    private String getText(TypeControl type) {
        String text= "";
        int size= getTextLength(type);
        int message= 0;
        char[] buffer= new char[size == 0 ? 1024 : size];
        
        if (this.handle != null && type != null) {
            switch (type) {
                case COMBO_BOX:
                    message= ComboBoxConstants.CB_GETLBTEXT.getValue();
                    
                    break;
                    
                case LIST_BOX:
                    message= User32.LB_GETTEXT;
                    
                    break;
                    
                default:
                    message= WindowMessagesConstants.WM_GETTEXT.getValue();
                    
                    break;
            }
            
            switch (type) {
                case LIST_BOX:
                    user32.SendMessage(this.handle, message, this.getSelectedIndex(type), 0);
                    break;
                    
                case COMBO_BOX:
                    user32.SendMessage(handle, message, this.getSelectedIndex(type), buffer);
                    break;
                    
                default:
                    user32.SendMessage(this.handle, message, buffer.length, buffer);
                    break;
            }
            
            text= Native.toString(buffer);
        }
        
        return text;
    }
    
    /**
     * This method get the actual index selected of Window (Control) thats type is:
     * Combo-Box or ListBox.
     * 
     * @param type
     *          Means the type of Window we want to gets the current index selected.
     * 
     * @return 
     *      Return the actual index selected of Window (Control) thats type is:
     *      Combo-Box or ListBox.
     */
    private int getSelectedIndex(TypeControl type) {
        int index= 0;
        int message= 0;
        
        if (type != null) {
            switch (type) {
                case COMBO_BOX:
                    message= ComboBoxConstants.CB_GETCURSEL.getValue();

                    break;
            }

            if (this.handle != null)
                index= user32.SendMessage(this.handle, message, 0, 0).intValue();
        }
        
        return index;
    }
    
    /**
     * This method update the current value text of Window (Control).
     */
    private void updateText() {
        if (this.text == null)
            this.text= new SimpleStringProperty(this, "text", getText(getTypeOfControl()));
        
        else
            this.text.setValue(getText(getTypeOfControl()));
    }
    
    private void registerEvents() {
        TypeControl type= this.getTypeOfControl();
        
        if (type != null) {
            this.eventListeners.
                            add(this.user32.SetWinEventHook(EventConstants.EVENT_OBJECT_VALUECHANGE.getValue(), 
                                    EventConstants.EVENT_OBJECT_VALUECHANGE.getValue(), 
                                    null, this.createChangeEvent(), 
                                    WinApiUtil.getProcessID(this.parent).getValue(), 0, 
                                    WinEventConstants.WINEVENT_OUTOFCONTEXT.getValue())
                            );
        }
    }
    
    private WinEventProc createChangeEvent() {
        return (WinNT.HANDLE hWinEventHook, int event, WinDef.HWND hwnd, int idObject, 
                int idChild, int dwEventThread, int dwmsEventTime) -> {
            EventConstants evt= EventConstants.fromInt(event);
            
            if (evt == EventConstants.EVENT_OBJECT_VALUECHANGE)
                this.updateText();
        };
    }
    
    private WinEventProc createLostFocusEvent() {
        return (WinNT.HANDLE hWinEventHook, int event, WinDef.HWND hwnd, int idObject, 
                int idChild, int dwEventThread, int dwmsEventTime) -> {
            
            // just lookup when lost focus
            if (!this.handle.equals(hwnd)) {
                EventConstants evt= EventConstants.fromInt(event);
            
                if (evt == EventConstants.EVENT_SYSTEM_FOREGROUND) 
                    this.updateText();
            }
        };
    }
}