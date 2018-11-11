package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import sample.DbConnection;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import com.mysql.jdbc.PreparedStatement;
//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import com.sun.xml.internal.stream.Entity;
import sample.dbControllers.CountriesJpaController;
import sample.dbControllers.DepartmentJpaController;
import sample.dbControllers.SemesterJpaController;
import sample.dbControllers.StudentJpaController;
import sample.dbControllers.SubDepartmentJpaController;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sample.DbTables.Countries;
import sample.DbTables.Department;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javax.naming.Context;
import javax.persistence.EntityGraph;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.security.auth.Subject;
import sample.DbTables.Semester;
import sample.DbTables.Student;
import sample.DbTables.SubDepartment;
import sample.DbTables.Subjects;
//import sample.DbTables.Countries;


public class NewStudentDialogController {
	
	@FXML ImageView stuPhoto;
	@FXML TextField stunameText;
	@FXML TextField stuIDText;
	@FXML TextField stuScioText;
	@FXML TextField stuPhoneText;
	@FXML TextArea stuAdText;
	@FXML Button uploadB;
	@FXML ComboBox countrysCo;
	@FXML ChoiceBox statusCo;
	@FXML ChoiceBox DepartmentCo;
	@FXML ChoiceBox MajoreCo;
	@FXML ChoiceBox AcadmicYCo;
	@FXML TextArea stuNotesText;
        @FXML ChoiceBox SemesterCo;

//        private ObservableList<String> countryList = FXCollections.observableArrayList();
//        private ObservableList<String> departmintList = FXCollections.observableArrayList();
        private EntityManagerFactory emf ;
        private List<Department> deplist;
        private List<Countries> coulist;
        private SubDepartment subList;
        private List <String> extList ;
        private final String [] acadimic={"1","2","3","4","5","6","7","8","9"};
        private List <Semester> semsterList ;

    @FXML
    private DialogPane newStudentView;
        
        
         
