package co.istad.reaksa.ecommerce.features.file;

import co.istad.reaksa.ecommerce.features.file.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    FileUploadResponse upload(MultipartFile file);

    List<FileUploadResponse> uploadMultiple(List<MultipartFile> files);

    void deleteByName(String name);
 }
