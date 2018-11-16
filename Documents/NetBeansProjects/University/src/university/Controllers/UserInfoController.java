/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package university.Controllers;

import sample.dbControllers.MacPcJpaController;
import sample.dbControllers.UsersJpaController;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sample.DbTables.Log;
import sample.DbTables.MacPc;
import sample.DbTables.Users;

/**
 * FXML Controller class
 *
 * @author monder
 */
public class UserInfoController implements Initializable {

    @FXML
    private Button addUserB;
    @FXML
    private Button editeUserB;
    @FXML
    private Button deleteUserB;
    @FXML
    private Label userPrivL;
    @FXML
    private Label userIdL;
    @FXML
    private Label userNamL;
    @FXML
    private Label userPasswordL;
    @FXML
    private TableView <Log> rEventTable;
    @FXML
    private TableColumn<Log, Date> colDateOfEvent;
    @FXML
    private TableColumn<Log, String> colDetalOfEvent;
    @FXML
    private TableColumn<Log, String> colPcOfEvent;
    @FXML
    private TableView<Users> tLeft;
    @FXML
    private TableColumn tLeftIdCol;
    @FXML
    private TableColumn tLeftNameCol;
    @FXML
    private TableColumn tLeftPrivCol;
    private ObservableList<Users> dataOfUsersOrignal ;
    private ObservableList<Log> logsListSelectedUser ;
    private ObservableList<MacPc> PcListOrignal ;
    private MacPcJpaController macPc ;

    private Users selctedUser;
    private MacPc selectedPc;
    private MacPc toBeCreated;

    public static Users staticSelected;
    private UsersJpaController ujpc;
    private EntityManagerFactory emf;
    @FXML
    private AnchorPane userInfoView;
    @FXML
    private Tab usersInfoTab;
    @FXML
    private Tab pcInfoTab;
    @FXML
    private TableView<MacPc> tPc;
    @FXML
    private TableColumn<MacPc, Integer> tPcId;
    @FXML
    private TableColumn<MacPc, String> tPcName;
    @FXML
    private TableColumn<MacPc, String> tPcMac;
    @FXML
    private TabPane mamaTabePane;
    @FXML
    private TextField pcNameT;
    @FXML
    private TextField pcMacT;
    @FXML
    private Button addPcB;
    @FXML
    private ContextMenu menuInTablePcM;
    @FXML
    private MenuItem editeCellPcT;
    @FXML
    private MenuItem deleteM;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editeUserB.setDisable(true);
        deleteUserB.setDisable(true);
        mamaTabePane.getSelectionModel().selectedItemProperty().addListener((observablee, oldValuee, newValuee) -> {
            if (newValuee.getId().equals("pcInfoTab")) {
                addUserB.setDisable(true);
                loadAllPcs();
                
                tPc.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue!=null) {
                    pcNameT.setText(newValue.getPcName());
                    pcMacT.setText(newValue.getMacAddress());
                    selectedPc = newValue;
                    }else{
                        
                        System.out.println("no selection new "+newValue);
                    }
                });
            }else{
            addUserB.setDisable(false);

            loadEmAll();
        tLeft.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            editeUserB.setDisable(false);
            deleteUserB.setDisable(false);               
            selctedUser = newValue;
            staticSelected = newValue;
            try{
            if (null != newValue ){
                    if (newValue.getLogList() != null) {
                    logsListSelectedUser = FXCollections.observableArrayList(newValue.getLogList());
                    colDateOfEvent.setCellValueFactory(
                    new PropertyValueFactory<>("dateTime"));
                   colDetalOfEvent.setCellValueFactory(
                    new PropertyValueFactory<>("notes"));
                   colPcOfEvent.setCellValueFactory(cellData -> {
                          return  new ReadOnlyStringWrapper (cellData.getValue().getPcMac().getPcName());
                      });
                   rEventTable.setItems(logsListSelectedUser);

                }
                userPrivL.setText(newValue.getPriv());
                userIdL.setText(newValue.getId()+"");
                userNamL.setText(newValue.getUsername());
                userPasswordL.setText(newValue.getPassword());               
            }else {
                logsListSelectedUser = null;
            }
            }catch(NullPointerException e){
                e.printStackTrace();
            }
            
        
        });                
            }
});
    }
    private ObservableList<MacPc> loadAllPcs (){
        emf = Persistence.createEntityManagerFactory("newGuiPU");
        macPc = new MacPcJpaController(emf);
        
        PcListOrignal = FXCollections.observableArrayList(macPc.findMacPcEntities());    
        tPc.setItems(PcListOrignal);
        tPcId.setCellValueFactory(
                new PropertyValueFactory("id"));
        tPcMac.setCellValueFactory(
                new PropertyValueFactory("macAddress"));
        tPcName.setCellValueFactory(
                new PropertyValueFactory("pcName"));
       return PcListOrignal;
    }
    private ObservableList<Users> loadEmAll(){
        emf = Persistence.createEntityManagerFactory("newGuiPU");
        ujpc = new UsersJpaController(emf);
        dataOfUsersOrignal = FXCollections.observableArrayList(ujpc.findUsersEntities());    
        tLeft.setItems(dataOfUsersOrignal);
                tLeftIdCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("id"));
        tLeftNameCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("username"));
        tLeftPrivCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("priv"));
       return dataOfUsersOrignal;
       
    }
        private ObservableList<Users> loadEmAll(Users u){
        dataOfUsersOrignal.remove(u);
        dataOfUsersOrignal.add(u);
        tLeft.setItems(dataOfUsersOrignal);
                tLeftIdCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("id"));
        tLeftNameCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("username"));
        tLeftPrivCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("priv"));
       return dataOfUsersOrignal;
       
    }
    @FXML
    private void addUserHand(ActionEvent event) {
    	Dialog<ButtonType> dialog = new Dialog<ButtonType>();
	dialog.initOwner(userInfoView.getScene().getWindow());
	FXMLLoader fxmlLoader = new FXMLLoader();
	fxmlLoader.setLocation(getClass().getResource("/fxStuff/newUserDialog.fxml"));
						
	try {
		dialog.getDialogPane().setContent(fxmlLoader.load());
		dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		Optional<ButtonType> resualt = dialog.showAndWait();
                
//                fxmlLoader.getClassLoader()
		if(resualt.isPresent()) {
			NewUserDialogController bew = fxmlLoader.getController();
			if (resualt.get()==ButtonType.YES) {
                            if(bew.addNewUser()){
                                loadEmAll();
                            }
//			studentListView.getItems().setAll(loadView());
			}
		}else {
                    System.out.println("No New Student is regestread dum dum");
		}
	} catch (Exception e) {
		e.printStackTrace();
                System.out.println(e + "jjj"+"\n"+e.getMessage());	
		}
    }

    @FXML
    private void editeUserHand(ActionEvent event) {
    	Dialog<ButtonType> dialog = new Dialog<ButtonType>();
	dialog.initOwner(userInfoView.getScene().getWindow());
	FXMLLoader fxmlLoader = new FXMLLoader();
	fxmlLoader.setLocation(getClass().getResource("/fxStuff/editeUserDialog.fxml"));
						
	try {
		dialog.getDialogPane().setContent(fxmlLoader.load());
		dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		Optional<ButtonType> resualt = dialog.showAndWait();
                EditeUserDialogController bew = fxmlLoader.getController();
//                fxmlLoader.getClassLoader()
		if(resualt.isPresent()) {

			if (resualt.get()==ButtonType.YES) {
                            Users toBeSentToLoad= bew.editeUser();
                            if(toBeSentToLoad!=null){
                                loadEmAll(toBeSentToLoad);
                            }
//			studentListView.getItems().setAll(loadView());
			}
		}else {
                    System.out.println("No New Student is regestread dum dum");
		}
	} catch (Exception e) {
		e.printStackTrace();
                System.out.println(e + "jjj"+"\n"+e.getMessage());	
		}        
    }
