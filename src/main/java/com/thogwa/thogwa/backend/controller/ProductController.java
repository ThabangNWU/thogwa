package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.common.ApiResponse;
import com.thogwa.thogwa.backend.config.AppConstants;
import com.thogwa.thogwa.backend.dto.productdto.ProductResponse;
import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.Pager;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.model.ProductColor;
import com.thogwa.thogwa.backend.service.CategoryService;
import com.thogwa.thogwa.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 8;
    private static final int[] PAGE_SIZES = {8, 12, 16, 18, 20};

//    @GetMapping()
//    public String  getProducts(Model model) {
//     return findPaginated(1,model);
//    }

    @GetMapping()
    public ModelAndView displayProducts(){
        Optional<Integer> pageSize = Optional.of(8);
        Optional<Integer> page = Optional.of(1);
        return showProducts(pageSize,page);
    }
    @GetMapping("/parameter")
    public ModelAndView showProducts(@RequestParam("pageSize") Optional<Integer> pageSize,
                               @RequestParam("page") Optional<Integer> page){
        var modelAndView = new ModelAndView("shop");
        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // If a requested parameter is null or less than 1,
        // return the initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        var products = productService.findAllPageable(evalPage, evalPageSize);
        // Check if the requested page is out of bounds
        if (evalPage >= products.getTotalPages()) {
            // Redirect to the last available page
            return new ModelAndView("redirect:/product/?pageSize=" + evalPageSize + "&page=" + (products.getTotalPages() - 1));
        }
        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages",products.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }
    @GetMapping("/search")
    public ModelAndView seacrhByPage (@RequestParam("pageSize") Optional<Integer> pageSize,
                                      @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("keyword") Optional<String> keyword){
        var modelAndView = new ModelAndView("shop");
        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // If a requested parameter is null or less than 1,
        // return the initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = page.filter(p -> p >= 1)
                .map(p -> p - 1)
                .orElse(INITIAL_PAGE);

        var products = productService.findAllPageable(evalPage, evalPageSize);
        // Check if the requested page is out of bounds
        if (evalPage >= products.getTotalPages()) {
            // Redirect to the last available page
            return new ModelAndView("redirect:/product/?pageSize=" + evalPageSize + "&page=" + (products.getTotalPages() - 1));
        }
        var pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);



        modelAndView.addObject("products", products);
        modelAndView.addObject("totalItems", products.getTotalElements());
        modelAndView.addObject("totalPages",products.getTotalPages());
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    @PostMapping("/add/{categoryId}")
    public ResponseEntity<ApiResponse> addProduct(@RequestParam("description") String description,
                                                  @RequestParam("name") String name,
                                                  @RequestParam("price") BigDecimal price,
                                                  @PathVariable Integer categoryId,
                                                  @RequestParam("quantity") Integer quantity,
                                                  @RequestParam("file") MultipartFile file) {
        Optional<Category> optionalCategory = categoryService.readCategory(categoryId);
        if (!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(
                    false, "category is invalid"), HttpStatus.CONFLICT);
        }
        Category categories = optionalCategory.get();
        productService.createProduct(description,name,price, categories,file,quantity);
        return new ResponseEntity<>(new ApiResponse(
                true, "Product has been added"), HttpStatus.CREATED);
    }

    @GetMapping("/random/category")
    @ResponseBody
    public List<Product> getRandomAmountOfProducts() {
        return productService.getRandomAmountOfProducts();
    }
    @PutMapping("admin/product/update/{productId}")
    @ResponseBody
    public Product updateProduct (@PathVariable Integer productId,
                                  @RequestBody Product product) {
        return productService.updateProduct(productId, product);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }


    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Integer categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }









    @GetMapping("/shop-details/{productId}")
    public String getProductDetails(@PathVariable Integer productId, Model model) {

        Product product = productService.getProductById(productId);

        model.addAttribute("product", product);

        return "shop-details";
    }

//    @PostMapping("/update/{productID}")
//    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productID") Integer productID, @RequestBody @Valid ProductDto productDto) {
//        Optional<Category> optionalCategory = categoryService.readCategory(productDto.getCategoryId());
//        if (!optionalCategory.isPresent()) {
//            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
//        }
//        Category category = optionalCategory.get();
//        productService.updateProduct(productID, productDto, category);
//        return new ResponseEntity<>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
//    }

    @GetMapping("/addColor/{productId}")
    public String addColorForm(@PathVariable Integer productId, Model model) {
        // Ensure 'productId' is added to the model
        model.addAttribute("productId", productId);
        // Create a new ProductColor instance
        model.addAttribute("color", new ProductColor());
        return "addColor";
    }


    @PostMapping("/addColor/{productId}")
    public String addColor(@PathVariable Integer productId, @ModelAttribute ProductColor color) {
        // Assuming productService.addColorToProduct is correctly implemented
        productService.addColorToProduct(productId, color);
        // Redirect to the product listing page
        return "redirect:/product/list";
    }
    @GetMapping("/list")
    public String productList(Model model) {
        // Fetch the product list from your service and add it to the model
        // For demonstration purposes, let's assume you have a ProductService
        // that provides a method getProductList() to get the list of products.
        model.addAttribute("products", productService.getAllProducts());

        return "productList"; // Thymeleaf template name
    }
}
