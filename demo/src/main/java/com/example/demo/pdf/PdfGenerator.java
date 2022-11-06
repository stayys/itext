package com.example.demo.pdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.OutlineHandler;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.tomcat.util.buf.StringUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

public class PdfGenerator {

    private PdfGenerator() {
        // 构造器私有化
    }

    public static int getPageNumber(String htmlTmpStr,ConverterProperties properties){
        List<IElement> iElements = HtmlConverter.convertToElements(htmlTmpStr, properties);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream, new WriterProperties().setFullCompressionMode(Boolean.TRUE));
        PdfDocument doc = new PdfDocument(writer);
        int pageCountNumber = 0;
        try {
            doc.setDefaultPageSize(PageSize.A4);
            Document document = new Document(doc);
            document.setBottomMargin(50f);
            document.setTopMargin(80f);
            document.setLeftMargin(0f);
            document.setRightMargin(0f);
            for (int i = 0; i < iElements.size(); i++) {
                IElement iElement = iElements.get(i);
                document.add((IBlockElement) iElement);
            }
            pageCountNumber = doc.getNumberOfPages();
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                doc.close();
                writer.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pageCountNumber;
    }

    public static byte[] createPDF(String htmlTmpStr, String fontFilePath, String waterImgPath, String waterContent) {
        byte[] result = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream, new WriterProperties().setFullCompressionMode(Boolean.TRUE));
        PdfDocument doc = new PdfDocument(writer);
        try {
            doc.setDefaultPageSize(PageSize.A4);
            FontProvider fontProvider = new FontProvider();
            PdfFont pdfFont = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            fontProvider.addFont(pdfFont.getFontProgram(), "UniGB-UCS2-H");

            ConverterProperties properties = new ConverterProperties();
            fontProvider.addStandardPdfFonts();
            fontProvider.addSystemFonts();
            properties.setFontProvider(fontProvider);

            // PDF目录
            properties.setOutlineHandler(OutlineHandler.createStandardHandler());


            int pageNumber = getPageNumber(htmlTmpStr,properties);
            // 添加页眉
            doc.addEventHandler(PdfDocumentEvent.START_PAGE, new PdfHeaderMarker());
            // 添加页脚
            doc.addEventHandler(PdfDocumentEvent.END_PAGE, new PdfPageMarker(pdfFont,pageNumber));

            // 将html转为pdf代码块，按div生成每天一页pdf
            Document document = new Document(doc);
            document.setBottomMargin(50f);
            document.setTopMargin(80f);
            document.setLeftMargin(0f);
            document.setRightMargin(0f);
            List<IElement> iElements = HtmlConverter.convertToElements(htmlTmpStr, properties);
            int size = iElements.size();
            for (int i = 0; i < size; i++) {
                IElement iElement = iElements.get(i);
                document.add((IBlockElement) iElement);
                if (!(i == size - 1)) { // 不是最后一页
                    // 添加新的一页
//                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
            }
            document.close();
            result = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                doc.close();
                writer.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String freemarkerRender(Map<String, Object> dataMap, String ftlFilePath) {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Writer out = new StringWriter();
        try {
            Template template = new Template("",new BufferedReader(new InputStreamReader(PdfGenerator.class.getClassLoader().getResourceAsStream("test.html"))) , configuration);
            template.process(dataMap, out);
            out.flush();
            return out.toString();
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

