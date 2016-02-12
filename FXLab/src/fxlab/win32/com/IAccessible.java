/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.com;

import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 *
 * @author srey
 */
@IID("{618736E0-3C3D-11CF-810C-00AA00389B71}")
public interface IAccessible extends IUnknown{
    @VTID(7)
    HRESULT Get_accParent(PointerByReference pdisp);
    
    @VTID(8)
    HRESULT Get_accChildCount(LongByReference pcnt);
    
    @VTID(-5002)
    HRESULT Get_accChild(int childId, PointerByReference cdisp);
    
    @VTID(10)
    HRESULT Get_accName(int childId, PointerByReference pstr);
    
    @VTID(-5004)
    HRESULT Get_accValue(int childId, PointerByReference pstr);
    
    @VTID(-5005)
    HRESULT Get_accDescription(int childId, PointerByReference pstr);
    
    @VTID(-5006)
    HRESULT Get_accRole(int childId, PointerByReference roleId);
    
    @VTID(-5007)    
    HRESULT Get_accState(int childId, PointerByReference stateId);
    
    @VTID(-5008)
    HRESULT Get_accHelp(int childId, PointerByReference pstr);
    
    @VTID(-5009)
    HRESULT Get_accHelpTopic(PointerByReference pstr, int childId, PointerByReference topic);
    
    @VTID(-5010)
    HRESULT Get_accKeyboardShortcut(int childId, PointerByReference pstr);
    
    @VTID(-5011)
    HRESULT Get_accFocus(PointerByReference ptr);
    
    @VTID(-5012)
    HRESULT Get_accSelection(PointerByReference ptr);
    
    @VTID(-5013)
    HRESULT Get_accDefaultAction(int childId, PointerByReference pstr);
    
    @VTID(-5014)
    HRESULT accSelect(long flags, int childId);
    
    @VTID(22)
    HRESULT accLocation(IntByReference pxLeft,
        IntByReference pyTop,
        IntByReference pcxWidth,
        IntByReference pcyHeight,
        Variant varChild);
    
    @VTID(-5016)
    HRESULT accNavigate(long navDir, PointerByReference varStart,
        PointerByReference vv);
    
    @VTID(-5017)
    HRESULT accHitTest(long xLeft, long yTop, PointerByReference res);
    
    @VTID(27)
    HRESULT accDoDefaultAction(int childId);
    
    @VTID(-5003)
    HRESULT Set_accName(int childId, String str);
    
    @VTID(-5004)
    HRESULT Set_accValue(int childId, String str);
}