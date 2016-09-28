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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
        categoryTest = new Category("Test category", "", null);
        supplierTest = new Supplier("Test supplier", null, null);
        productTest = new Product("Test product","", "", 10, 20, 5, supplierTest, categoryTest);

        categoryService.save(categoryTest);
        supplierService.save(supplierTest);
        productService.save(productTest);
    }

    @Test
    public void findAll() throws Exception {
        assertEquals(1, productService.findAll().size());
    }

    @Test
    public void findById() throws Exception {
        Product retrieved = productService.findById(productTest.getId());
        assertEquals(productTest, retrieved);
    }

    @Test
    public void save() throws Exception {
        Product product = new Product("Test product","", "", 10, 20, 5, supplierTest, categoryTest);
        Long idBeforeSave = product.getId();
        productService.save(product);
        assertNotEquals(idBeforeSave, product.getId());
    }

    @Test
    public void delete() throws Exception {
        productService.delete(productTest);
        assertEquals(0, productService.findAll().size());
    }

}