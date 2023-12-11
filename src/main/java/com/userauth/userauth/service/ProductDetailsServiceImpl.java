package com.userauth.userauth.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.userauth.userauth.entity.Category;
import com.userauth.userauth.entity.ProductData;
import com.userauth.userauth.entity.ProductDetails;
import com.userauth.userauth.entity.ProductRequest;
import com.userauth.userauth.entity.UserProducts;
import com.userauth.userauth.exception.EntityNotFoundException;
import com.userauth.userauth.repository.ProductDetailsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductDetailsServiceImpl implements ProductDetailsService {

    ProductDetailsRepository productDetailsRepository;
    private Firestore firestore;

    private CollectionReference getProductCollection() {
        return firestore.collection("productListByCategory");
    }

    private CollectionReference getCatgoryCollection() {
        return firestore.collection("productDetails");
    }

    // @Override
    // public ProductDetails getProductDetails(Long id) {
    // }

    private String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String storageBucket = "e-com-services-f8918.appspot.com";
        // Storage storage =
        // StorageOptions.newBuilder().setProjectId(storageBucket).build().getService();
        Storage storage = StorageClient.getInstance().bucket().getStorage();
        BlobId blobId = BlobId.of(storageBucket, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                .build();
        Blob blob = storage.create(blobInfo, file.getBytes());
        return blob.getMediaLink();
    }

    // @Override
    // public void saveProduct(ProductRequest product, ProductData productData) {
    // try {
    // MultipartFile file = product.getFile();
    // String fileUrl = uploadFile(file);
    // productData.setFileUrl(fileUrl);

    // ApiFuture<WriteResult> future =
    // firestore.collection("productList").document(productData.getCategory())
    // .set(productData);
    // firestore.collection("users").document(product.getUserId()).collection("products")
    // .document(productData.getCategory())
    // .set(productData);
    // } catch (IOException e) {
    // throw new EntityNotFoundException(null, ProductDetails.class);
    // }
    // }

    @Override
    public void saveProduct(ProductRequest product, ProductData productData) {
        MultipartFile[] files = product.getFiles();
        List<String> filesList = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            String fileUrl;
            try {
                fileUrl = uploadFile(file);
            } catch (IOException e) {
                throw new EntityNotFoundException(null, ProductDetails.class);
            }
            filesList.add(fileUrl);
        });
        productData.setFileUrls(filesList);

        DocumentReference categoryDocumentRef = firestore.collection("productListByCategory")
                .document(productData.getCategory());
        ApiFuture<DocumentSnapshot> categoryFuture = categoryDocumentRef.get();

        try {
            DocumentSnapshot document = categoryFuture.get();
            if (document.exists()) {
                categoryDocumentRef.update("products", FieldValue.arrayUnion(productData));
            } else {
                Map<String, List<ProductData>> mapProductsByCategory = new HashMap<>();
                List<ProductData> tempProductList = new ArrayList<>();
                tempProductList.add(productData);
                mapProductsByCategory.put("products", tempProductList);
                categoryDocumentRef.set(mapProductsByCategory);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new EntityNotFoundException(null, ProductDetails.class);
        }

        firestore.collection("productListById").document(productData.getUuid())
                .set(productData);

        DocumentReference userDocumentRef = firestore.collection("users").document(product.getUserId())
                .collection("products")
                .document(productData.getCategory());

        ApiFuture<DocumentSnapshot> userFuture = userDocumentRef.get();

        try {
            DocumentSnapshot document = userFuture.get();
            if (document.exists()) {
                userDocumentRef.update("products", FieldValue.arrayUnion(productData));
            } else {
                Map<String, List<ProductData>> mapProductsByCategory = new HashMap<>();
                List<ProductData> tempProductList = new ArrayList<>();
                tempProductList.add(productData);
                mapProductsByCategory.put("products", tempProductList);
                userDocumentRef.set(mapProductsByCategory);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new EntityNotFoundException(null, ProductDetails.class);
        }
    }

    @Override
    public void updateProdut(Long id, ProductDetails product) {
    }

    @Override
    public void deleteProduct(Long id) {
    }

    @Override
    public List<UserProducts> getAllProducts() {
        ApiFuture<QuerySnapshot> querySnapshot = getProductCollection().get();
        try {
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            List<UserProducts> allProductsList = new ArrayList<>();
            Gson gson = new Gson();
            for (QueryDocumentSnapshot document : documents) {
                UserProducts tempProduct = new UserProducts();
                tempProduct.setCategory(document.getId());
                List<ProductDetails> productDataList = gson.fromJson(gson.toJson(document.getData().get("products")),
                        new TypeToken<List<ProductDetails>>() {
                        }.getType());
                tempProduct.setProductsList(productDataList);
                allProductsList.add(tempProduct);
            }
            return allProductsList;
        } catch (Exception e) {
            throw new EntityNotFoundException(null, ProductDetails.class);
        }
    }

    @Override
    public List<Category> getCategories() {
        ApiFuture<QuerySnapshot> querySnapshot = getCatgoryCollection().get();
        try {
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            List<Category> categoryList = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                Category category = document.toObject(Category.class);
                category.setCategory(document.getId());
                categoryList.add(category);
            }
            return categoryList;
        } catch (Exception e) {
            throw new EntityNotFoundException(null, ProductDetails.class);
        }
    }

}
