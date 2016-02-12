/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

/**
 *
 * @author srey
 */
public class Accessible extends Dispatch {

    public Accessible(Pointer pvInstance) {
        super(pvInstance);
    }

    public Accessible(int pvInstance) {
        super(new Pointer(pvInstance));
    }
    
    public WinNT.HRESULT get_accChildren(LongByReference longByReference){
        return (WinNT.HRESULT)this._invokeNativeObject(8, 
                new Object[]{ 
                    this.getPointer(), 
                    longByReference
                }, 
                WinNT.HRESULT.class);
    }
    
    public WinNT.HRESULT get_accName(Variant.VARIANT varID, WTypes.BSTRByReference pstr) {
        return (WinNT.HRESULT)this._invokeNativeObject(10 , 
                new Object[]{ 
                    this.getPointer(), 
                    varID, 
                    pstr 
                }, 
                WinNT.HRESULT.class);
    }
    
    public WinNT.HRESULT get_accRole(Variant.VARIANT childId, Variant.VARIANT roleId) {
        return (WinNT.HRESULT)this._invokeNativeObject(13 , 
                new Object[]{ 
                    this.getPointer(), 
                    childId, 
                    roleId 
                }, 
                WinNT.HRESULT.class);
    }
    
    public long getAccRoleID(long id){
        Variant.VARIANT.ByReference varResult = new Variant.VARIANT.ByReference();
        Variant.VARIANT varChild = new Variant.VARIANT();
        
        varChild.setValue((short)Variant.VT_I4, new WinDef.LONG(id));

        WinNT.HRESULT hr = this.get_accRole(varChild, varResult);

        COMUtils.checkRC(hr);
        
        return ((WinDef.LONG)varResult.getValue()).longValue();
    }
    
    public String getAccRoleText(long id){
        WinDef.DWORD roleId = new WinDef.DWORD(this.getAccRoleID(id));
        char[] buff = null;
        String text= "";

        int res = Oleacc.INSTANCE_API.GetRoleText(roleId.intValue(), buff , 0);
        
        if (res > 0){
            buff = new char[res + 1];
            
            res= Oleacc.INSTANCE_API.GetRoleText(roleId.intValue(), buff, res + 1);
            
            if(res > 0) 
                text= Native.toString(buff);
        }

        return text;
    }
    
    public String getAccName(long id){
        WTypes.BSTRByReference pstr = new WTypes.BSTRByReference();

        Variant.VARIANT varChild = new Variant.VARIANT();
        varChild.setValue((short)Variant.VT_I4, new WinDef.LONG(id));

        WinNT.HRESULT hr = this.get_accName(varChild, pstr);
        
        if (COMUtils.SUCCEEDED(hr))
            return pstr.getString();
        
        return "";
    }
}
