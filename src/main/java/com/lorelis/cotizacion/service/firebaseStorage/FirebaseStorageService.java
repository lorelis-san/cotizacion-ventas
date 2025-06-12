package com.lorelis.cotizacion.service.firebaseStorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FirebaseStorageService {

    String uploadFile(MultipartFile file) throws IOException;
    void deleteFile(String imageUrl);
    String uploadFileFromUrl(String imageUrl) throws IOException;
}
