package service;

import entity.*;
import repo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student setIsWorking(Long id, Boolean value) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setIsWorking(value);
            studentRepository.save(student);
        }
        return student;
    }
}