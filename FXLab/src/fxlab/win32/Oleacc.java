/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 *
 * @author srey
 */
public interface Oleacc extends StdCallLibrary {
    Oleacc INSTANCE_API= (Oleacc) Native.loadLibrary("oleacc.dll", Oleacc.class, 
            W32APIOptions.DEFAULT_OPTIONS);
    
    /**
     * Retrieves the address of the specified interface for the object associated with the specified window.
     * 
     * @param win
     *          Specifies the handle of a window for which an object is to be retrieved. 
     *          To retrieve an interface pointer to the cursor or caret object, 
     *          specify NULL and use the appropriate object ID in dwObjectID.
     * 
     * @param objID
     *          Specifies the object ID. 
     *          This value is one of the standard object identifier constants or a custom object ID 
     *          such as OBJID_NATIVEOM, which is the object ID for the Office native object model. 
     *          For more information about OBJID_NATIVEOM, see the Remarks section in this topic.
     * 
     * @param iid
     *          Specifies the reference identifier of the requested interface. 
     *          This value is either IID_IAccessible or IID_IDispatch, but it can also be IID_IUnknown, 
     *          or the IID of any interface that the object is expected to support.
     * 
     * @param ptr
     *          Address of a pointer variable that receives the address of the specified interface.
     * 
     * @return 
     *      If successful, returns S_OK.
     *      If not successful, returns one of the following or another standard COM error code.
     */
    HRESULT AccessibleObjectFromWindow(HWND win, int objID, Guid.IID iid, PointerByReference ptr);

    /**
     * Retrieves the child ID or {@link IDispatch} of each child within an accessible container object.
     * 
     * @param pAccPointer
     *          Pointer to the container object's {@link IAccessible} interface.
     * 
     * @param startFrom
     *          Specifies the zero-based index of the first child that is retrieved. 
     *          This parameter is an index, not a child ID, and it is usually is set to zero (0).
     * 
     * @param childCount
     *          Specifies the number of children to retrieve. To retrieve the current number of children, 
     *          an application calls IAccessible::get_accChildCount.
     * 
     * @param tableRef
     *          Pointer to an array of VARIANT structures that receives information about the container's children. 
     *          If the vt member of an array element is VT_I4, then the lVal member for that element is the child ID. 
     *          If the vt member of an array element is VT_DISPATCH, then the pdispVal member for that element 
     *          is the address of the child object's IDispatch interface.
     * 
     * @param returnCount
     *          Address of a variable that receives the number of elements in the 
     *          rgvarChildren array that is populated by the AccessibleChildren function. 
     *          This value is the same as that of the cChildren parameter; however, 
     *          if you request more children than exist, this value will be less than that of cChildren.
     * 
     * @return 
     *      If successful, returns S_OK.
     *      If not successful, returns one of the following or another standard COM error code.
     */
    HRESULT AccessibleChildren(Pointer pAccPointer, int startFrom, long childCount, 
            Variant.VARIANT tableRef, LongByReference returnCount);

    /**
     * Retrieves the window handle that corresponds to a particular instance of an IAccessible interface.
     * 
     * @param pointer
     *          Pointer to the IAccessible interface whose corresponding window handle will be retrieved. 
     *          This parameter must not be NULL.
     * 
     * @param ptr
     *          Address of a variable that receives a handle to the window containing the object specified in pacc. 
     *          If this value is NULL after the call, the object is not contained within a window; 
     *          for example, the mouse pointer is not contained within a window.
     * 
     * @return 
     *      If successful, returns S_OK.
     *      If not successful, returns the following or another standard COM error code.
     */
    int WindowFromAccessibleObject(Pointer pointer, PointerByReference ptr);

    /**
     * Retrieves the localized string that describes the object's role for the specified role value.
     * 
     * @param roleId
     *          One of the object role constants.
     * 
     * @param buff
     *          Address of a buffer that receives the role text string. 
     *          If this parameter is NULL, the function returns the role string's length, 
     *          not including the null character.
     * 
     * @param i
     *          The size of the buffer that is pointed to by the lpszRole parameter.
     *          For ANSI strings, this value is measured in bytes; for Unicode strings, it is measured in characters.
     * 
     * @return 
     *      If successful, and if lpszRole is non-NULL, the return value is the number of bytes (ANSI strings) or characters (Unicode strings) copied into the buffer, 
     *      not including the terminating null character. If lpszRole is NULL, the return value represents the string's length, not including the null character.
     * 
     *      If the string resource does not exist, or if the lpszRole parameter is not a valid pointer, the return value is zero (0). 
     *      To get extended error information, call GetLastError.
     */
    int GetRoleText(int roleId, char[] buff, int i);
}