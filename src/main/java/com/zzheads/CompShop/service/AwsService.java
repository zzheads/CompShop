package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.Product;

import java.util.List;

public interface AwsService {
    List<Product> itemSearch (String keywords, String searchIndex);
    Product itemLookup (String asin, String responseGroup);
}
