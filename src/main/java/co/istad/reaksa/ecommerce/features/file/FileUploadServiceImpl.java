package co.istad.reaksa.ecommerce.features.file;

import co.istad.reaksa.ecommerce.features.file.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final FileUploadRepository fileUploadRepository;
    private final FileUploadMapper fileUploadMapper;


    @Value("${file.storage-location}")
    private String storageLocation;

//    @Value("${file.base-uri}")
//    private String baseUri;


    @Override
    public Page<FileUploadResponse> findAll(int pageNumber, int pageSize) {

        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);

        Page<FileUpload> fileUploadResponse = fileUploadRepository.findAll(pageRequest);
        return fileUploadResponse.map(fileUploadMapper::mapFileUploadtoFileUploadResponse);

    }

    @Override
    public FileUploadResponse findByName(String name) {
        return fileUploadRepository.findByName(name)
                .map(fileUploadMapper::mapFileUploadtoFileUploadResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File has not been found."));
    }



    @Override
    public void deleteByName(String name) {

        FileUpload fileUpload = fileUploadRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File has not been found"));
        fileUploadRepository.delete(fileUpload);

        // Create absolute path to store file
        Path path = Paths.get(storageLocation + fileUpload.getName() + "." + fileUpload.getExtension());
        try {
            boolean isExisted = Files.deleteIfExists(path);
            if (!isExisted)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File has not been found");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File has been failed to delete");
        }

//        Path path = Paths.get(storageLocation + name);
//
//        if (!Files.exists(path)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
//                    "File not found with name: " + name);
//        }
//        try {
//            Files.delete(path);
//        } catch (IOException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
//                    "File has failed to delete");
//        }
    }



    @Override
    public List<FileUploadResponse> uploadMultiple(List<MultipartFile> files) {
        //write logic to han
        return files.stream()
                .map(this::upload)
                .toList();
    }


    @Override
    public FileUploadResponse upload(MultipartFile file) {
        return saveFile(file);
    }

    private FileUploadResponse saveFile(MultipartFile file){
        //Prepare file information
        // File name
        String name = UUID.randomUUID().toString();

        //myprofile.png
        String ext =  file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        //name += "." + ext; //new-unique-filename.ext

        // create absolute path to store file
        Path path = Paths.get(storageLocation + name + "." + ext);


        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "File has been failed to upload");
        }

        //save information file into db
        FileUpload fileUpload = new FileUpload();
        fileUpload.setName(name);
        fileUpload.setExtension(ext);
        fileUpload.setCaption("ISTAD-Advanced IT Institute in Cambodia");
        fileUpload.setSize(file.getSize());
        fileUpload.setMediaType(file.getContentType());
        fileUploadRepository.save(fileUpload);

        return fileUploadMapper.mapFileUploadtoFileUploadResponse(fileUpload);
    }


}
