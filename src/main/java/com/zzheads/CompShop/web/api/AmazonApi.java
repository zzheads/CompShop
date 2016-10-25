package com.zzheads.CompShop.web.api;

import com.google.gson.Gson;
import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Scope("request")
@Controller
public class AmazonApi {
    private final AddressService addressService;
    private final SupplierService supplierService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final AwsService awsService;

    private static final int COUNT_RETRY_IF_BUSY = 10;

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

    @RequestMapping(path = "/itemImage/{asin}", method = RequestMethod.GET)
    public @ResponseBody String itemImage (@PathVariable String asin) throws InterruptedException {
        Product product = getLookups(asin, "Images");
        return product.toJson();
    }

    @RequestMapping(path = "/itemsImages", method = RequestMethod.POST)
    public @ResponseBody String itemsImages (@RequestBody String asinsJson) throws InterruptedException {
        List<String> asins = fromJson(asinsJson);
        List<Product> products = new ArrayList<>();
        for (String asin : asins) {
            products.add(getLookups(asin, "Images"));
        }
        return Product.toJson(products);
    }

    @RequestMapping(path = "/itemPrice/{asin}", method = RequestMethod.GET)
    public @ResponseBody String itemPrice (@PathVariable String asin) throws InterruptedException {
        Product product = getLookups(asin, "OfferSummary");
        return product.toJson();
    }

    @RequestMapping(path = "/itemsPrices", method = RequestMethod.POST)
    public @ResponseBody String itemsPrices (@RequestBody String asinsJson) throws InterruptedException {
        List<String> asins = fromJson(asinsJson);
        List<Product> products = new ArrayList<>();
        for (String asin : asins) {
            products.add(getLookups(asin, "OfferSummary"));
        }
        return Product.toJson(products);
    }

    private List<String> fromJson (String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, List.class);
    }

    private Product getLookups (String asin, String method) throws InterruptedException {
        Product product = null;
        int count = 0;
        while (count<COUNT_RETRY_IF_BUSY) {
            product = awsService.itemLookup(asin, method);
            if (product != null) break;
            count++;
            TimeUnit.SECONDS.sleep(1);
        }
        return product;
    }
}
