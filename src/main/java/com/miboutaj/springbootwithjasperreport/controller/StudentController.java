package com.miboutaj.springbootwithjasperreport.controller;


import ch.qos.logback.core.model.Model;
import com.miboutaj.springbootwithjasperreport.model.Student;
import com.miboutaj.springbootwithjasperreport.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/students/{number}")
    public ResponseEntity<List<Student>> getStudents(@PathVariable int number) {
        System.out.println(studentService.getListStudents(number));
        return ResponseEntity.ok(studentService.getListStudents(number));

    }
}
