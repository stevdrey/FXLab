/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32.enu;

/**
 * Constants for register appropriate name of message for the WinForm property: .Name
 * 
 * @author srey
 */
public enum ContsantsMessages {
    DOT_NET_GET_CONTROL_NAME("WM_GETCONTROLNAME"),
    VB6_GET_CONTROL_NAME("Get_CONTROLNAME");

    private String value;

    private ContsantsMessages(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
