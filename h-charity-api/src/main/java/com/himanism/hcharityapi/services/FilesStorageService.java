package com.himanism.hcharityapi.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
 public void init();

  public void save(MultipartFile file, Long entityId);

  public Resource load(String filename);
  public Resource loadCoverPhoto(String filename, Long entityId);
  public Resource loadQrCodePhoto(String filename, Long entityId);
  
  public boolean delete(String filename);

  public void deleteAll();

  public Stream<Path> loadAll(Long entitId);
  public Stream<Path> loadAll();

  public void saveQRCode(Long entityId, MultipartFile file);
  public void saveCoverPhoto(Long entityId, MultipartFile file);
}
