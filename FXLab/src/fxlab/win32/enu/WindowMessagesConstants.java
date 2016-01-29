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
public enum WindowMessagesConstants {
    /**
     * Constant for determines the length, in characters, of the text associated with a window.
     */
    WM_GETTEXTLENGTH(0x000E),
    
    /**
     * Constant for copy the text that corresponds to a window into a buffer provided by the caller.
     */
    WM_GETTEXT(0x000D),
    
    /**
     * Retrieves the font with which the control is currently drawing its text.
     */
    WM_GETFONT(0x0031),
    
    /**
     * Sets the font that a control is to use when drawing text.
     */
    WM_SETFONT(0x0030),
    
    /**
     * Sets the text of a window.
     */
    WM_SETTEXT(0x000C),
    
    /**
     * Convenient enum to refer null option or the constant in not valid.
     */
    WM_NULL(-1);
    
    private final int value;

    private WindowMessagesConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static WindowMessagesConstants fromValue(int value) {
        return Stream.of(WindowMessagesConstants.values()).
                filter(wm -> wm.value == value).
                findFirst().
                orElse(WM_NULL);
    }
}
