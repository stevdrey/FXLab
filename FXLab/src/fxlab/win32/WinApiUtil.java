/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

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
}
