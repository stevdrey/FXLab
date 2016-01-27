/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.enu;

import java.util.stream.Stream;

/**
 *
 * @author srey
 */
public enum ObjectStateConstants {
    /**
     * There is no button.
     *//**
     * There is no button.
     */
    STATE_SYSTEM_INVISIBLE(0x8000),
    
    /**
     * The button is pressed.
     */
    STATE_SYSTEM_PRESSED(0x8),
    
    /**
     * Convenient enum to refer null option or the constant in not valid.
     */
    NULL(-1);
    
    private final int value;

    private ObjectStateConstants(int value) {
        this.value = value;
    }
    
    public static ObjectStateConstants fromValue(int value) {
        return Stream.of(ObjectStateConstants.values()).
                filter(osc -> osc.value == value).
                findFirst().
                orElse(NULL);
    }
}
