package com.luv2code.springmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.Gradebook;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.service.StudentAndGradeService;


@Controller
public class GradebookController {

    @Autowired
    private Gradebook gradebook;

	@Autowired
	private StudentAndGradeService studentAndGradeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStudents(Model m) {
        Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradebook();
		m.addAttribute("students", collegeStudents);
		return "index";
    }

    @GetMapping("/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model m) {
        if(!studentAndGradeService.checkIfStudentIsNull(id)){
            return "error";
        }

        GradebookCollegeStudent studentEntity = studentAndGradeService.studentInformation(id);
        m.addAttribute("student", studentEntity);
        if(studentEntity.getStudentGrades().getMathGradeResults().size() > 0) {
            m.addAttribute("mathAverage", studentEntity.getStudentGrades().
            findGradePointAverage(studentEntity.getStudentGrades().getMathGradeResults()));
        }else{
            m.addAttribute("mathAverage", "N/A");
        }
        if(studentEntity.getStudentGrades().getScienceGradeResults().size() > 0) {
            m.addAttribute("scienceAverage", studentEntity.getStudentGrades().
            findGradePointAverage(studentEntity.getStudentGrades().getScienceGradeResults()));
        }else{
            m.addAttribute("scienceAverage", "N/A");
        }
        if(studentEntity.getStudentGrades().getHistoryGradeResults().size() > 0) {
            m.addAttribute("historyAverage", studentEntity.getStudentGrades().
            findGradePointAverage(studentEntity.getStudentGrades().getHistoryGradeResults()));
        }else{
            m.addAttribute("historyAverage", "N/A");
        }

        return "studentInformation";
    }

    //Creates a student
    @PostMapping("/")
    //When the POST is made the data populated into an instance of CollegeStudent.
    //Model allows to pass the data to the view.
    public String createStudent(@ModelAttribute("student") CollegeStudent student, Model m) {  
        studentAndGradeService.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
        //Name of the view to be rendered.     
        Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradebook();
        m.addAttribute("students", collegeStudents);
        return "index";
    }

    @GetMapping("/delete/student/{id}")
    public String deleteStudent(@PathVariable int id, Model m){

        if(!studentAndGradeService.checkIfStudentIsNull(id)){
            return "error";
        }
        studentAndGradeService.deleteStudent(id);
        Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradebook();
        //Update list of students view. 
        m.addAttribute("students", collegeStudents);
        return "index";
    }

    @PostMapping(value = "/grades")
    public String createGrade(@RequestParam("grade") double grade,
                              @RequestParam("gradeType") String gradeType,
                              @RequestParam("studentId") int studentId, 
                              Model m) {

        if(!studentAndGradeService.checkIfStudentIsNull(studentId)){
            return "error";
        }

        boolean success = studentAndGradeService.createGrade(grade, studentId, gradeType);

        if(!success){
            return "error";
        }

        GradebookCollegeStudent studentEntity = studentAndGradeService.studentInformation(studentId);
        m.addAttribute("student", studentEntity);
        if(studentEntity.getStudentGrades().getMathGradeResults().size() > 0) {
            m.addAttribute("mathAverage", studentEntity.getStudentGrades().
            findGradePointAverage(studentEntity.getStudentGrades().getMathGradeResults()));
        }else{
            m.addAttribute("mathAverage", "N/A");
        }
        if(studentEntity.getStudentGrades().getScienceGradeResults().size() > 0) {
            m.addAttribute("scienceAverage", studentEntity.getStudentGrades().
            findGradePointAverage(studentEntity.getStudentGrades().getScienceGradeResults()));
        }else{
            m.addAttribute("scienceAverage", "N/A");
        }
        if(studentEntity.getStudentGrades().getHistoryGradeResults().size() > 0) {
            m.addAttribute("historyAverage", studentEntity.getStudentGrades().
            findGradePointAverage(studentEntity.getStudentGrades().getHistoryGradeResults()));
        }else{
            m.addAttribute("historyAverage", "N/A");
        }

        return "studentInformation";
    }

    


}
