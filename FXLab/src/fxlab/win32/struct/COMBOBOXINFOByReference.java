/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.struct;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author srey
 */
public class COMBOBOXINFOByReference extends Structure implements Structure.ByReference {
    public int cbSize;
    public WinDef.RECT rcItem;
    public WinDef.RECT rcButton;
    public int stateButton;
    public WinDef.HWND hwndCombo;
    public WinDef.HWND hwndItem;
    public WinDef.HWND hwndList;

    public COMBOBOXINFOByReference(int cbSize, WinDef.RECT rcItem, WinDef.RECT rcButton, 
            int stateButton, WinDef.HWND hwndCombo, WinDef.HWND hwndItem, WinDef.HWND hwndList) {
        this.cbSize = cbSize;
        this.rcItem = rcItem;
        this.rcButton = rcButton;
        this.stateButton = stateButton;
        this.hwndCombo = hwndCombo;
        this.hwndItem = hwndItem;
        this.hwndList = hwndList;
    }

    public COMBOBOXINFOByReference() {
        this.cbSize= size();
    }
    
    @Override
    protected List getFieldOrder() {
        return Arrays.asList("cbSize", "rcItem", "rcButton", "stateButton", "hwndCombo", 
                "hwndItem", "hwndList");
    }
    
}
