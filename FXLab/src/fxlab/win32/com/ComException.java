/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.com;

import com.sun.jna.platform.win32.WinNT;

/**
 *
 * @author srey
 */
public class ComException extends com.sun.jna.platform.win32.COM.COMException {
    private final WinNT.HRESULT hresult;

    public ComException(WinNT.HRESULT hresult, String message) {
        super(message);
        
        this.hresult = hresult;
    }

    public ComException(WinNT.HRESULT hresult) {
        super(String.format("COM API return 0x%s, fail code", 
                Integer.toHexString(hresult.intValue())));
        
        this.hresult = hresult;
    }

    public WinNT.HRESULT getHresult() {
        return hresult;
    }
    
}