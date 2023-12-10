package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.dto.ProductDto;
import com.thogwa.thogwa.backend.dto.productdto.ProductResponse;
import com.thogwa.thogwa.backend.exception.ProductNotExistsException;
import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.model.ProductColor;
import com.thogwa.thogwa.backend.repository.Categoryrepository;
import com.thogwa.thogwa.backend.repository.ProductColorRepository;
import com.thogwa.thogwa.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
     @Autowired
     StorageService storageService;
     @Autowired
     Categoryrepository categoryRepository;
     ProductColorRepository colorRepository;

    public void createProduct(String description, String name,
                              BigDecimal price, Category category,
                              MultipartFile file,Integer quantity) {
        Product product = new Product();
        product.setDescription(description);
        product.setImageURL(storageService.storeFile(file));
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        productRepository.save(product);
    }
    public Product getProductById(Integer productId) {
        // Assuming productRepository.findById returns an Optional<Product>
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
    }

    public ProductDto getProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setDescription(product.getDescription());
        productDto.setImageURL(product.getImageURL());
        productDto.setName(product.getName());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setPrice(product.getPrice());
        productDto.setId(product.getProductId());
        return productDto;
    }

    public List<ProductDto> getAllProducts( ) {
        List<Product> allProducts = productRepository.findAll();

        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product: allProducts) {
            productDtos.add(getProductDto(product));
        }
        return productDtos;
    }
    public Page<Product> findAllPageable(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return productRepository.findAll(pageable);
    }
//    public void updateProduct(Integer productID, ProductDto productDto, Category category) {
//        Product product = getProductFromDto(productDto, category);
//        product.setId(productID);
//        productRepository.save(product);
//    }


    public Product findById(Integer productId) throws ProductNotExistsException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotExistsException("product id is invalid: " + productId);
        }
        return optionalProduct.get();
    }

    public ProductResponse searchByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                                            String sortOrder) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check if sortBy is valid
        try {
            Product.class.getDeclaredField(sortBy);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Invalid sortBy parameter: " + sortBy);
        }

        // Change sortBy parameter to 'id'
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepository.findByCategory(category, pageDetails);

        List<Product> products = pageProducts.getContent();

        if (products.isEmpty()) {
            throw new RuntimeException(category.getCategoryName() + " category doesn't contain any products !!!");
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(products);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }
    public void addColorToProduct(Integer productId, ProductColor color) {
        Product product = getProductById(productId);
        if (product != null) {
            color.setProduct(product);
            colorRepository.save(color);
        }
    }

    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepository.findByNameLike(keyword, pageDetails);

        List<Product> products = pageProducts.getContent();

        if (products.size() == 0) {
            throw new RuntimeException("Products not found with keyword: " + keyword);
        }


        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(products);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }
    public List<Product> getRandomAmountOfProducts() {
        List<Product> productList = productRepository.findAllByCategoryId(1);
        if (productList.isEmpty()) {
            throw new RuntimeException("Couldn't find any product in DB");
        }
        Collections.shuffle(productList);
        int randomSeriesLength = 8;
        return productList.subList(0, randomSeriesLength);
    }

    public Product updateProduct(Integer productId, Product product1) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product"));
        if(product1.getPrice() != null &&
                !(product1.getPrice().
                        equals(product.getPrice()))) {
            product.setPrice(product1.getPrice());
        }
        if(product1.getOffPercentage() != null &&
                !(product1.getOffPercentage().
                        equals(product.getOffPercentage()))) {
            product.setOffPercentage(product1.getOffPercentage());
        }
        if(product1.getName() != null &&
                !(product1.getName().equals(product.getName()))) {
            product.setName(product1.getName());
        }
        if(product1.getDescription() != null &&
                !(product1.getDescription().equals(product.getDescription()))) {
            product.setDescription(product1.getDescription());
        }
        if(product1.getOffPercentage() != null &&
                !(product1.getOffPercentage().equals(product.getOffPercentage()))) {
            product.setOffPercentage(product1.getOffPercentage());
        }
        if(product1.getSpecialPrice() != null &&
                !(product1.getSpecialPrice().equals(product.getSpecialPrice()))) {
            product.setSpecialPrice(product1.getSpecialPrice());
        }
        if(product1.getQuantity() != null &&
                !(product1.getQuantity().equals(product.getQuantity()))) {
            product.setQuantity(product1.getQuantity());
        }
        return productRepository.save(product);
    }
}