        public void initialize() {
        System.out.println("\nNow"+11111+"\n");
                        MajoreCo.setDisable(true);

        loadCountry();
        loadStuts();
        loadDepartment();
        loadSemster();
        loadAcadimic();
        }
        private Image copyFile(File file) {
        Image i = null ;
            try {
            File dest = new File("D:\\myPhotos\\"+stuIDText.getText()+"\\"+file.getName()); //any location
            dest.mkdirs();
            Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Noting is coming dum dum\n"+dest.toPath().toString());
             i = new Image(dest.toURI().toURL()+"", 200, 180, true, true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return i;
    }
      	@FXML public void uploadHandler(ActionEvent event) {
            extList = new ArrayList<>();
            extList.add("*.JPG");
            extList.add("*.JPEG");
            extList.add("*.JBIG");
            extList.add("*.PNG");
            extList.add("*.BMP");
            extList.add("*.PNG");
            extList.add("*.BPG");
//            Subjects s=new Subjects(new Subjects().getPrerequisitesList().get(0).getSubjectId().getId());
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("An Image Only", extList));
            chooser.setTitle("Please Chose The Student Personal Photo");
            File f = chooser.showOpenDialog(newStudentView.getScene().getWindow());
            if(f != null){
                System.out.println("\\"+f.getAbsolutePath());
                    stuPhoto.setImage(copyFile(f));
             
            }
            
        }
        public void loadAcadimic() {
            AcadmicYCo.getItems().addAll(acadimic);
            AcadmicYCo.getSelectionModel().selectFirst();
            AcadmicYCo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("1")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Are You Shore");
                alert.setHeaderText("Are You Apsolotly SHORE ??");
                alert.setContentText("Are You Shore That you want to regster this student in the "+newValue+"th Semster ?");
                alert.showAndWait();
            }
            });
        }
        public void loadSemster (){
            emf = Persistence.createEntityManagerFactory("newGuiPU");
            SemesterJpaController semesterJpaController = new SemesterJpaController(emf);
            semsterList = semesterJpaController.findSemesterEntities();
            SemesterCo.getItems().addAll(semsterList);
            System.out.println("ID = "+semsterList.get(0).getSemesterName());
        }
        public  void loadMajore (Department iD){
            if (DepartmentCo.getSelectionModel().isEmpty()) {
                MajoreCo.setDisable(true);
            }else{
                MajoreCo.setDisable(false);
            emf = Persistence.createEntityManagerFactory("newGuiPU");
                SubDepartmentJpaController sdjc = new SubDepartmentJpaController(emf);
                   subList=  sdjc.findSubDepartment(iD.getId());
                    
            MajoreCo.getItems().addAll(subList);
           
            emf.close();
                  }
        }
        public  void loadDepartment(){
            emf = Persistence.createEntityManagerFactory("newGuiPU");
            DepartmentJpaController djc = new DepartmentJpaController(emf);
            deplist = djc.findDepartmentEntities();
            djc.getEntityManager().close();
            emf.close();
            DepartmentCo.getItems().addAll(deplist);
//            DepartmentCo.getSelectionModel().getSelectedItem();
            DepartmentCo.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((observable, oldValue, newValue) -> {
//            System.out.println("ID = "+deplist.get(deplist.indexOf(newValue)).getId());
                MajoreCo.getItems().clear();
//                System.out.println(deplist.get(DepartmentCo.getSelectionModel().getSelectedIndex()).getMajor());
                loadMajore(((Department) newValue));
            });
       
        }
        public  void loadStuts() {
            statusCo.getItems().addAll("Active","Not Active");
        }
	public  void loadCountry() {
                emf = Persistence.createEntityManagerFactory("newGuiPU");
                CountriesJpaController cjc = new CountriesJpaController(emf);
                coulist = cjc.getEntityManager().createNamedQuery("Countries.IdNationality", Countries.class).getResultList();
                countrysCo.getItems().addAll(coulist);
                        cjc.getEntityManager().close();
                        emf.close();
	}
	public void newStudent () {

	if(! (santize(stunameText.getText()).isEmpty() || santize(stuIDText.getText()).isEmpty()
			|| santize(stuScioText.getText()).isEmpty() || santize(stuPhoneText.getText()).isEmpty() 
			|| santize(stuNotesText.getText()).isEmpty() || 
                        santize(stuAdText.getText()).isEmpty() || AcadmicYCo.getSelectionModel().isEmpty()    ||
                        DepartmentCo.getSelectionModel().isEmpty() || MajoreCo.getSelectionModel().isEmpty()  ||
                        SemesterCo.getSelectionModel().isEmpty() || countrysCo.getSelectionModel().getSelectedItem().equals("")  ||
                        statusCo.getSelectionModel().isEmpty() || stuPhoto.getImage().isError() )) {
            emf = Persistence.createEntityManagerFactory("newGuiPU");
            StudentJpaController studentInfo = new StudentJpaController(emf);
            if(studentInfo.findStudent(Integer.parseInt(stuIDText.getText()))== null) {
            //TODO problem with geting the id from the choice box all over dum dum
                          Countries co = coulist.get(coulist.indexOf(countrysCo.getSelectionModel().getSelectedItem()));//TODO 2 get this shit done man !!
                          Semester s = semsterList.get(semsterList.indexOf(SemesterCo.getSelectionModel().getSelectedItem()));
                          SubDepartment sd = subList;
                          System.out.println(co.getNumCode()+"\t"+co.getNationality()+"\n"+s.getId()+"\t"+s.getSemesterName()+"\n"+sd.getId()+"\t"+sd.getSubMajorName());

                          boolean stat = false ;
                          if (statusCo.getSelectionModel().getSelectedIndex()==0) {
                              stat = true;
                              }

                          Student stu = new Student(Integer.parseInt(stuIDText.getText())
                                  , stunameText.getText(), stuAdText.getText(),Integer.parseInt(stuPhoneText.getText())
                                  , stuNotesText.getText(), stat, AcadmicYCo.getSelectionModel().getSelectedItem()+"");
                          stu.setNationality(co);
                          stu.setSocialNumber(stuScioText.getText());
                          stu.setSemsterId(s);
                          stu.setSubMajor(sd);
                          stu.setImage(stuPhoto.getImage().getUrl());
                            
                          
		try {
                    
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Student Was Added ");
                        alert.setContentText("Congratiolation A new Student was Added Succussfully !");
                        alert.showAndWait();

                System.out.println("yope yea done M**r F**r");
                    
                } catch (Exception e) {
			// TODO Auto-generated catch block
                         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Failed ");
                        alert.setContentText( e.getMessage());
                        alert.showAndWait();
			e.printStackTrace();
		}
		}else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Student Already Exsists");
                alert.setHeaderText("Student with Id = "+stuIDText.getText()+" is Already Regstreated ");
                alert.setContentText("Student ID = "+stuIDText.getText()+"\t Name : "+
                        (studentInfo.findStudent(Integer.parseInt(stuIDText.getText())).getName())
                        );
                alert.showAndWait();

                System.out.println("Exsists Dum Dum");
		}
        }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Student went Wrong");
                alert.setHeaderText("Please enter student information again");
                alert.setContentText("Some fields were empty or the photo was'nt selected propopply");
                alert.showAndWait();

                System.out.println("Nothing happend in newstudent dailog dum dum");
		}
	}
	private String santize (String data) {
		if (!data.matches("[^a-z A-Z0-9ا-ي]")) {
            return data.replaceAll("[^a-z A-Z0-9ا-ي]", "");  
    }else {
	return "" ;		
		}
	}

   
	}
