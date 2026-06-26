package co.istad.reaksa.ecommerce.features.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

    Optional<FileUpload> findByName(String name);
}
