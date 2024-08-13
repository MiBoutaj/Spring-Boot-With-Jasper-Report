package com.miboutaj.springbootwithjasperreport.controller;


import ch.qos.logback.core.model.Model;
import com.miboutaj.springbootwithjasperreport.model.Student;
import com.miboutaj.springbootwithjasperreport.service.StudentService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import net.sf.jasperreports.export.Exporter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudentController {

    @Value("${generated.doc.student_list}")
    private String pathToInvoiceListFile;

    private final StudentService studentService;

    @GetMapping("/students/list/pdf/{number}")
    public ResponseEntity<byte[]> getStudentsPDF(@PathVariable int number) throws FileNotFoundException, JRException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDate = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=%s.pdf", currentDate);
        headers.setContentDispositionFormData(headerKey, headerValue);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentService.getListStudents((number)));

        File file = ResourceUtils.getFile(pathToInvoiceListFile);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,dataSource);
        byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return new ResponseEntity<byte[]>(bytes,headers, HttpStatus.OK);

    }

    @GetMapping("/students/list/HTML/{number}")
    public ResponseEntity<byte[]> getStudentsHTML(@PathVariable int number) throws FileNotFoundException, JRException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDate = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=%s.html", currentDate);
        headers.setContentDispositionFormData(headerKey, headerValue);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentService.getListStudents((number)));

        File file = ResourceUtils.getFile(pathToInvoiceListFile);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null,dataSource);


        final Exporter exporter;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        exporter = new HtmlExporter();
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(out));
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.exportReport();

        return new ResponseEntity<byte[]>(out.toByteArray(),headers, HttpStatus.OK);

    }
}
