package com.fpl.datn.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.request.OrderIdRequest;
import com.fpl.datn.dto.request.OrderRequest;
import com.fpl.datn.service.ExportPDFService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PreviewController {
    ExportPDFService exportPDFService;

    @GetMapping("/{id}")
    public ResponseEntity<String> previewInvoice(@PathVariable int id) {
        String html = exportPDFService.renderOrderHtmlById(id);
        return ResponseEntity.ok().body(html);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<byte[]> exportpdfOrder(@PathVariable int id) throws Exception {
        byte[] pdf = exportPDFService.exportPdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/multiple")
    public ResponseEntity<byte[]> exportMultiplePdf(@RequestBody OrderIdRequest request) throws Exception {
        byte[] pdf = exportPDFService.exportMultiplePdf(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/order")
    public ResponseEntity<byte[]> exportRequestOrder(@RequestBody OrderRequest request) throws Exception {
        byte[] pdf = exportPDFService.exportRequestPdf(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
