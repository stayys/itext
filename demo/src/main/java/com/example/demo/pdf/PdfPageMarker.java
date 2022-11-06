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

import java.io.InputStream;

public class PdfPageMarker implements IEventHandler {

    private PdfFont pdfFont;

    private int pageCountNumber;

    public PdfPageMarker(PdfFont pdfFont, int pageCountNumber) {
        this.pdfFont = pdfFont;
        this.pageCountNumber = pageCountNumber;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        int pageNumber = pdf.getPageNumber(page);
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdf);
        Canvas canvas = new Canvas(pdfCanvas, pageSize);
        float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
        float y = pageSize.getBottom() + 15;
        Paragraph p1 = new Paragraph("第" + pageNumber + "页,共"+pageCountNumber+"页")
                .setFontSize(12)
                .setFont(pdfFont);
        Paragraph p2 = new Paragraph("hdfhisahfdshfisdhvijjjjjjjjjjjjjjjjjjriosdv\n"
        +"你好!!!")
                .setFontSize(12)
                .setFont(pdfFont);
        // 底部中间位置
        canvas.showTextAligned(p1, x-20, y-5, TextAlignment.CENTER);
        canvas.showTextAligned(p2, 10, y, TextAlignment.LEFT);
        canvas.close();

    }

}
