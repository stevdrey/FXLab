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
public enum AccessibilityConstants {
    /**
     * The object represents read-only text, such as labels for other controls or 
     * instructions in a dialog box. Static text cannot be modified or selected.
     */
    ROLE_SYSTEM_STATICTEXT(0x00000029),
    
    /**
     * The object represents a push-button control.
     */
    ROLE_SYSTEM_PUSHBUTTON(0x0000002B),
    
    /**
     * Convenient enum to refer null option or the constant in not valid.
     */
    ROLE_NULL(-1);
    
    private final int value;

    private AccessibilityConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static AccessibilityConstants fromValue(int value) {
        return Stream.of(AccessibilityConstants.values()).
                filter(p -> p.value == value).
                findFirst().
                orElse(ROLE_NULL);
    }
}
