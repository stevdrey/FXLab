/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.win32;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import fxlab.win32.enu.FontConstants;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author srey
 */
public class LOGFONT extends Structure {
    public int lfHeight;
    public int lfWidth;
    public int lfEscapement;
    public int lfOrientation;
    public int lfWeight;
    
    public WinDef.BYTE lfItalic;
    public WinDef.BYTE lfUnderline;
    public WinDef.BYTE lfStrikeOut;
    
    public byte lfCharSet;
    public byte lfOutPrecision;
    public byte lfClipPrecision;
    public byte lfQuality;
    public byte lfPitchAndFamily;
    
    public char[] lfFaceName;

    public LOGFONT(int lfHeight, int lfWidth, int lfEscapement, int lfOrientation, int lfWeight, 
            WinDef.BYTE lfItalic, WinDef.BYTE lfUnderline, WinDef.BYTE lfStrikeOut, byte lfCharSet, 
            byte lfOutPrecision, byte lfClipPrecision, byte lfQuality, byte lfPitchAndFamily, char[] lfFaceName) {
        this.lfHeight = lfHeight;
        this.lfWidth = lfWidth;
        this.lfEscapement = lfEscapement;
        this.lfOrientation = lfOrientation;
        this.lfWeight = lfWeight;
        this.lfItalic = lfItalic;
        this.lfUnderline = lfUnderline;
        this.lfStrikeOut = lfStrikeOut;
        this.lfCharSet = lfCharSet;
        this.lfOutPrecision = lfOutPrecision;
        this.lfClipPrecision = lfClipPrecision;
        this.lfQuality = lfQuality;
        this.lfPitchAndFamily = lfPitchAndFamily;
        this.lfFaceName = lfFaceName == null ? new char[FontConstants.LF_FULLFACESIZE.getValue()] : lfFaceName;
    }

    public LOGFONT() {
        this.lfFaceName= new char[FontConstants.LF_FULLFACESIZE.getValue()];
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList("lfHeight", "lfWidth", "lfEscapement", "lfOrientation",
                "lfWeight", "lfItalic", "lfUnderline","lfStrikeOut", "lfCharSet", 
                "lfOutPrecision", "lfClipPrecision", "lfQuality", "lfPitchAndFamily",
                "lfFaceName");
    }
    
}