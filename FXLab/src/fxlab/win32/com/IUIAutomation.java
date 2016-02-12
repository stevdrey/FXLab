/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.com;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;

/**
 *
 * @author srey
 */
public interface IUIAutomation extends IUnknown {
    @VTID(5)
    public HRESULT GetRootElement(PointerByReference elt);
    
    @VTID(6)
    public HRESULT ElementFromHandle(HWND hwnd, PointerByReference element);
    
    @VTID(7)
    public HRESULT ElementFromPoint(POINT pt, PointerByReference element);
    
    @VTID(8)
    public HRESULT GetFocusedElement(PointerByReference elt);
    
    @VTID(9)
    public HRESULT CreateTreeWalker(Pointer uiaCondition, PointerByReference walker);
    
    @VTID(21)
    public HRESULT CreateTrueCondition(PointerByReference v);
    
    @VTID(22)
    public HRESULT CreateFalseCondition(PointerByReference v);
    
    @VTID(23)
    public HRESULT CreatePropertyCondition(int propertyId, Variant value, PointerByReference result);
    
    @VTID(25)
    public HRESULT CreateAndCondition(Pointer c1, Pointer c2, PointerByReference v);
    
    @VTID(28)
    public HRESULT CreateOrCondition(Pointer c1, Pointer c2, PointerByReference v);
    
    @VTID(31)
    public HRESULT CreateNotCondition(Pointer c, PointerByReference v);
}