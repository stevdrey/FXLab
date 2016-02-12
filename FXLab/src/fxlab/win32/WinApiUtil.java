/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import fxlab.win32.struct.LOGFONT;
import fxlab.win32.struct.POINTByValue;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.util.ProxyObject;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import fxlab.win32.enu.FontConstants;
import fxlab.win32.enu.WindowMessagesConstants;
import java.util.stream.Stream;
import javafx.scene.paint.Color;

/**
 *
 * @author srey
 */
public final class WinApiUtil {
    
    /**
     * This method register a new message and return an unique ID.
     * 
     * @param message
     *          String with the name of new message we want to register.
     * 
     * @return 
     *      Return an unique ID for the message registered.
     */
    public static int registerMessage(String message) {
        return User32.INSTANCE_API.RegisterWindowMessage(message);
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
    public static WinNT.HANDLE openProcessShared(IntByReference pid) {
        return Kernel32.INSTANCE_API.OpenProcess(Kernel32.PROCESS_VM_OPERATION | 
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
    public static WinNT.HANDLE allocateMemory(WinNT.HANDLE handle, int size) {
        return Kernel32.INSTANCE_API.VirtualAllocEx(handle, 0, size, 
                        Kernel32.MEM_RESERVE | Kernel32.MEM_COMMIT, Kernel32.PAGE_READWRITE);
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
    public static IntByReference getProcessID(WinDef.HWND hwnd) {
        IntByReference pid= new IntByReference();
        
        if (hwnd != null)
            User32.INSTANCE_API.GetWindowThreadProcessId(hwnd, pid);
        
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
    public static WinDef.HWND getWindowHandler(String name) {
        return User32.INSTANCE_API.FindWindow(null, name);
    }
    
    public static WinDef.HWND getWindowFromPoint(int x, int y) {
        return User32.INSTANCE_API.WindowFromPoint(new POINTByValue(x, y));
    }
    
    /**
     * This method call the WinApi 32 to request the class name of this Window or Control.
     * 
     * @param hwnd
     * @return 
     *      Return the class name of this Window or Control.
     */
    public static String getRealClassName(WinDef.HWND hwnd) {
        char[] buffer= new char[1024];
        String nameControl= "";
        
        if (hwnd != null) {
            User32.INSTANCE_API.RealGetWindowClass(hwnd, buffer, buffer.length);
            nameControl= Native.toString(buffer);
        }
        
        return nameControl;
    }
    
    /**
     * This method call the WinApi 32 to request the class name of this Window or Control.
     * 
     * @param hwnd
     * @return 
     *      Return the class name of this Window or Control.
     */
    public static String getClassName(WinDef.HWND hwnd) {
        char[] buffer= new char[1024];
        String nameControl= "";
        
        if (hwnd != null) {
            User32.INSTANCE_API.GetClassName(hwnd, buffer, buffer.length);
            nameControl= Native.toString(buffer);
        }
        
        return nameControl;
    }
    
    /**
     * This method return the ID of an specific window or control
     * 
     * @param handle
     *          The pointer or handle of window (Control) we want to know
     *          the name property.
     * 
     * @param pid
     *          The number of the Process ID is run the target application.
     * 
     * @param property
     * 
     * @return 
     *      This method return the ID ({@code .Name} property in WinForms) of an 
     *      specific window or control
     */
    public static String getValueProperty(WinDef.HWND handle, IntByReference pid, String property) {
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
            msg= WinApiUtil.registerMessage(property);
        
            // create a process with shared memory.
            processHandle= WinApiUtil.openProcessShared(pid);

            if (processHandle != null) {
                // allocate an space of memory that will contains the result of .Name property
                bufferMen= WinApiUtil.allocateMemory(processHandle, size + 1);

                if (bufferMen != null) {
                    // send the message to target application and specific window (Control)
                    retLength= User32.INSTANCE_API.SendMessage(handle, msg, size + 1, bufferMen);

                    // read the shared memory and get the value of .name property.
                    retVal= Kernel32.INSTANCE_API.ReadProcessMemory(processHandle, bufferMen, 
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
                retVal= Kernel32.INSTANCE_API.VirtualFreeEx(processHandle, bufferMen, 0, 
                        Kernel32.MEM_RELEASE);
            
                if (!retVal)
                    System.err.println(String.format("Error message free allocate memory: %s", 
                            Kernel32Util.getLastErrorMessage()));
            }
        }
        
        return result;
    }
    
    public static LOGFONT getFontWindow(WinDef.HWND hwnd) {
        LOGFONT lFont= new LOGFONT();
        WinNT.HANDLE hgdiobj= null;
        WinDef.HDC hdc= User32.INSTANCE_API.GetDC(hwnd);
        WinDef.LRESULT lresult= User32.INSTANCE_API.SendMessage(hwnd, 
                WindowMessagesConstants.WM_GETFONT.getValue(), 0, 0);
                
        if (lresult != null) {            
            if (lresult.intValue() == 0) {
                hgdiobj= UxTheme.INSTANCE_API.OpenThemeData(hwnd, 
                        WinApiUtil.getClassName(hwnd));
                
                if (hgdiobj != null) {
                    WinNT.HRESULT hresult= UxTheme.INSTANCE_API.GetThemeFont(hgdiobj, hdc, 
                            1, 3, 210, lFont);
                    
                    if (hresult.intValue() != 0) {
                            lFont= null;

                            System.err.println(String.format("Error when try to get Font of specific Window: %s%n",
                                Kernel32Util.getLastErrorMessage()));
                        }
                    }

                    UxTheme.INSTANCE_API.CloseThemeData(hgdiobj);
                }
                else {
                    hgdiobj= UxTheme.INSTANCE_API.OpenThemeData(hwnd, 
                            WinApiUtil.getClassName(hwnd));

                    if (hgdiobj == null)
                        System.err.println(String.format("Error when try to get Font of specific Window: %s%n",
                                Kernel32Util.getLastErrorMessage()));

                    UxTheme.INSTANCE_API.CloseThemeData(hgdiobj);

                    hgdiobj= new WinNT.HANDLE(lresult.toPointer());

                    GDI32.INSTANCE_API.SelectObject(hdc, hgdiobj);
                    int size= GDI32.INSTANCE_API.GetObject(hgdiobj, lFont.size(), lFont);

                    if (size == 0) {
                        System.err.println(String.format("Error when try to get Font of specific Window: %s%n",
                                Kernel32Util.getLastErrorMessage()));

                        lFont= null;
                    }
                }
            }

            User32.INSTANCE_API.ReleaseDC(hwnd, hdc);

            return lFont;
        }

        public static String getFontFamily(WinDef.HWND hwnd) {
            String fontFamily= "";
            char[] buffer= new char[FontConstants.LF_FULLFACESIZE.getValue()];
            WinDef.HDC hdc= User32.INSTANCE_API.GetDC(hwnd);

            if (GDI32.INSTANCE_API.GetTextFace(hdc, buffer.length, buffer) > 0)
                fontFamily= Native.toString(buffer);

            else
                System.err.println(String.format("Error when try to get Font Family Name: %s%n",
                        Kernel32Util.getLastErrorMessage()));

            User32.INSTANCE_API.ReleaseDC(hwnd, hdc);

            return fontFamily;
        }

        public static String getRoleName(WinDef.HWND hWnd) {
            PointerByReference ptr= new PointerByReference();
            StringBuilder role= new StringBuilder();
            WinNT.HRESULT hresult= Oleacc.INSTANCE_API.AccessibleObjectFromWindow(hWnd, 
                    0, new Guid.IID("{618736E0-3C3D-11CF-810C-00AA00389B71}"), ptr);

            if (COMUtils.SUCCEEDED(hresult)) {
                Accessible acc= new Accessible(ptr.getValue());
                
                Variant.VARIANT roleId= new Variant.VARIANT();
                Variant.VARIANT variant= new Variant.VARIANT();
                variant._variant.__variant.iVal= new WinDef.SHORT(Variant.VT_I4);
                variant._variant.__variant.lVal= new WinDef.LONG(0);

                System.out.println(String.format("iVal: %d, lVal: %d", variant._variant.__variant.iVal.intValue(), 
                        variant._variant.__variant.lVal.intValue()));
                
                hresult= acc.get_accRole(variant, roleId);
                
                if (hresult != null && COMUtils.SUCCEEDED(hresult)) {
                    char[] buff= new char[1024];
                    
                    int res= Oleacc.INSTANCE_API.GetRoleText(roleId._variant.__variant.lVal.intValue(), 
                            buff, buff.length);
                    
                    if (COMUtils.SUCCEEDED(res))
                        role.append(Native.toString(buff));
                }
                    
            }
        
        return role.toString();
    }
    
    public static Color getBackground(WinDef.HWND hWnd) {
        Color background= null;
        WinDef.HDC hdc= User32.INSTANCE_API.GetWindowDC(hWnd);
                
        background= Color.valueOf(String.valueOf(GDI32.INSTANCE_API.GetBkColor(hdc)));
        
        User32.INSTANCE_API.ReleaseDC(hWnd, hdc);
        
        return background;
    }
}
