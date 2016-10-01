package com.zzheads.CompShop.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

public class ProductTest {
    private Product product;

    @Before
    public void setup () {
        product = new Product();
        product.setWeight(100);
        product.setUnitsW("hundredths-pounds");
        product.setHeight(100);
        product.setLength(100);
        product.setWidth(100);
        product.setUnitsL("hundredths-inches");
    }

    @Test
    public void get_weight() throws Exception {
        String weight = product.get_weight();
        assertEquals("0.453592", weight);
    }

    @Test
    public void get_dimensions() throws Exception {
        String dimensions = product.get_dimensions();
        assertEquals("2x2x2", dimensions);
    }

}