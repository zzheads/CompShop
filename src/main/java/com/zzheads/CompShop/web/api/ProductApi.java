package com.zzheads.CompShop.web.api;

import com.zzheads.CompShop.model.*;
import com.zzheads.CompShop.service.CategoryService;
import com.zzheads.CompShop.service.ProductService;
import com.zzheads.CompShop.service.QwintryService;
import com.zzheads.CompShop.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zzheads.CompShop.web.api.QwintryApi.getDeliveryCost;

@Scope("request")
@Controller
public class ProductApi {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final SortingOrder sortingOrder;
    private final ShoppingCart shoppingCart;
    private final QwintryService qwintryService;

    @Autowired
    public ProductApi(ProductService productService, CategoryService categoryService, SupplierService supplierService, SortingOrder sortingOrder, ShoppingCart shoppingCart, QwintryService qwintryService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.sortingOrder = sortingOrder;
        this.shoppingCart = shoppingCart;
        this.qwintryService = qwintryService;
    }

    @RequestMapping(path = "/product", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String findAll () {
        List<Product> products = productService.findAll(sortingOrder.getOrder());
        return Product.toJson(products);
    }

    @RequestMapping(path = "/product", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String save (@RequestBody String json) {
        Product product = Product.fromJson(json);
        return saveProduct(product).toJson();
    }

    @RequestMapping(path = "/products", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String saveAll (@RequestBody String json) throws Exception {
        List<Product> products = Product.fromJsonList(json);
        for (Product product : products) {
            product.setDelivery(Double.parseDouble(getDeliveryCost(product, shoppingCart, qwintryService)));
            saveProduct(product);
        }
        return Product.toJson(products);
    }

    @RequestMapping(path = "/product/{id}", method = RequestMethod.DELETE, produces = {"application/json"})
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product != null) productService.delete(product);
    }

    @RequestMapping(path = "/product/{id}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus (HttpStatus.OK)
    public @ResponseBody String findById (@PathVariable Long id) {
        Product product = productService.findById(id);
        return product.toJson();
    }

    @RequestMapping(path = "/details/{id}", method = RequestMethod.GET)
    @ResponseStatus (HttpStatus.OK)
    public String detailsById (@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "detail";
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

    private Product saveProduct (Product product) {
        Category category = categoryService.findByName(product.getCategory().getName());
        if (category == null) {
            category = new Category(product.getCategory().getName(),"", null);
            categoryService.save(category);
        }
        Supplier supplier = supplierService.findByName(product.getSupplier().getName());
        if (supplier == null) {
            supplier = new Supplier(product.getSupplier().getName(), null, null);
            supplierService.save(supplier);
        }
        product.setCategory(category);
        product.setSupplier(supplier);

        productService.save(product);
        return product;
    }
}
