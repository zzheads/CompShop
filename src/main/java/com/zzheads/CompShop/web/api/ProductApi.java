package com.zzheads.CompShop.web.api;

import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductApi {
    private final ProductService productService;

    @Autowired
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(path = "/product", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String findAll () {
        List<Product> products = productService.findAll();
        return Product.toJson(products);
    }

    @RequestMapping(path = "/product", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String save (@RequestBody String json) {
        Product product = Product.fromJson(json);
        productService.save(product);
        return product.toJson();
    }

    @RequestMapping(path = "/product/{id}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String findById (@PathVariable Long id) {
        Product product = productService.findById(id);
        return product.toJson();
    }

    @RequestMapping(path = "/product_by_category/{id}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String findByCategory (@PathVariable Long id) {
        List<Product> products = productService.findByCategory(id);
        return Product.toJson(products);
    }

    @RequestMapping(path = "/product_by_search/{pattern}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String findBySearch (@PathVariable String pattern) {
        List<Product> products = productService.findBySearch(pattern);
        return Product.toJson(products);
    }

}
