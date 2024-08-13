package com.miboutaj.springbootwithjasperreport.service;


import com.github.javafaker.Faker;
import com.miboutaj.springbootwithjasperreport.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StudentService {

    public List<Student> getListStudents(int numberOfStudents){
        List<Student> students = new ArrayList<>();
        for(int i=0; i<numberOfStudents; i++){
            Faker faker = new Faker();
            Student student = Student.builder()
                    .id(faker.idNumber().hashCode())
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .note((int) (Math.random() * 11) + 10).build();
            students.add(student);
        }

        return students;

    }
}
