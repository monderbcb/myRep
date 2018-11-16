package university.Controllers;

import university.JpaControllers.StudentJpaController;
import university.JpaControllers.exceptions.IllegalOrphanException;
import university.JpaControllers.exceptions.NonexistentEntityException;
import java.io.File;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import university.Entites.Student;

public class studentController implements Initializable {
    @FXML
    public ListView<Student> studentListView;
    public TextArea studentInfo;
	@FXML Button editB;
	@FXML Button addB;
	@FXML ImageView stuPhoto;
	@FXML TextField stunameText;
	@FXML TextField stuIDText;
	@FXML TextField stuScioText;
	@FXML TextField stuPhoneText;
	@FXML TextField stuNatoText;
	@FXML TextArea stuAdText;
	@FXML BorderPane studentView;
	@FXML TextArea stuNotesText;
	@FXML TextField stuStutsText;
	TextField stuStutsText1;
	@FXML TextField stuDepText;
	@FXML TextField stuAcYText;
	@FXML TextField stuSemsText;
        private EntityManagerFactory emf;
    @FXML
    private TextField stuMajoreText1;
    private static Student student_info;
    @FXML
    private Button deleteB;
    @FXML
    private Label studentCount;
    public void initialize() {
        deleteB.setDisable(true);
        editB.setDisable(true);
        studentListView.getItems().setAll(loadView());
        studentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                deleteB.setDisable(false);
                editB.setDisable(false);
                student_info = (Student) studentListView.getSelectionModel().getSelectedItem();
                stunameText.setText(student_info.getFname()+" "+student_info.getMname()+" "+student_info.getLname());
                stuAdText.setText(student_info.getAddress());
                stuIDText.setText(student_info.getId()+"");
                stuPhoneText.setText(student_info.getPhoneNumber()+"");
                stuScioText.setText(student_info.getSocialNumber()+"");
                stuNatoText.setText(student_info.getNationality()+"");
                stuAcYText.setText(student_info.getSemesterCount());
                stuDepText.setText(student_info.getSubMajor().getMajorName().getMajor());
                stuNotesText.setText(student_info.getNotes());
                stuPhoto.setImage(new Image (student_info.getImage(),200 ,180 ,true , true));
                stuSemsText.setText(student_info.getSemsterId().getSemesterName());
                stuStutsText.setText(student_info.getStatus()?"Active":"Not Active");
                stuMajoreText1.setText(student_info.getSubMajor().getSubMajorName());
            }
        });
    }
    public List<Student> loadView(){
            emf = Persistence.createEntityManagerFactory("newGuiPU");
            StudentJpaController sjc = new StudentJpaController(emf);
           studentCount.setText("There is Curntly "+sjc.getStudentCount()+" Student Regestreated");
            List<Student> studentList 
                    = sjc.findStudentEntities();
           
            sjc.getEntityManager().close();
            emf.close();
            return studentList;
    }
//    public void onItemSelected() {
//        StudentInfo student = (StudentInfo) studentListView.getSelectionModel().getSelectedItem();
//        studentInfo.setText(student.getSTUDENT_NAME() + "\n\n" +
//                student.getSTUDENT_ADDRESS() + "\n\n" + student.getSTUDENT_ID() + "\n\n" +
//                student.getSTUDENT_PHONE() + "\n\n" + student.getSTUDENT_SOCIAL() + "\n\n" +
//                student.getSTUDENT_NATIONALITY());
//    }

	@FXML public void addHandler(ActionEvent event) {
	Dialog<ButtonType> dialog = new Dialog<ButtonType>();
	
	dialog.initOwner(studentView.getScene().getWindow());
	
	FXMLLoader fxmlLoader = new FXMLLoader();
	
	fxmlLoader.setLocation(getClass().getResource("/fxStuff/newStudentDialog.fxml"));
						
	try {
		dialog.getDialogPane().setContent(fxmlLoader.load());
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		Optional<ButtonType> resualt = dialog.showAndWait();
                NewStudentDialogController newStudentDialogController = fxmlLoader.getController();

//                fxmlLoader.getClassLoader()
		if(resualt.isPresent()) {
			newStudentDialogController.loadCountry();
			if (resualt.get()==ButtonType.OK) {
			newStudentDialogController.newStudent();
			studentListView.getItems().setAll(loadView());
			}
		}else {
                    System.out.println("No New Student is regestread dum dum");
		}
	} catch (Exception e) {
		e.printStackTrace();
                System.out.println(e + "now"+"\n"+e.getMessage());	
		}
	}

	@FXML public void editHandler(ActionEvent event) {
        
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
	
	dialog.initOwner(studentView.getScene().getWindow());
	
	FXMLLoader fxmlLoader = new FXMLLoader();
	
	fxmlLoader.setLocation(getClass().getResource("/fxStuff/editeStudentDialog.fxml"));
						
	try {
		dialog.getDialogPane().setContent(fxmlLoader.load());
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		Optional<ButtonType> resualt = dialog.showAndWait();
                
                	EditeStudentDialogController editeStudentDialogController = fxmlLoader.getController();
		if(resualt.isPresent()) {

			if (resualt.get()==ButtonType.OK) {
                            editeStudentDialogController.editStudent();
                            studentListView.getItems().setAll(loadView());
			}
		}else {
                    System.out.println("No New Student is regestread dum dum");
		}
	} catch (Exception e) {
		e.printStackTrace();
                System.out.println(e +"\n"+e.getMessage()+"\n editing Studdent");	
		}
	}
public static Student getStedentList(){
    return student_info ;
}

    @FXML
    private void deleteHand(ActionEvent event) {
        if (student_info != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Dleting Student ");
            alert.setContentText("Are You Shore You Want To Delete Old Poor "+student_info.getName()+ " !");
            alert.showAndWait().ifPresent((t) -> {
            if (t.equals(ButtonType.OK)){
                System.out.println("Done deleting dum dum "+student_info.getSocialNumber());
                emf = Persistence.createEntityManagerFactory("newGuiPU");
                StudentJpaController sjc = new StudentJpaController(emf);

                    try {
                        sjc.destroy(student_info.getStudentId());

                    } catch (IllegalOrphanException ex) {
                        Logger.getLogger(studentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(studentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                studentListView.getItems().setAll(loadView());

            }    
                });
        }else {
            
        }
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
