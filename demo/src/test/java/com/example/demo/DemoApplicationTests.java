package com.example.demo;

import com.example.demo.pdf.PdfGenerator;
import com.itextpdf.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() throws Exception {
        String html = PdfGenerator.freemarkerRender(new HashMap<>(), "");
        byte[] pdf = PdfGenerator.createPDF(html, "", "", "");
        OutputStream fileOutputStream = new FileOutputStream("TEST.pdf");
        fileOutputStream.write(pdf);
        fileOutputStream.close();
    }

}
