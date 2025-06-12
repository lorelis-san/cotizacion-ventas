package com.lorelis.cotizacion.service.firebaseStorage;
import com.lorelis.cotizacion.model.productos.Products;

public interface ImageMigrationService {

    void migrateAllExternalImages();
    boolean migrateProductImageIfNeeded(Products product);
    void migrateBatchImages(int batchSize);

}
