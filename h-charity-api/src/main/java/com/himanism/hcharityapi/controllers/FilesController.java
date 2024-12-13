package com.himanism.hcharityapi.controllers;

import java.util.ArrayList;
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
  public ResponseEntity<MessageResponseDto> uploadPhoto(@PathVariable Long entityId,
      @RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      log.info("Files Controller: Starting the file upload process for entityId, file", entityId,
          file.getOriginalFilename());

      storageService.save(file, entityId);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      log.info("File uploaded successfully", file.getOriginalFilename());

      return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      log.error("Error occurred while uploading file", file.getOriginalFilename(), entityId, e);

      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponseDto(message));
    }
  }

  @GetMapping("/")
  public ResponseEntity<List<FileInfo>> getListFiles() {
    List<FileInfo> fileInfos = new ArrayList<>();
    try {
      log.info("Files Controller: Fetching list of files");

      fileInfos = storageService.loadAll().map(path -> {
        String filename = path.getFileName().toString();
        String url = MvcUriComponentsBuilder
            .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

        return new FileInfo(filename, url);
      }).collect(Collectors.toList());

      log.info("Successfully fetched files", fileInfos.size());
      return ResponseEntity.status(HttpStatus.OK).body(fileInfos);

    } catch (Exception e) {
      log.error("Error occurred while fetching file list", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("uploads/{entityId}/{filename:.+}")
  public ResponseEntity<Resource> entityPhotos(@PathVariable Long entityId, @PathVariable String filename) {
    try {
      log.info("Files Controller: load with filename", entityId, filename);

      Resource file = storageService.load(filename, entityId);

      if (file.exists()) {
        log.info("File loaded successfully", filename);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
      } else {
        log.error("File not found", filename);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

    } catch (Exception e) {
      log.error("Error occurred while loading file", entityId, filename, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("uploads/cover/{entityId}/{filename:.+}")
  public ResponseEntity<Resource> coverPhoto(@PathVariable String filename, @PathVariable Long entityId) {
    try {
      log.info("Files Controller: load cover photo with filename", entityId, filename);

      Resource file = storageService.loadCoverPhoto(filename, entityId);

      if (file.exists()) {
        log.info("Cover photo loaded successfully", filename);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
      } else {
        log.error("Cover photo not found", filename);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

    } catch (Exception e) {
      log.error("Error occurred while loading cover photo for entityId with filename", entityId, filename, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("uploads/qrcodes/{entityId}/{filename:.+}")
  public ResponseEntity<Resource> qrCodePhoto(@PathVariable String filename, @PathVariable Long entityId) {
    try {
      log.info("Files Controller: load QR code photo with filename: {}", entityId, filename);

      Resource file = storageService.loadQrCodePhoto(filename, entityId);

      if (file.exists()) {
        log.info("QR code photo loaded successfully", filename);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
      } else {
        log.error("QR code photo not found", filename);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

    } catch (Exception e) {
      log.error("Error occurred while loading QR code photo", entityId, filename, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("uploads/static/{filename:.+}")
  public ResponseEntity<Resource> defaultImage(@PathVariable String filename) {
    try {
      log.info("Files Controller: load default image with filename", filename);

      Resource file = storageService.loadDefaultImage(filename);

      if (file.exists()) {
        log.info("Default image loaded successfully", filename);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
      } else {
        log.error("Default image not found", filename);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

    } catch (Exception e) {
      log.error("Error occurred while loading default image with filename", filename, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/{filename:.+}")
  public ResponseEntity<MessageResponseDto> deleteFile(@PathVariable String filename) {
    String message = "";

    try {
      log.info("Files Controller: delete file", filename);

      boolean existed = storageService.delete(filename);

      if (existed) {
        message = "Deleted the file successfully " + filename;
        log.info("File deleted successfully", filename);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
      }

      message = "The file does not exist!";
      log.error("File not found", filename);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponseDto(message));
    } catch (Exception e) {
      message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
      log.error("Error occurred while deleting the file", filename, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseDto(message));
    }
  }

  @PostMapping("/uploadQRCode/{entityId}")
  public ResponseEntity<MessageResponseDto> uploadFile(@PathVariable Long entityId,
      @RequestParam("file") MultipartFile file) {
    String message = "";

    try {
      log.info("Files Controller: upload the file", file.getOriginalFilename(), entityId);

      storageService.saveQRCode(entityId, file);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      log.info("File uploaded successfully}", file.getOriginalFilename());

      return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      log.error("Error occurred while uploading the file", file.getOriginalFilename(), e);

      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponseDto(message));
    }
  }

  @PostMapping("/onUploadCoverPhoto/{entityId}")
  public ResponseEntity<MessageResponseDto> onUploadCoverPhoto(@PathVariable Long entityId,
      @RequestParam("file") MultipartFile file) {
    String message = "";

    try {
      log.info("Files Controller:upload cover photo", file.getOriginalFilename(), entityId);

      storageService.saveCoverPhoto(entityId, file);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      log.info("Cover photo uploaded successfully", file.getOriginalFilename());

      return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      log.error("Error occurred while uploading cover photo", file.getOriginalFilename(), e);

      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponseDto(message));
    }
  }

}
