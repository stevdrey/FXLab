/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import fxlab.win32.struct.LOGFONT;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;
import fxlab.win32.enu.ObjectConstants;
import fxlab.win32.enu.StokeObjectConstants;

/**
 *
 * @author srey
 */
public interface GDI32 extends com.sun.jna.platform.win32.GDI32{
    /**
     * Singleton instance of this Interface.
     */
    GDI32 INSTANCE_API= (GDI32) Native.loadLibrary("GDI32", GDI32.class, W32APIOptions.DEFAULT_OPTIONS);
    
    /**
     * The GetObject function retrieves information for the specified graphics object.
     * 
     * @param hgdiobj
     *          A handle to the graphics object of interest. 
     *          This can be a handle to one of the following: 
     *          <ul>
     *              <li>a logical bitmap</li>
     *              <li>a brush</li>
     *              <li>a font</li>
     *              <li>a palette</li>
     *              <li>a pen</li>
     *              <li>a device independent bitmap</li>
     *          </ul>
     *          created by calling the CreateDIBSection function.
     * 
     * @param cbBuffer
     *          The number of bytes of information to be written to the buffer.
     * 
     * @param lpvObject
     *          A pointer to a buffer that receives the information about the specified graphics object.
     *          
     *          If the lpvObject parameter is NULL, the function return value is the number of 
     *          bytes required to store the information it writes to the buffer for the specified graphics object.
     * 
     *          The address of lpvObject must be on a 4-byte boundary; otherwise, GetObject fails.
     * 
     * @return 
     *      If the function succeeds, and lpvObject is a valid pointer, the return 
     *      value is the number of bytes stored into the buffer.
     * 
     *      If the function succeeds, and lpvObject is NULL, the return value is the 
     *      number of bytes required to hold the information the function would store into the buffer.
     * 
     *      If the function fails, the return value is zero.
     */
    int GetObject(WinNT.HANDLE hgdiobj, int cbBuffer, LOGFONT lpvObject);
    
    /**
     * The GetTextFace function retrieves the typeface name of the font that is 
     * selected into the specified device context.
     * 
     * @param hdc
     *          A handle to the device context.
     * 
     * @param nCount
     *          The length of the buffer pointed to by lpFaceName. For the ANSI function it is a BYTE count and for the Unicode function it is a WORD count. 
     *          Note that for the ANSI function, characters in SBCS code pages take one byte each, while most characters in DBCS code pages take two bytes; 
     *          for the Unicode function, most currently defined Unicode characters 
     *          (those in the Basic Multilingual Plane (BMP)) are one WORD while Unicode surrogates are two WORDs.
     * 
     * @param lpFaceName
     *          A pointer to the buffer that receives the typeface name. 
     *          If this parameter is NULL, the function returns the number of characters in the name, 
     *          including the terminating null character.
     * 
     * @return 
     *      If the function succeeds, the return value is the number of characters copied to the buffer.
     *      If the function fails, the return value is zero.
     */
    int GetTextFace(WinDef.HDC hdc, int nCount, char[] lpFaceName);
    
    /**
     * The GetCurrentObject function retrieves a handle to an object of the specified 
     * type that has been selected into the specified device context (DC).
     * 
     * @param hdc
     *          A handle to the DC.
     * 
     * @param uObjectType
     *          The object type to be queried. This parameter can be one of the {@link ObjectConstants}
     *          values.
     * @return 
     */
    WinNT.HANDLE GetCurrentObject(WinDef.HDC hdc, int uObjectType);
    
    /**
     * The GetStockObject function retrieves a handle to one of the stock pens, brushes, fonts, or palettes.
     * 
     * @param fnObject
     *          The type of stock object. This parameter can be one of the {@link StokeObjectConstants} values.
     * 
     * @return 
     *      If the function succeeds, the return value is a handle to the requested logical object.
     */
    WinNT.HANDLE GetStockObject(int fnObject);
    
    /**
     * The GetBkColor function returns the current background color for the specified device context.
     * 
     * @param hdc
     *          Handle to the device context whose background color is to be returned.
     * 
     * @return 
     *      If the function succeeds, the return value is a COLORREF value for the current background color.
     */
    int GetBkColor(WinDef.HDC hdc);
    
    /**
     * The GetBValue macro retrieves an intensity value for the blue component of a red, green, blue (RGB) value.
     * 
     * @param rgb
     *          Specifies an RGB color value.
     * 
     * @return 
     *      The return value is the intensity of the blue component of the specified RGB color.
     */
    int GetBValue(int rgb);
    
    int GetRValue(int rgb);
    
    int GetGValue(int rgb);
}
