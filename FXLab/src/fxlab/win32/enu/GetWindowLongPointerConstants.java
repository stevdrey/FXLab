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
public enum GetWindowLongPointerConstants {
    /**
     * Retrieves the extended window styles.
     */
    GWL_EXSTYLE(-20),
    
    /**
     * Retrieves a handle to the application instance.
     */
    GWLP_HINSTANCE(-6),
    
    /**
     * Retrieves a handle to the parent window, if there is one.
     */
    GWLP_HWNDPARENT(-8),
    
    /**
     * Retrieves the identifier of the window.
     */
    GWLP_ID(-12),
    
    /**
     * Retrieves the window styles.
     */
    GWL_STYLE(-16),
    
    /**
     * Retrieves the user data associated with the window.
     * This data is intended for use by the application that created the window.
     * Its value is initially zero.
     */
    GWLP_USERDATA(-21),
    
    /**
     * Retrieves the pointer to the window procedure, or a handle representing the pointer to the window procedure. 
     * You must use the CallWindowProc function to call the window procedure.
     */
    GWLP_WNDPROC(-4),
    
    /**
     * Convenient enum to refer null option or the constant in not valid.
     */
    GWLP_NULL(-100);
    
    private final int value;

    private GetWindowLongPointerConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static GetWindowLongPointerConstants fromValue(int value) {
        return Stream.of(GetWindowLongPointerConstants.values()).
                filter(gwlp -> gwlp.value == value).
                findFirst().
                orElse(GWLP_NULL);
    }
}