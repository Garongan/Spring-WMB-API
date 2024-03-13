package com.enigma.wmb_api_next.service;

import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PdfService {
    void export(HttpServletResponse response) throws DocumentException, IOException;
}
