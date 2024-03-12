package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.response.BillResponse;
import com.enigma.wmb_api_next.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PdfServiceImpl implements PdfService {
    @Override
    public byte[] generatePdf(List<BillResponse> bills) {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.showText("Bill List:");
            contentStream.newLine();

            for (BillResponse bill : bills) {
                contentStream.showText(bill.toString());
                contentStream.newLine();
            }

            contentStream.endText();
            contentStream.close();
            document.save(out);
            document.close();
            return out.toByteArray();

        } catch (IOException e) {
            log.error("Error generate pdf", e.getMessage());
            return new byte[0];
        }
    }
}
