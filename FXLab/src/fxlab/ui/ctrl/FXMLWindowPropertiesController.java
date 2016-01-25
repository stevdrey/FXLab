/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab.ui.ctrl;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;
import fxlab.pojo.ControlRecord;
import fxlab.ui.enu.TypeControl;
import fxlab.ui.evt.MouseListerImpl;
import fxlab.util.DialogUtil;
import fxlab.win32.Kernel32;
import fxlab.win32.POINTByValue;
import fxlab.win32.User32;
import fxlab.win32.WinApiUtil;
import fxlab.win32.enu.AncestorConstants;
import fxlab.win32.enu.ConstantsMessages;
import java.net.URL;
import java.util.EventListener;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseListener;

/**
 * FXML Controller class
 *
 * @author srey
 */
public class FXMLWindowPropertiesController implements Initializable {
    private User32 user32;
    private Kernel32 kernel32;
    private ObservableList<EventListener> eventsList;
    private AtomicBoolean isRecord;
    private AtomicReference<ControlRecord> currentControl;
    
    @FXML
    private GridPane gpn_formContainer;
    @FXML
    private Label lbl_className;
    @FXML
    private TextField txt_className;
    @FXML
    private Label lbl_id;
    @FXML
    private TextField txt_id;
    @FXML
    private Label lbl_caption;
    @FXML
    private TextField txt_caption;
    @FXML
    private Label lbl_text;
    @FXML
    private TextField txt_text;
    @FXML
    private Label lbl_application;
    @FXML
    private ComboBox<String> cmb_application;
    @FXML
    private Button btn_record;
    @FXML
    private Button btn_clear;
    @FXML
    private Button btn_cancel;    
    @FXML
    private Button btn_update;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.user32= User32.INSTANCE_API;
        this.kernel32= Kernel32.INSTANCE_API;
        this.eventsList= FXCollections.observableArrayList();
        this.isRecord= new AtomicBoolean(false);
        this.currentControl= new AtomicReference<>();
        
        this.cmb_application.setItems(getListOfApps());
    }    
    
    // section of private methods
    
    /**
     * This method clear the controls in form
     * 
     * @param all 
     *          If this parameter is true clear all the controls, 
     *          else just clear text field controls.
     */
    private void clearControls(boolean all) {
        if (all)
            this.cmb_application.getItems().clear();
        
        this.txt_caption.clear();
        this.txt_className.clear();
        this.txt_id.clear();
        this.txt_text.clear();
    }
    
    /**
     * This method looking for an application is running in Operating System
     * 
     * @return 
     *      Return a list with all the names of applications running in Operating System.
     */
    private ObservableList<String> getListOfApps() {
        ObservableList<String> listOfApps= FXCollections.observableArrayList();
        
        user32.EnumWindows((WinDef.HWND hwnd, Pointer pntr) -> {
            char[] bufferApp= new char[1024];
            String nameApp= null;
            
            user32.GetWindowText(hwnd, bufferApp, bufferApp.length);
            nameApp= Native.toString(bufferApp);
            
            if (nameApp != null && !nameApp.isEmpty())
                listOfApps.add(nameApp);
            
            return true;
        }, Pointer.NULL);
        
        return listOfApps;
    }
    
    private void registerListeners() {
        if (this.eventsList != null && this.eventsList.size() > 0)
            this.eventsList.forEach(evt -> {
                if (evt instanceof MouseListerImpl)
                    GlobalScreen.addNativeMouseListener((NativeMouseListener) evt);
            });
    }
    
    private void removeListerners() {
        if (this.eventsList != null && this.eventsList.size() > 0) {
            this.eventsList.forEach(evt -> {
                if (evt instanceof MouseListerImpl)
                    GlobalScreen.removeNativeMouseListener((NativeMouseListener) evt);
            });
            
            // clear all register listeners
            this.eventsList.clear();
            
            // release any resource of last window (control) user selected.
            if (this.currentControl.get() != null)
                this.currentControl.get().dispose();
        }
    }
    
    private MouseListerImpl createMouseLister() {
        return new MouseListerImpl((evt) -> {
            Runnable r= () -> {
                if (!this.cmb_application.getSelectionModel().isEmpty() && this.isRecord.get()) {
                    WinDef.HWND application= WinApiUtil.getWindowHandler(this.cmb_application.
                        getSelectionModel().getSelectedItem());
                    
                    ControlRecord ctrl= this.currentControl.updateAndGet(record -> {
                        WinDef.HWND hwnd= WinApiUtil.getWindowFromPoint(evt.getX(), evt.getY());
                        Optional<ControlRecord> nControl= Optional.empty();
                        
                        if (record != null) {
                            if (!(record.isComboBox() && record.isShowListBox())
                                    && !record.getHandle().equals(hwnd)) {
                                
                                nControl= Optional.of(new ControlRecord(application, hwnd));
                                record.dispose();
                            } else
                                nControl= Optional.of(record);
                            
                        } else
                            nControl= Optional.of(new ControlRecord(application, hwnd));
                        
                        return nControl.get();
                    });
                    
                    this.clearControls(false);
                    
                    if (ctrl != null) {
                        this.txt_caption.setText(ctrl.getCaption());
                        this.txt_className.setText(ctrl.getClassName());
                        this.txt_id.setText(ctrl.getId());
                        this.txt_text.textProperty().unbind();
                        this.txt_text.textProperty().bind(ctrl.textProperty());
                    }
                } else {
                    DialogUtil.showWarning("Missiging Data", 
                            "Before Record, you must select the target application");
                    
                    this.cmb_application.requestFocus();
                }
            };
            
            if (!Platform.isFxApplicationThread())
                Platform.runLater(r);
            
            else
                r.run();
        });
    }
    
    // section of event handlers
    
    @FXML
    private void handleBtn_updateAction(ActionEvent evt) {
        this.cmb_application.setItems(getListOfApps());
    }
    
    @FXML
    private void handleBtn_clear(ActionEvent evt) {
        this.clearControls(true);
    }
    
    @FXML
    private void handlerBtn_record(ActionEvent evt) {
        Stage stage= null;
        
        this.isRecord.set(true);
        this.eventsList.add(this.createMouseLister());
        this.registerListeners();
        
        stage= (Stage) this.gpn_formContainer.getScene().getWindow();
        
        if (stage != null)
            stage.setIconified(true);
    }
    
    @FXML
    private void handleBtn_cancel(ActionEvent evt) {
        this.isRecord.set(false);
        this.removeListerners();
    }
}