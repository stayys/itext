package com.example.demo.pdf;


import cn.hutool.core.io.IoUtil;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.InputStream;

public class PdfHeaderMarker implements IEventHandler {

//    private PdfFont pdfFont;
//    private String title;
//
//    public PdfHeaderMarker(PdfFont pdfFont, String title) {
//        this.pdfFont = pdfFont;
//        this.title = title;
//    }
//
//    @Override
//    public void handleEvent(Event event) {
//        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
//        PdfDocument pdf = docEvent.getDocument();
//        PdfPage page = docEvent.getPage();
//        Rectangle pageSize = page.getPageSize();
//        PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdf);
//        Canvas canvas = new Canvas(pdfCanvas, pageSize);
//        float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
//        float y = pageSize.getTop() - 25;
//        Paragraph p = new Paragraph(title)
//                .setFontSize(12)
//                .setFont(pdfFont);
//        // 顶部中间位置
//        canvas.showTextAligned(p, x, y, TextAlignment.CENTER);
//        canvas.close();
//
//    }

    @SneakyThrows
    @Override
    public void handleEvent(Event event) {
        final PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        final PdfDocument pdfDoc = docEvent.getDocument();
        final Document doc = new Document(pdfDoc);
        final PdfPage page = docEvent.getPage();
        final Rectangle pageSize = page.getPageSize();
        final float pdfWidth = pageSize.getWidth();
        final float pdfHeight = pageSize.getHeight();
        final float tableWidth = pdfWidth - doc.getRightMargin() - doc.getLeftMargin();
        final Table headerTable = new Table(2);
        headerTable.setFixedLayout();
        headerTable.setWidth(tableWidth);
        headerTable.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // 设置图片
        InputStream is = new FileInputStream("C:\\Users\\star\\IdeaProjects\\itext\\demo\\src\\main\\resources\\logo.png");
        byte[] bytes = IoUtil.readBytes(is);
        Image img = new Image(ImageDataFactory.create(bytes));
        img.setHeight(70);
        Paragraph imgParagraph = new Paragraph().add(img);
        imgParagraph.setMarginLeft(-20);
        Cell imgCell = new Cell();
        imgCell.setBorder(Border.NO_BORDER);
        imgCell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        imgCell.setVerticalAlignment(VerticalAlignment.BOTTOM);
        imgCell.setTextAlignment(TextAlignment.LEFT);
        imgCell.add(imgParagraph);
        headerTable.addCell(imgCell);

        headerTable.setFixedPosition(0, pdfHeight - 75, tableWidth);
        doc.add(headerTable);
    }


}
