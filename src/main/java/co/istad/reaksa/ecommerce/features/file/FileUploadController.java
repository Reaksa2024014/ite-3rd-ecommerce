package co.istad.reaksa.ecommerce.features.file;


import co.istad.reaksa.ecommerce.features.file.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;


    @GetMapping("/{name}")
    public FileUploadResponse findByName(@PathVariable String name) {
        return fileUploadService.findByName(name);
    }


    @GetMapping
    public Page<FileUploadResponse> findAll(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "25") int pageSize
    ) {
        return fileUploadService.findAll(pageNumber, pageSize);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping (consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileUploadResponse upload(@RequestPart MultipartFile file){

        return fileUploadService.upload(file);
    }

    @ResponseStatus(HttpStatus.CREATED) // when successfully created, it will show status 201 :
    @PostMapping("/multiple")
    public List<FileUploadResponse> uploadMultiple (@RequestPart List<MultipartFile> files){
        return fileUploadService.uploadMultiple(files);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteByName(@PathVariable String name){
        fileUploadService.deleteByName(name);
    }

}