public static Users getSelctedUser (){
    return staticSelected ;
}
    @FXML
    private void deleteUserHand(ActionEvent event) {
        try {
            
            ujpc.destroy(selctedUser.getId());
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(UserInfoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UserInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadEmAll();
    }

    @FXML
    private void addPcHand(ActionEvent event) {
        if (addPcB.getText().equals("Add new Pc")) {
            
             if(! cheker(new MacPc(macPc.getMacPcCount()+1, pcMacT.getText(), pcNameT.getText()))){
        toBeCreated = new MacPc(macPc.getMacPcCount()+1, pcMacT.getText(), pcNameT.getText());
        macPc.create(toBeCreated);
        loadAllPcs();
            System.out.println("Not The Same");
        }else{
            System.out.println("Contains the same");
        }///////////////////////////////////////////////////////////////////////////////////////////////////////
        }else if (addPcB.getText().equals("Edite And Save Pc")) {/////////////////////////////////////////////////////////
            try {
                MacPc pcToEdite = new MacPc(selectedPc.getId() , pcMacT.getText(), pcNameT.getText());
                macPc.edit(pcToEdite);
                System.out.println("Edited");
                addPcB.setText("Add new Pc");
//                loadAllPcs();
                PcListOrignal.replaceAll((t) -> {
                    if (Objects.equals(t.getId(), selectedPc.getId()) ) {
                        return pcToEdite ;
                    }else
                    return t; //To change body of generated lambdas, choose Tools | Templates.
                });
//                PcListOrignal.add(pcToEdite);


            } catch (Exception ex) {
                Logger.getLogger(UserInfoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private boolean cheker(MacPc v){
        System.out.println("About to enter for");
            return (selectedPc.getMacAddress().equalsIgnoreCase(v.getMacAddress())) && (selectedPc.getPcName().equalsIgnoreCase(v.getPcName()));
    }
    @FXML
    private void editeCellHand(ActionEvent event) {
        addPcB.setText("Edite And Save Pc");
    }

    @FXML
    private void deleteCellHand(ActionEvent event) {
        if (selectedPc != null) {
            try {
                macPc.destroy(selectedPc.getId());
                PcListOrignal.remove(selectedPc);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(UserInfoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}
