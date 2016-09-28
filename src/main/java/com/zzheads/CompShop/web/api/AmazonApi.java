package com.zzheads.CompShop.web.api;

import com.zzheads.CompShop.model.Address;
import com.zzheads.CompShop.model.Category;
import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.model.Supplier;
import com.zzheads.CompShop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AmazonApi {
    private final AddressService addressService;
    private final SupplierService supplierService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final AwsService awsService;

    @Autowired
    public AmazonApi(AddressService addressService, SupplierService supplierService, ProductService productService, AwsService awsService, CategoryService categoryService) {
        this.addressService = addressService;
        this.supplierService = supplierService;
        this.productService = productService;
        this.awsService = awsService;
        this.categoryService = categoryService;
    }

    @RequestMapping(path = "/itemSearch/{keywords}/{searchIndex}", method = RequestMethod.GET)
    public @ResponseBody String itemSearch (@PathVariable String keywords, @PathVariable String searchIndex) {
        List<Product> products = awsService.itemSearch(keywords, searchIndex);
        return Product.toJson(products);
    }

    @RequestMapping(path = "/itemLookup/{asin}", method = RequestMethod.GET)
    public @ResponseBody String itemLookup (@PathVariable String asin) {
        Product product = awsService.itemLookup(asin, "OfferSummary");
        product.setPhoto(awsService.itemLookup(asin, "Images").getPhoto());
        return product.toJson();
    }

}
