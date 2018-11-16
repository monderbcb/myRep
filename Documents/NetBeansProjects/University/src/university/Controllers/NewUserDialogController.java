/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sample.DbTables.Users;
import sample.dbControllers.UsersJpaController;

/**
 * FXML Controller class
 *
 * @author monder
 */
public class NewUserDialogController implements Initializable {

    @FXML
    private DialogPane newStudentDialogView;
    @FXML
    private TextField nameT;
    @FXML
    private ChoiceBox<String> privlageCo;
    @FXML
    private PasswordField passwordT;
    private EntityManagerFactory emf ;
    private final String[] privList = {"Dean","Instructore","Advance User","Simple User"};
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        privlageCo.getItems().addAll(privList);
//        newStudentDialogView.addEventHandler(EventType.ROOT, (event) -> {
//            System.out.println(event.getEventType().getName());});
//        if () {
//            
//        }
//        
        // TODO
    }    
    private String santize (String data) {
		if (!data.matches("[^a-z A-Z0-9ا-ي]")) {
            return data.replaceAll("[^a-z A-Z0-9ا-ي]", "");  
                }else {
                    return "" ;		
		}
    }
    public boolean addNewUser(){
        emf = Persistence.createEntityManagerFactory("newGuiPU");
        UsersJpaController ujc = new UsersJpaController(emf);
        String tobe =santize(nameT.getText());
        if(!(tobe.equals("") || passwordT.getText().isEmpty())){
           
        ujc.create(new Users(ujc.getUsersCount()+1,santize(nameT.getText()), passwordT.getText(), privlageCo.getSelectionModel().getSelectedIndex()+1+""));
         return true;
        }else{
                return false;
            }
    }
    
}
