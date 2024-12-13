package com.himanism.hcharityapi.services.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.himanism.hcharityapi.controllers.FilesController;
import com.himanism.hcharityapi.entities.EntityPhotos;
import com.himanism.hcharityapi.repo.EntityPhotosRepository;
import com.himanism.hcharityapi.repo.EntityRepository;
import com.himanism.hcharityapi.services.FilesStorageService;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Service
@Transactional
public class FilesStorageServiceImpl implements FilesStorageService {

  @Autowired
  EntityPhotosRepository entityPhotosRepository;

  @Autowired
  EntityRepository entityRepository;

  private final Path root = Paths.get("uploads");

  @Override
  public void init() {
    try {
      log.info("Files Storage Service: Initializing folder for upload at", root.toString());

      Files.createDirectories(root);

      log.info("Folder for upload initialized successfully at", root.toString());
    } catch (IOException e) {
      log.error("Error initializing folder for upload at", root.toString(), e);
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public void save(MultipartFile file, Long entityId) {
    try {
      Path imagePath = Paths.get("uploads/" + entityId);

      log.info("Files Storage Service: Checking if directory exists for entityId", entityId, imagePath.toString());

      if (!Files.exists(imagePath)) {
        Files.createDirectories(imagePath);
        log.info("Created directory for entityId", entityId, imagePath.toString());
      } else {
        Files.copy(file.getInputStream(), imagePath.resolve(file.getOriginalFilename()),
            StandardCopyOption.REPLACE_EXISTING);
        log.info("File uploaded successfully", file.getOriginalFilename(), imagePath.toString());

        this.updatePhotosTableWithUrl(file, entityId);
        log.info("Photos table updated with URL for file", file.getOriginalFilename());
      }
    } catch (Exception e) {
      if (e instanceof FileAlreadyExistsException) {
        log.error("A file with the name, already exists", file.getOriginalFilename(), entityId);
        throw new RuntimeException("A file of that name already exists.");
      }
      log.error("Error occurred while saving the file", file.getOriginalFilename(), entityId, e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public Resource load(String filename, Long entityId) {
    try {
      Path imagePath = Paths.get("uploads/" + entityId.toString());
      Path file = imagePath.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      log.info("Files Storage Service: Checking if file  exists", filename, entityId, file.toString());

      if (resource.exists() || resource.isReadable()) {
        log.info("File found and is readable", filename, entityId, file.toString());
        return resource;
      } else {
        log.error("File  not found or not readable", filename, entityId, file.toString());
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      log.error("Malformed URL exception occurred while loading file", filename, entityId, e.getMessage());
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public Resource loadCoverPhoto(String filename, Long entityId) {
    try {
      Path imagePath = Paths.get("uploads/cover/" + entityId.toString());
      Path file = imagePath.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      log.info("Files Storage Service: Checking if cover photo file exists", filename, entityId, file.toString());

      if (resource.exists() || resource.isReadable()) {
        log.info("Cover photo file found and is readable", filename, entityId, file.toString());
        return resource;
      } else {
        log.error("Cover photo file  not found or not readable ", filename, entityId, file.toString());
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      log.error("Malformed URL exception occurred while loading cover photo file", filename, entityId, e.getMessage());
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public Resource loadQrCodePhoto(String filename, Long entityId) {
    try {
      Path imagePath = Paths.get("uploads/qrcodes/" + entityId.toString());
      Path file = imagePath.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      log.info("Files Storage Service: Checking if QR code photo file  exists", filename, entityId, file.toString());

      if (resource.exists() || resource.isReadable()) {
        log.info("QR code photo file found and is readable", filename, entityId, file.toString());
        return resource;
      } else {
        log.error("QR code photo file not found or not readable", filename, entityId, file.toString());
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      log.error("Malformed URL exception occurred while loading QR code photo file ", filename, entityId,
          e.getMessage());
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public Resource loadDefaultImage(String filename) {
    try {
      Path imagePath = Paths.get("uploads/static");
      Path file = imagePath.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      log.info("Files Storage Service: Checking if default image file  exists", filename, file.toString());

      if (resource.exists() || resource.isReadable()) {
        log.info("Default image file  found and is readable", filename, file.toString());
        return resource;
      } else {
        log.error("Default image file not found or not readable", filename, file.toString());
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      log.error("Malformed URL exception occurred while loading default image file", filename, e.getMessage());
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public boolean delete(String filename) {
    try {
      Path file = root.resolve(filename);

      log.info("Files Storage Service: delete file", filename, file.toString());

      boolean isDeleted = Files.deleteIfExists(file);

      if (isDeleted) {
        log.info("File successfully deleted", filename, file.toString());
      } else {
        log.info("File not found", filename, file.toString());
      }

      return isDeleted;
    } catch (IOException e) {
      log.error("Error occurred while deleting file", filename, e.getMessage());
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    try {
      log.info("Files Storage Service: delete all files in directory", root.toString());

      FileSystemUtils.deleteRecursively(root.toFile());

      log.info("Successfully deleted all files in directory", root.toString());
    } catch (Exception e) {
      log.error("Error occurred while deleting all files in directory", root.toString(), e.getMessage());
      throw new RuntimeException("Error occurred while deleting all files: " + e.getMessage());
    }
  }

  @Override
  public Stream<Path> loadAll(Long entityId) {
    try {
      Path photosPath = Paths.get("uploads/" + entityId);

      log.info("Files Storage Service: load all files from directory", photosPath.toString());

      Stream<Path> fileStream = Files.walk(photosPath, 1)
          .filter(path -> !path.equals(photosPath))
          .map(photosPath::relativize);

      log.info("Successfully loaded files from directory", photosPath.toString());

      return fileStream;
    } catch (IOException e) {
      log.error("Error occurred while loading files from directory", "uploads/" + entityId, e.getMessage());
      throw new RuntimeException("Could not load the files!");
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      Path photosPath = Paths.get("uploads/");

      log.info("Files Storage Service: load all files from the directory", photosPath.toString());

      Stream<Path> fileStream = Files.walk(photosPath, 1)
          .filter(path -> !path.equals(photosPath))
          .map(photosPath::relativize);

      log.info("Successfully loaded files from the directory", photosPath.toString());

      return fileStream;
    } catch (IOException e) {
      log.error("Error occurred while loading files from the directory", "uploads/", e.getMessage());
      throw new RuntimeException("Could not load the files!");
    }
  }

  @Override
  public void saveQRCode(Long entityId, MultipartFile file) {
    try {
      Path qrcodePath = Paths.get("uploads/qrcodes/" + entityId);

      log.info("Files Storage Service: save QR code", entityId);

      if (!Files.exists(qrcodePath)) {
        Files.createDirectories(qrcodePath);
        log.info("Created directory ", entityId);
      } else {
        log.info("Cleaning existing directory", entityId);
        this.cleanDirectory("uploads/qrcodes/" + entityId, entityId);
        entityPhotosRepository.deleteByEntityIdAndIsQRCode(entityId, true);
        log.info("Deleted existing QR code", entityId);
      }

      Files.copy(file.getInputStream(), qrcodePath.resolve(file.getOriginalFilename()));
      log.info("Successfully uploaded QR code", file.getOriginalFilename(), entityId);

      this.loadAllByPath(qrcodePath).forEach(path -> {
        String filename = path.getFileName().toString();
        String url = MvcUriComponentsBuilder
            .fromMethodName(FilesController.class, "qrCodePhoto", filename, entityId).build().toString();

        if (path.getFileName().toString().equalsIgnoreCase(file.getOriginalFilename())) {
          saveEntityPhotos(url, entityId, true, false);
          log.info("Saved entity photo with URL", url, entityId);
        }
      });
    } catch (Exception e) {
      if (e instanceof FileAlreadyExistsException) {
        log.error("A file with the name  already exists ", file.getOriginalFilename(), entityId);
        throw new RuntimeException("A file of that name already exists.");
      }

      log.error("Error occurred while saving QR code", entityId, e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public void saveCoverPhoto(Long entityId, MultipartFile file) {
    try {
      Path coverPath = Paths.get("uploads/cover/" + entityId);

      log.info("Files Storage Service: save cover photo", entityId);

      if (!Files.exists(coverPath)) {
        Files.createDirectories(coverPath);
        log.info("Created directory for cover photo", entityId);
      } else {
        log.info("Cleaning existing directory for cover photo", entityId);
        this.cleanDirectory("uploads/cover/" + entityId, entityId);
        entityPhotosRepository.deleteByEntityIdAndIsCoverPhoto(entityId, true);
        log.info("Deleted existing cover photo", entityId);
      }

      this.compressAndChangeResolutionForCoverPhotoAndQrCode(file, coverPath);
      log.info("Successfully compressed and changed resolution for cover photo", entityId);

      this.loadAllByPath(coverPath).forEach(path -> {
        String filename = path.getFileName().toString();
        String url = MvcUriComponentsBuilder
            .fromMethodName(FilesController.class, "coverPhoto", filename, entityId).build().toString();

        if (path.getFileName().toString().equalsIgnoreCase(file.getOriginalFilename())) {
          saveEntityPhotos(url, entityId, false, true);
          log.info("Saved entity photo with URL", url, entityId);
        }
      });
    } catch (Exception e) {
      if (e instanceof FileAlreadyExistsException) {
        log.error("A file with the name already exists", file.getOriginalFilename(), entityId);
        throw new RuntimeException("A file of that name already exists.");
      }

      log.error("Error occurred while saving cover photo", entityId, e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  private void cleanDirectory(String path, Long entityId) {
    File directory = new File(path);
    log.info("Files Storage Service: Cleaning directory", path, entityId);

    for (File fileObj : Objects.requireNonNull(directory.listFiles())) {
      if (!fileObj.isDirectory()) {
        boolean deleted = fileObj.delete();
        if (deleted) {
          log.info("Deleted file", fileObj.getName(), entityId);
        } else {
          log.error("Failed to delete file", fileObj.getName(), entityId);
        }
      }
    }
  }

  private void updatePhotosTableWithUrl(MultipartFile file, Long entityId) {
    this.loadAll(entityId).forEach(path -> {
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
          .fromMethodName(FilesController.class, "entityPhotos", entityId, filename).build().toString();

      if (path.getFileName().toString().equalsIgnoreCase(file.getOriginalFilename())) {
        saveEntityPhotos(url, entityId, false, false);
        log.info("Files Storage Service: Updated photo URL ", entityId, file.getOriginalFilename());
      }
    });
  }

  private void saveEntityPhotos(String url, Long entityId, Boolean isQRCode, Boolean isCoverPhoto) {
    EntityPhotos entityPhotos = new EntityPhotos();
    entityPhotos.setIsQRCode(isQRCode);
    entityPhotos.setPhotoUrl(url);
    entityPhotos.setEntityId(entityId);
    entityPhotos.setIsCoverPhoto(isCoverPhoto);
    entityPhotosRepository.save(entityPhotos);

    log.info("Files Storage Service: Saved entity photo with URL", url, entityId);
  }

  private Stream<Path> loadAllByPath(Path photosPath) {
    try {
      log.info("Files Storage Service: Loading all files", photosPath);
      return Files.walk(photosPath, 1)
          .filter(path -> !path.equals(photosPath))
          .map(photosPath::relativize);
    } catch (IOException e) {
      log.error("Error occurred while loading files", photosPath, e);
      throw new RuntimeException("Could not load the files!");
    }
  }

  private void compressAndChangeResolutionForCoverPhotoAndQrCode(MultipartFile file, Path outputPath)
      throws IOException {
    try {
      log.info("Files Storage Service: Compressing and changing resolution for file", file.getOriginalFilename(),
          outputPath);

      // Use Thumbnailator to compress and change resolution
      Thumbnails.of(file.getInputStream())
          .size(300, 200) // Change resolution to 300x200
          .outputFormat("jpg") // Change format to JPEG
          .toFile(outputPath.resolve(file.getOriginalFilename()).toFile());

      log.info("Successfully compressed and changed resolution for file", file.getOriginalFilename());
    } catch (Exception ex) {
      log.error("Error occurred while compressing and changing resolution for file", file.getOriginalFilename(), ex);
      throw ex;
    }
  }

}
