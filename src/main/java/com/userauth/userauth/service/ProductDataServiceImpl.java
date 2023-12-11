package com.userauth.userauth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.api.gax.paging.Page;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import com.userauth.userauth.entity.ProductData;
import com.userauth.userauth.exception.EntityNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductDataServiceImpl implements ProductDataService {

    private Firestore firestore;

    private CollectionReference getProductCollection() {
        return firestore.collection("productListById");
    }

    @Override
    public ProductData getProductData(String uuid) {
        DocumentReference docRef = getProductCollection().document(uuid);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                ProductData tempProduct = document.toObject(ProductData.class);
                return tempProduct;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new EntityNotFoundException(null, ProductData.class);
        }
        return null;
    }

    @Override
    public List<String> getCarouselImages() {
        String prefix = "carosels" + "/";
        String storageBucket = "e-com-services-f8918.appspot.com";
        Storage storage = StorageClient.getInstance().bucket().getStorage();
        Page<Blob> blobs = storage.list(storageBucket, Storage.BlobListOption.prefix(prefix));
        List<String> downloadUrls = new ArrayList<>();
        for (Blob blob : blobs.iterateAll()) {
            if (!blob.isDirectory()) {
                String downloadUrl = blob.getMediaLink();
                downloadUrls.add(downloadUrl);
            }
        }
        return downloadUrls;
    }

}
