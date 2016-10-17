package com.zzheads.CompShop.service;

import com.zzheads.CompShop.Application;
import com.zzheads.CompShop.model.Category;
import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.model.Supplier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SupplierService supplierService;

    private Product productTest;
    private Category categoryTest;
    private Supplier supplierTest;

    @Before
    public void setUp() throws Exception {
        categoryTest = categoryService.findAll().get(0);
        supplierTest = supplierService.findAll().get(0);
        productTest = new Product("Test product","", "", "", "", 10, 20, 5, supplierTest, categoryTest);

        productService.save(productTest);
    }

    @Test
    public void findAll() throws Exception {
        assertTrue(productService.findAll().contains(productTest));
    }

    @Test
    public void findById() throws Exception {
        Product retrieved = productService.findById(productTest.getId());
        assertEquals(productTest, retrieved);
    }

    @Test
    public void save() throws Exception {
        Product product = new Product("Test product","", "", "", "", 10, 20, 5, supplierTest, categoryTest);
        Long idBeforeSave = product.getId();
        productService.save(product);
        assertNotEquals(idBeforeSave, product.getId());
    }

    @Test
    public void delete() throws Exception {
        int before = productService.findAll().size();
        productService.delete(productTest);
        assertEquals(before-1, productService.findAll().size());
    }

    @Test
    public void findAllSortedName() throws Exception {
        productTest.setSupplier(null);
        productTest.setCategory(null);
        productService.save(productTest);
        productService.delete(productTest);
        List<Product> productList = new ArrayList<>();
        for (int i=0;i<100;i++) {
            String name = String.valueOf(i)+" product";
            Double price = 10000.0/(i+1.0);
            productList.add(new Product(name, "", "", "", "", price, price, 1, supplierTest, categoryTest));
            productService.save(productList.get(i));
        }

        List<Product> sortedByName = productService.findAll("name");
        List<Product> sortedByPrice = productService.findAll("price");
        assertNotEquals(sortedByName, sortedByPrice);
    }

}