/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.cb;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

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
 *
 * @author srey
 */
@FunctionalInterface
public interface PropEnumProcEx extends StdCallLibrary.StdCallCallback {
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
        public boolean callback(WinDef.HWND hwnd, WinDef.ATOM lpszString, WinNT.HANDLE hData, BaseTSD.ULONG_PTR dwData);
}
