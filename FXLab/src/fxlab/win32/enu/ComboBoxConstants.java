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
public enum ComboBoxConstants {
     /**
     * Constant for gets a length of string in combo-box
     */
    CB_GETLBTEXTLEN(0x0149),
    
    /**
     * Constant for gets a string from a combo-box
     */
    CB_GETLBTEXT(0x0148),
    
    /**
     * Constant for gets a selected string in combo-box
     */
    CB_SELECTSTRING(0x014D),
    
    /**
     * An application sends a CB_GETITEMDATA message to a combo box to retrieve 
     * the application-supplied value associated with the specified item in the combo box.
     */
    CB_GETITEMDATA(0x0150),
    
    /**
     * An application sends a CB_GETCURSEL message to retrieve the index of the currently 
     * selected item, if any, in the list box of a combo box.
     */
    CB_GETCURSEL(0x0147),
    
    /**
     * Determines whether the list box of a combo box is dropped down.
     */
    CB_GETDROPPEDSTATE(0x0157);
    
    private final int value;

    private ComboBoxConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
