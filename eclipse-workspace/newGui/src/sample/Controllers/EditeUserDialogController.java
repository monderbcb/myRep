/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.Controllers;

import sample.dbControllers.UsersJpaController;
import sample.dbControllers.exceptions.NonexistentEntityException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sample.DbTables.Users;

/**
 * FXML Controller class
 *
 * @author monder
 */
public class EditeUserDialogController implements Initializable {

    @FXML
    private DialogPane editeStudentDialogView;
    @FXML
    private TextField nameT;
    @FXML
    private ChoiceBox<String> privlageCo;
    @FXML
    private PasswordField passwordT;
    private EntityManagerFactory emf ;
    private final String[] privList = {"Dean","Instructore","Advance User","Simple User"};
    private Users toEdit;
    @FXML
    private CheckBox showPassCb;
    @FXML
    private Label userId;
    @FXML
    private TextField passwordTextField;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toEdit = UserInfoController.staticSelected;
        if (toEdit!= null){
//            passwordTextField.setVisible(false);
            passwordTextField.managedProperty().bind(showPassCb.selectedProperty());
            passwordTextField.visibleProperty().bind(showPassCb.selectedProperty());
            passwordT.managedProperty().bind(showPassCb.selectedProperty().not());
            passwordT.visibleProperty().bind(showPassCb.selectedProperty().not());    
            passwordTextField.textProperty().bindBidirectional(passwordT.textProperty());
//            privlageCo.getItems().addAll(privList);
            privlageCo.setValue(toEdit.getPriv());
            nameT.setText(toEdit.getUsername());
            passwordT.setText(toEdit.getPassword());
            userId.setText("This User ID = "+toEdit.getId());
        // TODO
        }
    }   
    private String santize (String data) {
		if (!data.matches("[^a-z A-Z0-9ا-ي]")) {
            return data.replaceAll("[^a-z A-Z0-9ا-ي]", "");  
                }else {
                    return "" ;		
		}
    }
    public Users editeUser(){
        emf = Persistence.createEntityManagerFactory("newGuiPU");
        UsersJpaController ujc = new UsersJpaController(emf);
        String tobe =santize(nameT.getText());
        if(!(tobe.equals("") || passwordT.getText().isEmpty())){
            try {
                Users editeddToenter = new Users(toEdit.getId(), nameT.getText(), passwordT.getText(), privlageCo.getSelectionModel().getSelectedIndex()+"");
                ujc.edit(editeddToenter);
                return editeddToenter ;
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(EditeUserDialogController.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (Exception ex) {
                Logger.getLogger(EditeUserDialogController.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }else{
                return null;
            }
    }

    @FXML
    private void showPasswordHand(ActionEvent event) {
        System.out.println(passwordTextField.getText());
        System.out.println(passwordT.getText());

//        if (showPassCb.isSelected() && (! passwordT.getText().isEmpty())) {
//            passwordT.set
//        }
    }
    
    
}
