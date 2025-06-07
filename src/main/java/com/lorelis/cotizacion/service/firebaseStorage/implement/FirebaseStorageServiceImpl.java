package com.lorelis.cotizacion.service.firebaseStorage.implement;

import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.lorelis.cotizacion.service.firebaseStorage.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    private static final String FOLDER_NAME = "products";

    @Value("${firebase.bucket-name}")
    private String bucketName;

    @Autowired
    private Storage storage;
    @Autowired
    private Bucket firebaseBucket;

    @PostConstruct
    public void verifyBucketName() {
        try {
            String actualBucketName = FirebaseApp.getInstance().getOptions().getStorageBucket();
            System.out.println("Nombre del bucket configurado en Firebase: " + actualBucketName);
            System.out.println("Nombre del bucket configurado en la aplicación: " + bucketName);

            if (!actualBucketName.equals(bucketName)) {
                System.out.println("ADVERTENCIA: El nombre del bucket en la configuración no coincide con el de Firebase");
            }
        } catch (Exception e) {
            System.err.println("Error al verificar nombre del bucket: " + e.getMessage());
        }
    }

    @PostConstruct
    public void testFirebaseConnection() {
        try {
            System.out.println("Probando conexión a Firebase Storage...");
            System.out.println("Nombre del bucket: " + bucketName);

            // Listar buckets disponibles
            System.out.println("Buckets disponibles:");
            storage.list().iterateAll().forEach(bucket ->
                    System.out.println(" - " + bucket.getName()));

            // Intentar listar objetos en el bucket
            System.out.println("Objetos en el bucket " + bucketName + ":");
            storage.list(bucketName).iterateAll().forEach(blob ->
                    System.out.println(" - " + blob.getName()));

            System.out.println("Conexión a Firebase Storage exitosa");
        } catch (Exception e) {
            System.err.println("Error al conectar con Firebase Storage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("No se puede subir un archivo vacío");
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        String path = FOLDER_NAME + "/" + fileName;

        try {
            // Generar un token único para la descarga
            String downloadToken = UUID.randomUUID().toString();

            // Crear metadata con el token de descarga
            Map<String, String> metadata = new HashMap<>();
            metadata.put("firebaseStorageDownloadTokens", downloadToken);

            BlobId blobId = BlobId.of(bucketName, path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .setMetadata(metadata)
                    .build();

            // Subir el archivo
            Blob blob = storage.create(blobInfo, file.getBytes());

            System.out.println("Archivo subido con éxito: " + path);
            System.out.println("Token generado: " + downloadToken);

            // Generar URL con el token
            String downloadUrl = String.format(
                    "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s",
                    bucketName,
                    URLEncoder.encode(path, StandardCharsets.UTF_8),
                    downloadToken
            );

            System.out.println("URL de descarga generada: " + downloadUrl);
            return downloadUrl;

        } catch (Exception e) {
            System.err.println("Error al subir el archivo: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Error al subir archivo a Firebase: " + e.getMessage());
        }
    }
//    @Override
//    public String uploadFile(MultipartFile file) throws IOException {
//        if (file == null || file.isEmpty()) {
//            throw new IOException("No se puede subir un archivo vacío");
//        }
//
//        String originalFileName = file.getOriginalFilename();
//        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//        String fileName = UUID.randomUUID().toString() + fileExtension;
//
//        String path = FOLDER_NAME + "/" + fileName;
//
//        try {
//            // Crear configuración de BlobInfo para que sea públicamente accesible
//            BlobId blobId = BlobId.of(bucketName, path);
//            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
//                    .setContentType(file.getContentType())
//                    // Aquí está la clave: hacer que el archivo sea públicamente accesible
//                    .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
//                    .build();
//
//            // Subir el archivo con la configuración de acceso público
//            Blob blob = storage.create(blobInfo, file.getBytes());
//
//            System.out.println("Archivo subido con éxito: " + path);
//            System.out.println("URL pública generada: " + getDownloadUrl(path));
//
//            // Verificar si el archivo es accesible públicamente
//            System.out.println("¿El archivo es público? " + blob.getAcl().stream()
//                    .anyMatch(acl -> acl.getEntity().equals(Acl.User.ofAllUsers()) &&
//                            acl.getRole().equals(Acl.Role.READER)));
//
//            return getDownloadUrl(path);
//        } catch (Exception e) {
//            System.err.println("Error al subir el archivo: " + e.getMessage());
//            e.printStackTrace();
//            throw new IOException("Error al subir archivo a Firebase: " + e.getMessage());
//        }
//    }


    private String getDownloadUrl(String path) {
        // Generar URL pública para descargar el archivo
        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucketName,
                URLEncoder.encode(path, StandardCharsets.UTF_8)
        );
    }

    @Override
    public void deleteFile(String imageUrl) {
        try {
            String path = extractPathFromUrl(imageUrl);
            if (path == null) {
                System.err.println("No se pudo extraer el path del archivo para eliminar.");
                return;
            }

            BlobId blobId = BlobId.of(bucketName, path);
            boolean deleted = storage.delete(blobId);

            if (deleted) {
                System.out.println("Archivo eliminado correctamente: " + path);
            } else {
                System.err.println("No se pudo eliminar el archivo (puede que no exista): " + path);
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar archivo de Firebase: " + e.getMessage());
        }
    }

    private String extractPathFromUrl(String imageUrl) {
        try {
            // Busca el valor después de 'o=' y antes de '&' (el path está codificado en la URL)
            String[] parts = imageUrl.split("/o/");
            if (parts.length > 1) {
                String pathAndToken = parts[1];
                String pathEncoded = pathAndToken.split("\\?")[0];
                return URLDecoder.decode(pathEncoded, StandardCharsets.UTF_8.name());
            }
        } catch (Exception e) {
            System.err.println("No se pudo extraer el path de la URL: " + e.getMessage());
        }
        return null;
    }
}
