package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.response.BillResponse;
import com.enigma.wmb_api_next.service.PdfService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
@Slf4j
public class PdfServiceImpl implements PdfService {
    private final List<BillResponse> billResponseList;

    public PdfServiceImpl(List<BillResponse> billResponseList) {
        this.billResponseList = billResponseList;
    }

    private void writeTabHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(12);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Bill ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Transaction Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Transaction Type", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Customer Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Table", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Payment ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Status", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (BillResponse bill : billResponseList) {
            table.addCell(bill.getId());
            table.addCell(bill.getTransDate().toString());
            table.addCell(bill.getTransType());
            table.addCell(bill.getCustomerName());
            table.addCell(bill.getTableName());
            table.addCell(bill.getPayment().getId());
            table.addCell(bill.getPayment().getTransactionStatus());
        }
    }

    @Override
    public void export(HttpServletResponse response) throws DocumentException {
        try (Document document = new Document(PageSize.A4.rotate())) {
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.HELVETICA);
            font.setColor(Color.BLUE);

            Paragraph billReports = new Paragraph("Bill Reports", font);
            billReports.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(billReports);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{2.0f, 3.5f, 3.5f, 3.0f, 2.0f, 2.0f, 1.5f});
            table.setSpacingBefore(10);

            writeTabHeader(table);
            writeTableData(table);

            document.add(table);
        } catch (Exception e){
            log.error("Error exporting PDF", e);
        }
    }
}
