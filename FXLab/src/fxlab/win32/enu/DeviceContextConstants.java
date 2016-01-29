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
public enum DeviceContextConstants {
    /**
     * Does not reset the attributes of this DC to the default attributes when this DC is released.
     */
    DCX_NORESETATTRS(4),
    
    /**
     * Returns a DC that corresponds to the window rectangle rather than the client rectangle.
     */
    DCX_WINDOW(1),
    
    /**
     * Convenient enum to refer null option or the constant in not valid.
     */
    DCX_NULL(-1);
    
    private final int value;

    private DeviceContextConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static DeviceContextConstants fromValue(int value) {
        return Stream.of(DeviceContextConstants.values()).
                filter(dc -> dc.value == value).
                findFirst().
                orElse(DCX_NULL);
    }
}
