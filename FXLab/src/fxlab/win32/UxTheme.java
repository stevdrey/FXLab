/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import fxlab.win32.struct.LOGFONT;

/**
 *
 * @author srey
 */
public interface UxTheme extends StdCallLibrary {
    UxTheme INSTANCE_API= (UxTheme) Native.loadLibrary("UxTheme", UxTheme.class, W32APIOptions.DEFAULT_OPTIONS);
    
    /**
     * Retrieves a theme handle to a window that has visual styles applied.
     * 
     * @param hWnd
     *          Handle of the window.
     * 
     * @return 
     *      The most recent theme handle from OpenThemeData.
     */
    WinNT.HANDLE GetWindowTheme(HWND hWnd);
    
    /**
     * Retrieves the value of a font property.
     * 
     * @param hTheme
     *          Handle to a window's specified theme data. Use OpenThemeData to create an HTHEME.
     * 
     * @param hdc
     *          HDC. This parameter may be set to NULL.
     * 
     * @param iPartId
     *          Value of type int that specifies the part that contains the font property. See Parts and States.
     * 
     * @param iStateId
     *          Value of type int that specifies the state of the part. See Parts and States.
     * 
     * @param iPropId
     *          Value of type int that specifies the property to retrieve. For a list of possible values, see Property Identifiers.
     * 
     * @param pFont
     *          Pointer to a {@link LOGFONT} structure that receives the font property value.
     * 
     * @return 
     *      If this function succeeds, it returns S_OK. Otherwise, it returns an HRESULT error code.
     */
    WinNT.HRESULT GetThemeFont(WinNT.HANDLE hTheme, WinDef.HDC hdc, int iPartId, 
            int iStateId, int iPropId, LOGFONT pFont);
    
    /**
     * Opens the theme data for a window and its associated class.
     * 
     * @param hwnd
     *          Handle of the window for which theme data is required.
     * 
     * @param pszClassList
     *          Pointer to a string that contains a semicolon-separated list of classes.
     * 
     * @return 
     *      OpenThemeData tries to match each class, one at a time, to a class data section in the active theme. 
     *      If a match is found, an associated HTHEME handle is returned. If no match is found NULL is returned.
     */
    WinNT.HANDLE OpenThemeData(HWND hwnd, String pszClassList);
    
    /**
     * Closes the theme data handle.
     * 
     * @param hTheme
     *          Handle to a window's specified theme data. Use {@link OpenThemeData} to create an HTHEME.
     * 
     * @return 
     *      If this function succeeds, it returns S_OK. Otherwise, it returns an HRESULT error code.
     */
    WinNT.HRESULT CloseThemeData(WinNT.HANDLE hTheme);
}
