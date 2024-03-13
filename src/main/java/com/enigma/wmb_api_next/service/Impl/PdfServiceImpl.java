package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.response.BillDetailResponse;
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
    private static final int PAGE_HEIGHT = 792;
    private static final int MARGIN = 50;

    @Override
    public byte[] generatePdf(List<BillResponse> bills) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            int yPos = PAGE_HEIGHT - MARGIN;

            for (BillResponse bill : bills) {
                if (yPos <= MARGIN + 200) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    yPos = PAGE_HEIGHT - MARGIN;
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPos);
                contentStream.showText("Bill ID: " + bill.getId());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Transaction Date: " + bill.getTransDate());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Transaction Type: " + bill.getTransType());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Customer Name: " + bill.getCustomerName());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Table: " + bill.getTableName());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Payment ID: " + bill.getPayment().getId());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Status: " + bill.getPayment().getTransactionStatus());
                contentStream.endText();

                yPos -= 150;

                for (BillDetailResponse detail : bill.getBilldetails()) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, yPos);
                    contentStream.showText("Menu ID: " + detail.getMenuId());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Quantity: " + detail.getQty());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Price: " + detail.getPrice());
                    contentStream.endText();
                    yPos -= 60;
                }

                yPos -= 20;
            }

            contentStream.close();
            document.save(out);

            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error generating PDF", e);
            return new byte[0];
        }
    }
}
