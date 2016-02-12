/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import fxlab.win32.struct.POINTByValue;
import fxlab.win32.struct.COMBOBOXINFOByReference;
import com.sun.jna.Native;
import com.sun.jna.WString;
import com.sun.jna.win32.W32APIOptions;
import fxlab.win32.cb.PropEnumProcEx;
import fxlab.win32.cb.WinEventProc;
import fxlab.win32.enu.AncestorConstants;
import fxlab.win32.enu.DeviceContextConstants;
import fxlab.win32.enu.EventConstants;
import fxlab.win32.enu.SystemParamInfoConstants;
import fxlab.win32.enu.WinEventConstants;
import fxlab.win32.struct.NONCLIENTMETRICS;

/**
 * This Interface handles all the native call of methods in User32.h of Win32Api
 * 
 * @author srey
 */
public interface User32 extends com.sun.jna.platform.win32.User32{
    /**
     * Singleton instance of this Interface
     */
    User32 INSTANCE_API= (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
    
    /**
     * Constant for gets the length of a string in a list box.
     */
    final int LB_GETTEXTLEN = 0x018A;
    
    /**
     * Constant for gets a string from a list box.
     */
    final int LB_GETTEXT = 0x0189;
    
    /**
     * Does not skip any child windows
     */
    final int CWP_ALL= 0x0000;
    
    /**
     * Retrieves a handle to a window whose class name and window name match the specified strings. 
     * The function searches child windows, beginning with the one following the specified child window. 
     * This function does not perform a case-sensitive search.
     * 
     * @param parent
     *          A handle to the parent window whose child windows are to be searched.
     *          If hwndParent is NULL, the function uses the desktop window as the parent window. 
     *          The function searches among windows that are child windows of the desktop.
     *          If hwndParent is HWND_MESSAGE, the function searches all message-only windows.
     * 
     * @param child
     *          A handle to a child window. The search begins with the next child window in the Z order. 
     *          The child window must be a direct child window of hwndParent, not just a descendant window.
     *          If hwndChildAfter is NULL, the search begins with the first child window of hwndParent.
     *          Note that if both hwndParent and hwndChildAfter are NULL, the function searches all top-level and message-only windows.
     * 
     * @param className
     *          The class name or a class atom created by a previous call to the RegisterClass or RegisterClassEx function. 
     *          The atom must be placed in the low-order word of lpszClass; the high-order word must be zero.
     *          If lpszClass is a string, it specifies the window class name. 
     *          The class name can be any name registered with RegisterClass or RegisterClassEx, or any of the predefined control-class names, or it can be MAKEINTATOM(0x8000). 
     *          In this latter case, 0x8000 is the atom for a menu class. For more information, see the Remarks section of this topic.
     * 
     * @param window
     *          The window name (the window's title). If this parameter is NULL, all window names match.
     * 
     * @return 
     *      If the function succeeds, the return value is a handle to the window that has the specified class and window names.
     *      If the function fails, the return value is NULL. To get extended error information, call GetLastError.
     */
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
    int SendMessage(HWND hWnd, int Msg, int wParam, HANDLE lParam);
    
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
     * @param msg 
     *         The message to be sent.
     *         For lists of the system-provided messages, see System-Defined Messages.
     * 
     * @param wParam
     *          Additional message-specific information.
     * 
     * @param cbi
     *          Additional message-specific information.
     * 
     * @return 
     *      The return value specifies the result of the message processing; it depends on the message sent.
     */   
    boolean SendMessage(HWND hWnd, int msg, int wParam, COMBOBOXINFOByReference cbi);
    
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
    LRESULT SendMessage(HWND hWnd, int Msg, int wParam, int lParam);
    
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
    int SendMessage(HWND hWnd, int Msg, int wParam, char[] lParam);
    
    /**
     * Retrieves a handle to the child window at the specified point. 
     * The search is restricted to immediate child windows; grandchildren and deeper 
     * descendant windows are not searched.
     * 
     * @param hwndParent
     *          A handle to the window whose child is to be retrieved.
     * 
     * @param point
     * 
     * @return 
     *      The return value is a handle to the child window that contains the specified point.
     */
    HWND RealChildWindowFromPoint(HWND hwndParent, POINTByValue point);
    
    /**
     * Retrieves a handle to the window that contains the specified point.
     * 
     * @param Point
     *          The point to be checked.
     * 
     * @return 
     *      The return value is a handle to the window that contains the point. 
     *      If no window exists at the given point, the return value is NULL. 
     *      If the point is over a static text control, the return value is a handle to the window under the static text control.
     */
    HWND WindowFromPoint(POINTByValue Point);
        
    /**
     * Determines which, if any, of the child windows belonging to the specified parent window contains the specified point. 
     * The function can ignore invisible, disabled, and transparent child windows. 
     * The search is restricted to immediate child windows. 
     * Grandchildren and deeper descendants are not searched.
     * 
     * @param hwndParent
     *          A handle to the parent window.
     * 
     * @param point
     * 
     * @param uFlags
     *          The child windows to be skipped.
     * 
     * @return 
     *      The return value is a handle to the first child window that contains the point and meets the criteria specified by uFlags. 
     *      If the point is within the parent window but not within any child window that meets the criteria, the return value is a handle to the parent window. 
     *      If the point lies outside the parent window or if the function fails, the return value is NULL.
     */
    HWND ChildWindowFromPointEx(HWND hwndParent, POINTByValue point, int uFlags);
    
    /**
     * Retrieves a handle to the window that contains the specified physical point.
     * 
     * @param point
     *          The physical coordinates of the point.
     * 
     * @return 
     *      A handle to the window that contains the given physical point. If no window exists at the point, this value is NULL.
     */
    HWND WindowFromPhysicalPoint(POINTByValue point);
    
    /**
     * Sets an event hook function for a range of events.
     * 
     * This function allows clients to specify which processes and threads they are interested in.
     * If the idProcess parameter is nonzero and idThread is zero, the hook function receives the specified events from all threads in that process. 
     * If the idProcess parameter is zero and idThread is nonzero, the hook function receives the specified events only from the thread specified by idThread. 
     * If both are zero, the hook function receives the specified events from all threads and processes.
     * 
     * Clients can call SetWinEventHook multiple times if they want to register additional hook functions or listen for additional events.
     * The client thread that calls SetWinEventHook must have a message loop in order to receive events.
     * 
     * @param eventMin
     *          Specifies the event constant for the lowest event value in the range of events ({@link EventConstants}) that are handled by the hook function. 
     *          This parameter can be set to EVENT_MIN to indicate the lowest possible event value.
     * 
     * @param eventMax
     *          Specifies the event constant for the lowest event value in the range of events ({@link EventConstants}) that are handled by the hook function. 
     *          This parameter can be set to EVENT_MAX to indicate the highest possible event value.
     * 
     * @param hmodWinEventProc
     *          Handle to the DLL that contains the hook function at lpfnWinEventProc, 
     *          if the WINEVENT_INCONTEXT (see {@link WinEventConstants}) flag is specified in the dwFlags parameter. 
     *          If the hook function is not located in a DLL, 
     *          or if the WINEVENT_OUTOFCONTEXT (see {@link WinEventConstants}) flag is specified, this parameter is NULL.
     * 
     * @param lpfnWinEventProc
     *          Pointer to the event hook function. 
     *          For more information about this function, see {@link WinEventProc.}
     * 
     * @param idProcess
     *          Specifies the ID of the process from which the hook function receives events. 
     *          Specify zero (0) to receive events from all processes on the current desktop.
     * 
     * @param idThred
     *          Specifies the ID of the thread from which the hook function receives events. 
     *          If this parameter is zero, the hook function is associated with all existing threads on the current desktop.
     * 
     * @param dwflags
     *          Flag values that specify the location of the hook function and of the events to be skipped
     *          see {@link WinEventConstants}
     * 
     * @return 
     *      If successful, returns an {@code HANDLE} value that identifies this event hook instance. 
     *      Applications save this return value to use it with the UnhookWinEvent function.
     *      If unsuccessful, returns zero.
     */
    HANDLE SetWinEventHook(int eventMin, int eventMax, HMODULE hmodWinEventProc, WinEventProc lpfnWinEventProc, 
            int idProcess, int idThred, int dwflags);
    
    /**
     * Removes an event hook function created by a previous call to {@link SetWinEventHook}.
     * 
     * @param hWinEventHook
     *          Handle to the event hook returned in the previous call to {@link SetWinEventHook}.
     * 
     * @return 
     *      If successful, returns TRUE; otherwise, returns FALSE.
     */
    boolean UnhookWinEvent(HANDLE hWinEventHook);
    
    /**
     * Determines whether a window is a child window or descendant window of a specified parent window. 
     * A child window is the direct descendant of a specified parent window if that parent window is in the chain of parent windows; 
     * the chain of parent windows leads from the original overlapped or pop-up window to the child window.
     * 
     * @param hWndParent
     *          A handle to the parent window.
     * 
     * @param hWnd
     *          A handle to the window to be tested.
     * 
     * @return 
     *      If the window is a child or descendant window of the specified parent window, the return value is nonzero.
     *      If the window is not a child or descendant window of the specified parent window, the return value is zero
     */
    boolean IsChild(HWND hWndParent, HWND hWnd);
    
    /**
     * Retrieves the handle to the ancestor of the specified window.
     * 
     * @param hwnd
     *          A handle to the window whose ancestor is to be retrieved. 
     *          If this parameter is the desktop window, the function returns NULL.
     * 
     * @param gaFlags
     *          The ancestor to be retrieved. This parameter can be one of the {@link AncestorConstants} values.
     * 
     * @return 
     *      The return value is the handle to the ancestor window.
     */
    HWND GetAncestor(HWND hwnd, int gaFlags);
    
    /**
     * Retrieves a handle to the specified window's parent or owner.
     * To retrieve a handle to a specified ancestor, use the {@link GetAncestor} function.
     * 
     * @param hwnd
     *          A handle to the window whose parent window handle is to be retrieved.
     * 
     * @return 
     *      If the window is a child window, the return value is a handle to the parent window. 
     *      If the window is a top-level window with the WS_POPUP style, the return value is a handle to the owner window.
     * 
     *      If the function fails, the return value is NULL. To get extended error information, call GetLastError.
     */
    HWND GetParent(HWND hwnd);
    
    /**
     * Retrieves a handle to the desktop window. 
     * The desktop window covers the entire screen. 
     * The desktop window is the area on top of which other windows are painted.
     * 
     * @return 
     *      The return value is a handle to the desktop window
     */
    HWND GetDesktopWindow();
    
    /**
     * Retrieves a handle to a window that has the specified relationship (Z-Order or owner) to the specified window.
     * 
     * @param hWnd
     *          A handle to a window. The window handle retrieved is relative to this window, based on the value of the uCmd parameter.
     * 
     * @param uCmd
     *          The relationship between the specified window and the window whose handle is to be retrieved. 
     * 
     * @return 
     *      If the function succeeds, the return value is a window handle. 
     *      If no window exists with the specified relationship to the specified window, the return value is NULL. 
     *      To get extended error information, call GetLastError.
     */
    HWND GetWindow(HWND hWnd, int uCmd);
    
    /**
     * The GetWindowDC function retrieves the device context (DC) for the entire window, 
     * including title bar, menus, and scroll bars. A window device context permits painting anywhere in a window, 
     * because the origin of the device context is the upper-left corner of the window instead of the client area.
     * 
     * GetWindowDC assigns default attributes to the window device context each time 
     * it retrieves the device context. Previous attributes are lost.
     * 
     * @param hWnd
     *          A handle to the window with a device context that is to be retrieved. 
     *          If this value is NULL, GetWindowDC retrieves the device context for the entire screen.
     * 
     *          If this parameter is NULL, GetWindowDC retrieves the device context for the primary display monitor. 
     *          To get the device context for other display monitors, use the EnumDisplayMonitors and CreateDC functions.
     * 
     * @return 
     *      If the function succeeds, the return value is a handle to a device context for the specified window.
     *      If the function fails, the return value is NULL, indicating an error or an invalid hWnd parameter.
     */
    HDC GetWindowDC(HWND hWnd);
    
    /**
     * The GetWindowRgn function obtains a copy of the window region of a window. 
     * The window region of a window is set by calling the SetWindowRgn function. 
     * The window region determines the area within the window where the system permits drawing. 
     * The system does not display any portion of a window that lies outside of the window region
     * 
     * @param hWnd
     *          Handle to the window whose window region is to be obtained.
     * 
     * @param hRgn
     *          Handle to the region which will be modified to represent the window region.
     * 
     * @return 
     *      The return value specifies the type of the region that the function obtains. 
     */
    int GetWindowRgn(HWND hWnd, HRGN hRgn);
    
    /**
     * The GetDCEx function retrieves a handle to a device context (DC) for the client area of 
     * a specified window or for the entire screen. 
     * You can use the returned handle in subsequent GDI functions to draw in the DC. 
     * The device context is an opaque data structure, whose values are used internally by GDI.
     *
     * This function is an extension to the GetDC function, which gives an application 
     * more control over how and whether clipping occurs in the client area.
     * 
     * @param hWnd
     *          A handle to the window whose DC is to be retrieved. 
     *          If this value is NULL, GetDCEx retrieves the DC for the entire screen.
     * 
     * @param hrgnClip
     *          A clipping region that may be combined with the visible region of the DC. 
     *          If the value of flags is DCX_INTERSECTRGN or DCX_EXCLUDERGN, 
     *          then the operating system assumes ownership of the region and will automatically 
     *          delete it when it is no longer needed. 
     *          In this case, the application should not use or delete the region 
     *          after a successful call to GetDCEx.
     * 
     * @param flags
     *          Specifies how the DC is created. 
     *          This parameter can be one or more of the {@link DeviceContextConstants} values.
     * 
     * @return 
     *      If the function succeeds, the return value is the handle to the DC for the specified window.
     *      If the function fails, the return value is NULL. An invalid value for the hWnd parameter will cause the function to fail.
     */
    HDC GetDCEx(HWND hWnd, HRGN hrgnClip, int flags);
    
    /**
     * The WindowFromDC function returns a handle to the window associated with the specified display device context (DC). 
     * Output functions that use the specified device context draw into this window.
     * 
     * @param hdc
     *          Handle to the device context from which a handle to the associated window is to be retrieved.
     * 
     * @return 
     *      The return value is a handle to the window associated with the specified DC. 
     *      If no window is associated with the specified DC, the return value is NULL.
     */
    HWND WindowFromDC(HDC hdc);
    
    /**
     * Retrieves or sets the value of one of the system-wide parameters. 
     * This function can also update the user profile while setting a parameter.
     * 
     * @param uiAction
     *          The system-wide parameter to be retrieved or set. 
     *          The possible values are organized in the {@link SystemParamInfoConstants} values
     * 
     * @param uiParam
     *          A parameter whose usage and format depends on the system parameter being queried or set. 
     *          For more information about system-wide parameters, see the uiAction parameter. 
     *          If not otherwise indicated, you must specify zero for this parameter.
     * 
     * @param pvParam
     *          A parameter whose usage and format depends on the system parameter being queried or set. 
     *          For more information about system-wide parameters, see the uiAction parameter. 
     *          If not otherwise indicated, you must specify NULL for this parameter.
     * 
     * @param fWinIni
     *          If a system parameter is being set, specifies whether the user profile is to be updated, and if so, 
     *          whether the WM_SETTINGCHANGE message is to be broadcast to all top-level windows to notify them of the change.
     * 
     * @return 
     *      If the function succeeds, the return value is a nonzero value.
     *      If the function fails, the return value is zero. To get extended error information, call GetLastError.
     */
    boolean SystemParametersInfo(int uiAction, int uiParam, NONCLIENTMETRICS pvParam, int fWinIni);
    
    /**
     * Retrieves a data handle from the property list of the specified window. 
     * The character string identifies the handle to be retrieved. 
     * The string and handle must have been added to the property list by a previous call to the SetProp function.
     * 
     * @param hwnd
     *          A handle to the window whose property list is to be searched.
     * @param lpString
     *          An atom that identifies a string. 
     *          If this parameter is an atom, it must have been created by using the GlobalAddAtom function. 
     *          The atom, a 16-bit value, must be placed in the low-order word of the lpString parameter; the high-order word must be zero.
     * 
     * @return 
     */
    HANDLE GetProp(HWND hwnd, String lpString);
        
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
    
    public static interface PropEnumProc extends StdCallCallback {
        public boolean callback(HWND hwnd, WString lpszString, HANDLE hData);
    }
}
