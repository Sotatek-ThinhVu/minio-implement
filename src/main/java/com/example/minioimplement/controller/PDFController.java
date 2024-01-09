package com.example.minioimplement.controller;

import com.example.minioimplement.dto.FileDto;
import com.example.minioimplement.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/pdf")
public class PDFController {

    private static Logger log = LoggerFactory.getLogger(PDFController.class);
    @Autowired
    private FileService fileService;
    @PostMapping("")
        public ResponseEntity<?> fileupload(@RequestParam("file") MultipartFile uploadfile) throws Exception {
        try {
            UUID uuid = fileService.save(uploadfile);
            return ResponseEntity.ok(uuid);
        } catch (Exception ex){
            log.error("Error when save file "+ex.getMessage());
        }
        return ResponseEntity.badRequest().body("error");
    }

    @GetMapping(value = "/download",
    produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadFile(@RequestParam UUID uuid, HttpServletResponse response) throws Exception {
        FileDto file = fileService.getPdfFile(uuid);
        byte[] data = file.getInputStream().readAllBytes();
        streamReport(response, data, file.getName());
    }

    protected void streamReport(HttpServletResponse response, byte[] data, String name)
            throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=" + name);
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }
}
