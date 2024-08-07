package com.himanism.hcharityapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.himanism.hcharityapi.dto.response.MessageResponseDto;
import com.himanism.hcharityapi.models.FileInfo;
import com.himanism.hcharityapi.services.FilesStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
public class FilesController {
    private final FilesStorageService storageService;

  @PostMapping("/upload/{entityId}")
  public ResponseEntity<MessageResponseDto> uploadPhoto(@PathVariable Long entityId, @RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      storageService.save(file, entityId);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponseDto(message));
    }
  }

  @GetMapping("/")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
          .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

      return new FileInfo(filename, url);
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("uploads/{entityId}/{filename:.+}")
  public ResponseEntity<Resource> entityPhotos(@PathVariable Long entityId, @PathVariable String filename) {
    Resource file = storageService.load(filename, entityId);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @GetMapping("uploads/cover/{entityId}/{filename:.+}")
  public ResponseEntity<Resource> coverPhoto(@PathVariable String filename, @PathVariable Long entityId) {
    Resource file = storageService.loadCoverPhoto(filename, entityId);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @GetMapping("uploads/qrcodes/{entityId}/{filename:.+}")
  public ResponseEntity<Resource> qrCodePhoto(@PathVariable String filename, @PathVariable Long entityId) {
    Resource file = storageService.loadQrCodePhoto(filename, entityId);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @GetMapping("uploads/static/{filename:.+}")
  public ResponseEntity<Resource> defaultImage(@PathVariable String filename) {
    Resource file = storageService.loadDefaultImage(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @DeleteMapping("/{filename:.+}")
  public ResponseEntity<MessageResponseDto> deleteFile(@PathVariable String filename) {
    String message = "";
    
    try {
      boolean existed = storageService.delete(filename);
      
      if (existed) {
        message = "Delete the file successfully: " + filename;
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
      }
      
      message = "The file does not exist!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponseDto(message));
    } catch (Exception e) {
      message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseDto(message));
    }
  }

  @PostMapping("/uploadQRCode/{entityId}")
  public ResponseEntity<MessageResponseDto> uploadFile(@PathVariable Long entityId, @RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      storageService.saveQRCode(entityId, file);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponseDto(message));
    }
  }

  @PostMapping("/onUploadCoverPhoto/{entityId}")
  public ResponseEntity<MessageResponseDto> onUploadCoverPhoto(@PathVariable Long entityId, @RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      storageService.saveCoverPhoto(entityId, file);
      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponseDto(message));
    }
  }
}
