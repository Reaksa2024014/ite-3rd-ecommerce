package co.istad.reaksa.ecommerce.features.file;

import co.istad.reaksa.ecommerce.features.file.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService{

    @Value("${file.storage-location}")
    private String storageLocation;

    @Value("${file.base-uri}")
    private String baseUri;


    @Override
    public void deleteByName(String name) {
        Path path = Paths.get(storageLocation + name);

        if (!Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "File not found with name: " + name);
        }

        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "File has failed to delete");
        }
    }

    @Override
    public List<FileUploadResponse> uploadMultiple(List<MultipartFile> files) {
        return files.stream()
                .map(this::upload)
                .toList();
    }

    @Override
    public FileUploadResponse upload(MultipartFile file) {

        //Prepare file information
        // File name
         String name = UUID.randomUUID().toString();

         //myprofile.png
         String ext =  file.getOriginalFilename()
                 .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

         name += "." + ext; //new-unique-filename.ext

        // create absolute path to store file
        Path path = Paths.get(storageLocation + name);


        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "File has been failed to upload");
        }

        return FileUploadResponse.builder()
                .name(name)
                .size(file.getSize())
                .mediaType(file.getContentType())
                .uri(baseUri + name)
                .build();

    }
}
