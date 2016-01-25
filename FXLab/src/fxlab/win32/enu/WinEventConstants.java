/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.enu;

/**
 *
 * @author srey
 */
public enum WinEventConstants {
    /**
     * Events are ASYNC
     */
    WINEVENT_OUTOFCONTEXT(0x0000),
    
    /**
     * Don't call back for events on installer's thread
     */
    WINEVENT_SKIPOWNTHREAD(0x0001),
    
    /**
     * Don't call back for events on installer's process
     */
    WINEVENT_SKIPOWNPROCESS(0x0002),
    
    /**
     * Events are SYNC, this causes your dll to be injected into every process
     */
    WINEVENT_INCONTEXT(0x0004);
    
    private final int value;

    private WinEventConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
