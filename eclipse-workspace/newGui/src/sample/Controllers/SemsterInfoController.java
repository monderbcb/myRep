/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.Controllers;

import sample.dbControllers.MacPcJpaController;
import sample.dbControllers.SemesterJpaController;
import sample.dbControllers.UsersJpaController;
import sample.dbControllers.exceptions.IllegalOrphanException;
import sample.dbControllers.exceptions.NonexistentEntityException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sample.DbTables.Grade;
import sample.DbTables.InstructorSub;
import sample.DbTables.Instructors;
import sample.DbTables.Semester;
import sample.DbTables.Student;

/**
 * FXML Controller class
 *
 * @author monder
 */
public class SemsterInfoController implements Initializable {

    @FXML
    private TableView<Semester> TableStuffT;
    @FXML
    private TableColumn<Semester, Integer> idC;
    @FXML
    private TableColumn<Semester, String> nameC;
    @FXML
    private TableColumn<Semester, Date> StartC;
    @FXML
    private TableColumn<Semester, Date> endC;
    @FXML
    private TableColumn<Semester, String> subjectssStudiedC;
    @FXML
    private TableColumn<Semester, String> studentPayC;
    @FXML
    private TableColumn<Semester, String> studentRegesterdC;
    @FXML
    private TextField semsterIdT;
    @FXML
    private TextField semsterNameT;
    @FXML
    private DatePicker semsterStartD;
    @FXML
    private DatePicker semsterEndD;
    @FXML
    private Tab semInfoTabe;

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button newSemB;
    @FXML
    private Button editeSemB;
    @FXML
    private Button deleteSemB;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory
        ("newGuiPU");
    private SemesterJpaController sjc = new SemesterJpaController(emf);
    private ObservableList<Semester> semesterListOrignal;
    private ObservableList <Grade> gradesOnSemList;
    private ObservableList <InstructorSub> instructorSubjectOnSemList;
    private ObservableList <Instructors> instructorInfoOnSemList;
    private ObservableList <Student> studentInfoOnSemList;
    private Semester selctedSemster ;
    private Semester editedSemster ;
    private Semester newSemster;
    private String sName;
    private int studentInStudentSubList;
    private int MAX_USER_ID;
    private int payed_subj;
    @FXML
    private Label rgStudentSemL;
    @FXML
    private Label regGradeSemL;
    @FXML
    private Label regStuSubSemL;
    @FXML
    private Label regInstructSemL;
    @FXML
    private Label regStudPaySemL;
    @FXML
    private Label regIrnstructorSubListL;
    @FXML
    private Tab semsterAssoicTabe;
    @FXML
    private TableView<Grade> gradeTableInSemT;
    @FXML
    private TableColumn<Grade, String> colIdGradeC;
    @FXML
    private TableColumn<Grade, Short> colMiddlGradeC;
    @FXML
    private TableColumn<Grade, Short> colFinalGradeC;
    @FXML
    private TableColumn<Grade, String> colResGradeC;
    @FXML
    private Label selectedSemsterLabel;
    @FXML
    private TableView<InstructorSub> instructSubSemsTable;
    @FXML
    private TableColumn<InstructorSub, String> colNamInstruSubTaC;
    @FXML
    private TableColumn<InstructorSub, String> colSubjInstructorSubTaC;
    @FXML
    private TableColumn<InstructorSub, Integer> colGroupInstructSuTac;
    @FXML
    private TableView<Instructors> instructorInfoTable;
    @FXML
    private TableColumn<Instructors, String> colInstructNameInstructTabC;
    @FXML
    private TableColumn<Instructors, String> colDepartmInstructInfoTabC;
    @FXML
    private TableColumn<Instructors, String> colDegreInstroctInfoTabC;
    @FXML
    private TableColumn<Instructors, String> colMjorInstructInoTabC;
    @FXML
    private TableColumn<Instructors, String> colIsDeanInstructInfoTabC;
    @FXML
    private TableColumn<Student, Integer> colStuIDSemsTabC;
    @FXML
    private TableColumn<Student, String> colNamStuSemsTabC;
    @FXML
    private TableView<Student> stuSemsTable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void loadItAllSemsterInfo(){
        semesterListOrignal = FXCollections.observableArrayList(sjc.findSemesterEntities());    
        TableStuffT.setItems(semesterListOrignal);
        idC.setCellValueFactory(
                new PropertyValueFactory("id"));
        nameC.setCellValueFactory(
                new PropertyValueFactory("semesterName"));
        StartC.setCellValueFactory(
                new PropertyValueFactory("semesterStart"));
        endC.setCellValueFactory(
                new PropertyValueFactory("semesterEnd"));
        subjectssStudiedC.setCellValueFactory(cellData -> {
                          return  new ReadOnlyStringWrapper (cellData.getValue().getStudentSubList().size()+"");
                      });
        studentPayC.setCellValueFactory(cellData -> {                          
                          semesterListOrignal.forEach((t) -> {
                          t.getStudentSubList().forEach((tt) -> {
                              tt.getStudentPaymentList().forEach((ttt) -> {
                                  if (ttt.getStutes().equals("Fully Paied")) {
                                      payed_subj++;
                                    }
                                });
                            });
                          });
                        return  new ReadOnlyStringWrapper (payed_subj+"");});
        studentRegesterdC.setCellValueFactory(cellData -> {
                          return  new ReadOnlyStringWrapper (cellData.getValue().getStudentList().size()+"");
                      });
        TableStuffT.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                editeSemB.setDisable(false);
                deleteSemB.setDisable(false);
                selctedSemster = newValue;
                semsterAssoicTabe.setDisable(false);
                semsterNameT.setText(newValue.getSemesterName());
                semsterIdT.setText(newValue.getId()+"");
                semsterStartD.setValue(newValue.getSemesterStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                semsterEndD.setValue(LocalDate.ofInstant(newValue.getSemesterEnd().toInstant() , ZoneId.systemDefault()));
                      semsterStartD.valueProperty().addListener((observablee, oldValuee, newValuee) -> {
            if (newValuee != null) {
                switch (newValuee.getMonthValue()) {
                    case 3:
                    case 4:
                    case 5:
                        semsterNameT.setText(semsterStartD.getValue().getYear()+"SP");
                        semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear(), 5, 31));
                        break;
                    case 6:
                    case 7:
                    case 8:
                        semsterNameT.setText(semsterStartD.getValue().getYear()+"SU");
                        semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear(), 8, 31));
                        break;
                    case 9:
                    case 10:
                    case 11:
                        semsterNameT.setText(semsterStartD.getValue().getYear()+"AU");
                        semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear(), 11, 30));
                        break;
                    case 12:
                        semsterNameT.setText((semsterStartD.getValue().getYear())+"WI");
                        semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear()+1, 2, 28));
                        break;
                    default:
                        semsterNameT.setText(semsterStartD.getValue().getYear()+"WI");
                        semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear(), 2, 28));
                        break;
                }
                studentInStudentSubList = 0;
            }
        });
  
            }
        });
        
    }

    @FXML
    private void semInfoTabeHand(Event event) {
                loadItAllSemsterInfo();

    }

    @FXML
    private void newSemesterHand(ActionEvent event) {
        if (newSemB.getText().equals("Open New Semster")) {
            MAX_USER_ID = sjc.getSemesterCount();
            semsterEndD.setValue(null);
            semsterIdT.setText((MAX_USER_ID+1)+"");
            semsterNameT.setText("");
            semsterNameT.setEditable(false);
            editeSemB.setDisable(true);
            deleteSemB.setDisable(true);
            semsterStartD.setValue(null);
//            semsterIdT.setEditable(true);
            newSemB.setText("Enter the New Semester");
                semsterStartD.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        switch (newValue.getMonthValue()) {
                            case 3:
                            case 4:
                            case 5:
                                semsterNameT.setText(semsterStartD.getValue().getYear()+"SP");
                                semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear(), 5, 31));
                                break;
                            case 6:
                            case 7:
                            case 8:
                                semsterNameT.setText(semsterStartD.getValue().getYear()+"SU");
                                semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear(), 8, 31));
                                break;
                            case 9:
                            case 10:
                            case 11:
                                semsterNameT.setText(semsterStartD.getValue().getYear()+"AU");
                                semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear(), 11, 30));
                                break;
                            case 12:
                                semsterNameT.setText((semsterStartD.getValue().getYear())+"WI");
                                semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear()+1, 2, 28));
                                break;
                            default:
                                semsterNameT.setText(semsterStartD.getValue().getYear()+"WI");
                                semsterEndD.setValue(LocalDate.of(semsterStartD.getValue().getYear(), 2, 28));
                                break;
                        }
                    }
                });
                

        }else{
                sName=semsterNameT.getText();
                if (semsterStartD.getValue() != null) {
                    semesterListOrignal.forEach((t) -> {
                        if(t.getSemesterName().equalsIgnoreCase(sName)){
                        sName = "t";
                        }
                    });
                    if (!sName.equals("t")) {
                        if (semsterStartD.getValue().plusMonths(1).compareTo(semsterEndD.getValue()) < 0) { 
                                                                //        sesionC.getItems().addAll("Spring","Summer","Fall (autumn)","Winter");
                        newSemster = new Semester(Integer.parseInt(semsterIdT.getText()), semsterNameT.getText()
                                , java.sql.Date.valueOf(semsterStartD.getValue()),
                                java.sql.Date.valueOf(semsterEndD.getValue()));
        //                System.out.println(semsterNameT.getText().subSequence(0, 4).equals(semsterStartD.getValue().getYear()+""));
                        sjc.create(newSemster);
            //                    semsterIdT.setEditable(false);
                        semesterListOrignal.add(newSemster);
                        newSemB.setText("Open New Semster");
                        editeSemB.setDisable(false);
                        deleteSemB.setDisable(false);
                        TableStuffT.getSelectionModel().clearSelection();
                        alertTypesShow(Alert.AlertType.INFORMATION, "Opening New Semester !",
                            "The New Semester Was Added Correctly \n And It's Ready to Sign Students In");
                        }else{
                            alertTypesShow(Alert.AlertType.ERROR, "Opening New Semester !",
                            "The Start or The End Date Of The Semester is Invalid \n Please Check Again !");
                        }
                    }else{
                       alertTypesShow(Alert.AlertType.ERROR, "Opening New Semester !",
                               "The Semester with The Name ' "+semsterNameT.getText()+" ' Already Exsists !");
                    }
            }
        }
        
    }

    @FXML
    private void editeSemesterHand(ActionEvent event) {
        semsterNameT.setEditable(false);
        if (semsterStartD.getValue().plusMonths(1).compareTo(semsterEndD.getValue()) < 0) { 
            try {                
            editedSemster = new Semester(selctedSemster.getId()
                    , semsterNameT.getText(), java.sql.Date.valueOf(semsterStartD.getValue()), 
                    java.sql.Date.valueOf(semsterEndD.getValue())); 
            editedSemster.setGradeList(selctedSemster.getGradeList());
            editedSemster.setInstructorSubList(selctedSemster.getInstructorSubList());
            editedSemster.setInstructorsList(selctedSemster.getInstructorsList());
            editedSemster.setStudentList(selctedSemster.getStudentList());
            editedSemster.setStudentSubList(selctedSemster.getStudentSubList());
            editedSemster.setStudentSubList(selctedSemster.getStudentSubList());
            
            sjc.edit(editedSemster);
            semesterListOrignal.replaceAll((t) -> {
                if (Objects.equals(t.getId(), editedSemster.getId())) {
                    return editedSemster;
                }else
                return t; 
            });
                 TableStuffT.getSelectionModel().clearSelection();

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SemsterInfoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SemsterInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }else{
            alertTypesShow(Alert.AlertType.ERROR, "Opening New Semester !",
            "The Start or The End Date Of The Semester is Invalid \n Please Check Again !");
        }               
        
    }

    @FXML
    private void deleteSemesterHand(ActionEvent event) {
            try {
                sjc.destroy(selctedSemster.getId());
                semesterListOrignal.remove(selctedSemster);
            } catch (IllegalOrphanException | NonexistentEntityException ex) {
                Logger.getLogger(SemsterInfoController.class.getName()).log(Level.SEVERE, null, ex);
                alertTypesShow(Alert.AlertType.ERROR, "Deleting a Semester !",ex.getMessage());
            }
    }
    private void alertTypesShow(Alert.AlertType ALERTTYPE, String TITLE_MESS, String MESSAGE_BODY ){
        Alert alert = new Alert(ALERTTYPE);
        alert.setTitle(TITLE_MESS);
        alert.setContentText(MESSAGE_BODY);
        alert.showAndWait();
    }   

    @FXML
    private void semsterAccHand(Event event) {
        if (selctedSemster != null) {
            selectedSemsterLabel.setText("The Selected Semester is  "+selctedSemster.getSemesterName());
            if (selctedSemster.getGradeList() != null && selctedSemster.getGradeList().size() > 0) {       
                gradesOnSemList = FXCollections.observableArrayList(selctedSemster.getGradeList());
                gradeTableInSemT.setItems(gradesOnSemList);
                colIdGradeC.setCellValueFactory(cellData -> {
                                  return  new ReadOnlyStringWrapper (cellData.getValue().getStudentSub().getSubjectId().getSubjectName());
                              });
                colMiddlGradeC.setCellValueFactory(
                        new PropertyValueFactory("middleExam"));
                colFinalGradeC.setCellValueFactory(
                        new PropertyValueFactory("finalExam"));
                colResGradeC.setCellValueFactory(
                        new PropertyValueFactory("finalResault"));
            }else{
                gradeTableInSemT.setDisable(true);
            }
            if (selctedSemster.getInstructorSubList() != null && selctedSemster.getInstructorSubList().size() > 0) {       
                instructorSubjectOnSemList = FXCollections.observableArrayList(selctedSemster.getInstructorSubList());
                instructSubSemsTable.setItems(instructorSubjectOnSemList);
                colNamInstruSubTaC.setCellValueFactory(cellData -> {
                                  return  new ReadOnlyStringWrapper (cellData.getValue().getInstructorId().getName());
                              });
                colSubjInstructorSubTaC.setCellValueFactory(cellData -> {
                                  return  new ReadOnlyStringWrapper (cellData.getValue().getSubjectId().getSubjectName());
                              });
                colGroupInstructSuTac.setCellValueFactory(
                        new PropertyValueFactory("groupNumber"));
            }else{
                instructSubSemsTable.setDisable(true);
            }
            if (selctedSemster.getInstructorsList() !=  null && selctedSemster.getInstructorsList().size() > 0) {
                instructorInfoOnSemList = FXCollections.observableArrayList(selctedSemster.getInstructorsList());
                instructorInfoTable.setItems(instructorInfoOnSemList);
                colInstructNameInstructTabC.setCellValueFactory(
                        new PropertyValueFactory("name"));
                colDepartmInstructInfoTabC.setCellValueFactory(cellData -> {
                                  return  new ReadOnlyStringWrapper (cellData.getValue().getDepartment().getMajor());
                              });
                colDegreInstroctInfoTabC.setCellValueFactory(
                        new PropertyValueFactory("degree"));
                colMjorInstructInoTabC.setCellValueFactory(
                        new PropertyValueFactory("majoresIn"));
                colIsDeanInstructInfoTabC.setCellValueFactory(cellData -> {
                                  return  new ReadOnlyStringWrapper (cellData.getValue()
                                          .getDeansList().isEmpty()?"No":"Yes IN , "+cellData.getValue()
                                                  .getDeansList().iterator().next()
                                                  .getMajor());
                              });
            }else{
                instructorInfoTable.setDisable(true);
            }
            if (selctedSemster.getStudentList() !=  null && selctedSemster.getStudentList().size() > 0) {
                studentInfoOnSemList = FXCollections.observableArrayList(selctedSemster.getStudentList());
                stuSemsTable.setItems(studentInfoOnSemList);
                colStuIDSemsTabC.setCellValueFactory(
                        new PropertyValueFactory("studentId"));
                colNamStuSemsTabC.setCellValueFactory(
                        new PropertyValueFactory("name"));
            }else{
              stuSemsTable.setDisable(true);
            }
        }else {
            semsterAssoicTabe.setDisable(true);
        }
    }
}
