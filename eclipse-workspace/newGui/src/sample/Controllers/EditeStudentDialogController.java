/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.Controllers;

import sample.dbControllers.CountriesJpaController;
import sample.dbControllers.DepartmentJpaController;
import sample.dbControllers.SemesterJpaController;
import sample.dbControllers.StudentJpaController;
import sample.dbControllers.SubDepartmentJpaController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sample.DbTables.Countries;
import sample.DbTables.Department;
import sample.DbTables.Semester;
import sample.DbTables.Student;
import sample.DbTables.SubDepartment;

/**
 * FXML Controller class
 *
 * @author monder
 */
public class EditeStudentDialogController implements Initializable {

    @FXML
    private DialogPane editStudentView;
    @FXML
    private ImageView stuPhoto;
    @FXML
    private TextField stunameText;
    @FXML
    private TextField stuIDText;
    @FXML
    private TextField stuScioText;
    @FXML
    private TextField stuPhoneText;
    @FXML
    private TextArea stuAdText;
    @FXML
    private Button uploadB;
    @FXML
    private ChoiceBox statusCo;
    @FXML
    private ChoiceBox DepartmentCo;
    @FXML
    private ChoiceBox MajoreCo;
    @FXML
    private ChoiceBox AcadmicYCo;
    @FXML
    private TextArea stuNotesText;
    @FXML
    private ComboBox countrysCo;
    @FXML
    private ChoiceBox SemesterCo;
    private Student studentListOrignal;
    
    private EntityManagerFactory emf ;
    private List<Department> deplist;
    private List<Countries> coulist;
    private SubDepartment subList;
    private List <String> extList ;
    private final String [] acadimic={"1","2","3","4","5","6","7","8","9"};
    private List <Semester> semsterList ;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        studentListOrignal = studentController.getStedentList();
        if (studentListOrignal!=null) {
            loadAllFields(studentListOrignal);

        }
    }    
    public void loadAllFields(Student studentListOrignal){
        loadCountry();
        loadStuts();
        loadDepartment();
        loadSemster();
        loadAcadimic();
//        MajoreCo.getSelectionModel().select(studentListOrignal.getSubMajor());

        stunameText.setText(studentListOrignal.getName());
        stuAdText.setText(studentListOrignal.getAddress());
        stuIDText.setText(studentListOrignal.getStudentId()+"");
        stuPhoneText.setText(studentListOrignal.getPhone()+"");
        stuScioText.setText(studentListOrignal.getSocialNumber()+"");
        countrysCo.getSelectionModel().select(studentListOrignal.getNationality());
        stuNotesText.setText(studentListOrignal.getNotes());
        stuPhoto.setImage(new Image (studentListOrignal.getImage(),200 ,180 ,true , true));
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

        public void loadAcadimic() {
            AcadmicYCo.getItems().addAll(acadimic);
            AcadmicYCo.getSelectionModel().select(studentListOrignal.getSemesterCount());
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
            SemesterCo.getSelectionModel().select(studentListOrignal.getSemsterId());
            System.out.println("ID = "+semsterList.get(0).getSemesterName()+"\n"+studentListOrignal.getSemsterId().getSemesterName());
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
            MajoreCo.getSelectionModel().select(studentListOrignal.getSubMajor());
           
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
            DepartmentCo.getSelectionModel().select(studentListOrignal.getSubMajor().getMajorName());
//            DepartmentCo.getSelectionModel().getSelectedItem();
            MajoreCo.getItems().setAll(studentListOrignal.getSubMajor());
            MajoreCo.getSelectionModel().select(studentListOrignal.getSubMajor());

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
            statusCo.getSelectionModel().select(studentListOrignal.getStatus()?"Active":"Not Active");
        }
	public  void loadCountry() {
                emf = Persistence.createEntityManagerFactory("newGuiPU");
                CountriesJpaController cjc = new CountriesJpaController(emf);
                coulist = cjc.getEntityManager().createNamedQuery("Countries.IdNationality", Countries.class).getResultList();
                countrysCo.getItems().addAll(coulist);
                countrysCo.getSelectionModel().select(studentListOrignal.getNationality());
                        cjc.getEntityManager().close();
                        emf.close();
	}

    @FXML
    private void upl(ActionEvent event) {

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
        File f = chooser.showOpenDialog(editStudentView.getScene().getWindow());
        if(f != null){
            System.out.println("\\"+f.getAbsolutePath());
                stuPhoto.setImage(copyFile(f));

        }
    }
    	public void editStudent () {

	if(! (santize(stunameText.getText()).isEmpty() || santize(stuIDText.getText()).isEmpty()
			|| santize(stuScioText.getText()).isEmpty() || santize(stuPhoneText.getText()).isEmpty() 
			|| santize(stuNotesText.getText()).isEmpty() || 
                        santize(stuAdText.getText()).isEmpty() || AcadmicYCo.getSelectionModel().isEmpty()    ||
                        DepartmentCo.getSelectionModel().isEmpty() || MajoreCo.getSelectionModel().isEmpty()  ||
                        SemesterCo.getSelectionModel().isEmpty() || countrysCo.getSelectionModel().getSelectedItem().equals("")  ||
                        statusCo.getSelectionModel().isEmpty() || stuPhoto.getImage().isError() )) {
            emf = Persistence.createEntityManagerFactory("newGuiPU");
            StudentJpaController studentInfo = new StudentJpaController(emf);
            if(studentInfo.findStudent(Integer.parseInt(stuIDText.getText()))!= null) {
            //TODO problem with geting the id from the choice box all over dum dum
                          Countries co = coulist.get(coulist.indexOf(countrysCo.getSelectionModel().getSelectedItem()));//TODO 2 get this shit done man !!
                          Semester s = semsterList.get(semsterList.indexOf(SemesterCo.getSelectionModel().getSelectedItem()));
                          SubDepartment sd = (SubDepartment)MajoreCo.getSelectionModel().getSelectedItem();
                          
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
                 
                        studentInfo.edit(stu);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Student Was edited ");
                        alert.setContentText("Congratiolation A new Student was edited Succussfully !");
                        alert.showAndWait();

                System.out.println("yope yea done M**r F**r");
                    
                } catch (Exception e) {
			// TODO Auto-generated catch block
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        
                        alert.setTitle("Editing this Student info Failed ! ");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
			e.printStackTrace();
		}
		}else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Student Dosent Exsists");
                alert.setHeaderText("Student with Id = "+stunameText.getText()+" is Not Regstreated ");
                alert.setContentText("Student ID = "+stuIDText.getText());
                alert.showAndWait();

                System.out.println("Does Not Exsists Dum Dum");
		}
        }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Student went Wrong");
                alert.setHeaderText("Please enter student information again");
                alert.setContentText("Some fields were empty or the photo was'nt selected propopply");
                alert.showAndWait();

                System.out.println("Nothing happend in editestudent dailog dum dum");
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
