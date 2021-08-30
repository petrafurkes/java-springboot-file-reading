package com.data.data.controller;

import com.data.data.model.FileContent;
import com.data.data.model.MessageResponse;
import com.data.data.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {

    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/import")
    public ResponseEntity<?> fileImport(@RequestParam("file") MultipartFile file) {

        try {
            List<FileContent> fileContentList = new ArrayList<>();
            fileService.importFile(file, fileContentList);

            return ResponseEntity.status(HttpStatus.OK).body(fileContentList);
        } catch (Exception e) {
            String message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }

}
