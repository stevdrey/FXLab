/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;

/**
 *
 * @author srey
 */
public interface WINAPI extends User32{
    WINAPI INSTANCE_API= (WINAPI) Native.loadLibrary("user32", WINAPI.class, W32APIOptions.DEFAULT_OPTIONS);
    
    int SendMessageW(HWND hWnd, int msg, int wParam, byte[] lParam);
    
    HWND FindWindowEx(HWND parent, HWND child, String className, String window);
    
    /**
     * Retrieves a string that specifies the window type.
     * 
     * @param hwnd
     *          A handle to the window whose type will be retrieved.
     * 
     * @param pszType
     *          A pointer to a string that receives the window type.
     * 
     * @param cchType
     *          The length, in characters, of the buffer pointed to by the pszType parameter.
     * 
     * @return 
     *  If the function succeeds, the return value is the number of characters copied to the specified buffer.
     *  If the function fails, the return value is zero. To get extended error information, call GetLastError.
     */
    int RealGetWindowClass(HWND hwnd, char[] pszType, int cchType);
    
    /**
     * Sends the specified message to a window or windows. The SendMessage function calls the window 
     * procedure for the specified window and does not return until the window procedure has processed the message.
     * To send a message and return immediately, use the SendMessageCallback or SendNotifyMessage function. 
     * To post a message to a thread's message queue and return immediately, use the PostMessage or PostThreadMessage function.
     * 
     * @param hWnd
     *          A handle to the window whose window procedure will receive the message. If this parameter is HWND_BROADCAST ((HWND)0xffff), 
     *          the message is sent to all top-level windows in the system, including disabled or invisible unowned windows, overlapped windows, 
     *          and pop-up windows; but the message is not sent to child windows.
     * 
     *          Message sending is subject to UIPI. The thread of a process can send messages only to message queues of threads in 
     *          processes of lesser or equal integrity level.
     * 
     * @param Msg
     *         The message to be sent.
     *         For lists of the system-provided messages, see System-Defined Messages.
     * 
     * @param wParam
     *          Additional message-specific information.
     * 
     * @param lParam
     *          Additional message-specific information.
     * 
     * @return 
     *      The return value specifies the result of the message processing; it depends on the message sent.
     */
    LRESULT SendMessage(HWND hWnd, int Msg, IntByReference wParam, IntByReference lParam);
    
    int SendMessage(HWND hWnd, int Msg, int wParam, HANDLE lParam);
    
    /**
     * Enumerates all entries in the property list of a window by passing them, one by one, to the specified callback function. 
     * EnumPropsEx continues until the last entry is enumerated or the callback function returns FALSE.
     * 
     * @param hWnd
     *      A handle to the window whose property list is to be enumerated.
     * 
     * @param lpEnumFunc
     *      A pointer to the callback function. For more information about the callback function, see the PropEnumProcEx function.
     * 
     * @param lparam
     *      Application-defined data to be passed to the callback function.
     * 
     * @return 
     *      The return value specifies the last value returned by the callback function. 
     *      It is -1 if the function did not find a property for enumeration.
     */
    int EnumPropsEx(HWND hWnd, PropEnumProcEx lpEnumFunc, LPARAM lparam);
    
    int EnumProps(HWND hWnd, PropEnumProc lpEnumFunc);
    
    /**
     * The PROPENUMPROCEX type defines a pointer to this callback function.
     * 
     * The following restrictions apply to this callback function:
     * 
     * <ul>
     *  <li>The callback function must not yield control or do anything that might yield control to other tasks.</li>
     *  <li>The callback function can call the RemoveProp function. However, RemoveProp can remove only the property passed to the callback function through the callback function's parameters.</li>
     *  <li>The callback function should not attempt to add properties.</li>
     * </ul>
     */
    public static interface PropEnumProcEx extends StdCallCallback {
        /**
         * Application-defined callback function used with the EnumPropsEx function.
         * The function receives property entries from a window's property list. 
         * The PROPENUMPROCEX type defines a pointer to this callback function. 
         * PropEnumProcEx is a placeholder for the application-defined function name.
         * 
         * @param hwnd
         *          A handle to the window whose property list is being enumerated.
         * 
         * @param lpszString
         *          The string component of a property list entry. 
         *          This is the string that was specified, along with a data handle, 
         *          when the property was added to the window's property list via a call to the SetProp function.
         * 
         * @param hData
         *          A handle to the data. This handle is the data component of a property list entry.
         * 
         * @param dwData
         *          Application-defined data. This is the value that was specified as the lParam parameter of the call to EnumPropsEx that initiated the enumeration.
         * 
         * @return 
         *      Return TRUE to continue the property list enumeration.
         *      Return FALSE to stop the property list enumeration.
         */
        public boolean callback(HWND hwnd, WString lpszString, HANDLE hData, ULONG_PTR dwData);
    }
    
    public static interface PropEnumProc extends StdCallCallback {
        public boolean callback(HWND hwnd, String lpszString, HANDLE hData);
    }
}
