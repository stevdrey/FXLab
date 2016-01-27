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
public enum AncestorConstants {
    /**
     * Retrieves the parent window. This does not include the owner, as it does with the GetParent function.
     */
    GA_PARENT(1), 
    
    /**
     * Retrieves the root window by walking the chain of parent windows.
     */
    GA_ROOT(2), 
    
    /**
     * Retrieves the owned root window by walking the chain of parent and owner windows returned by GetParent.
     */
    GA_ROOTOWNER(3);
    
    private final int value;

    private AncestorConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}