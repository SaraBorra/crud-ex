package controller;

import entity.*;
import repo.*;
import service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/")
    public @ResponseBody Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @GetMapping("/")
    public @ResponseBody List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @PutMapping("/set/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student newStudent) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setName(newStudent.getName());
            student.setSurname(newStudent.getSurname());
            return studentRepository.save(student);
        }
        return null;
    }

    @PutMapping("/setIsWorking/{id}")
    public @ResponseBody Student updateIsWorking(@PathVariable Long id, @RequestParam boolean working) {
        return studentService.setIsWorking(id, working);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }
}
