package com.zzheads.CompShop.web.api;

import com.google.gson.Gson;
import com.zzheads.CompShop.model.Purchase;
import com.zzheads.CompShop.model.ShoppingCart;
import com.zzheads.CompShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Scope("request")
@Controller
public class ShoppingCartApi {
    private final ProductService productService;
    private final ShoppingCart shoppingCart;

    @Autowired
    public ShoppingCartApi(ProductService productService, ShoppingCart shoppingCart) {
        this.productService = productService;
        this.shoppingCart = shoppingCart;
    }

    @RequestMapping(path = "/purchases", method = RequestMethod.GET, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getPurchases() {
        return Purchase.toJson(shoppingCart.getPurchases());
    }

    @RequestMapping(path = "/add_purchase", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String addToShoppingCart (@RequestBody String jsonString) {
        Purchase purchase = Purchase.fromJson(jsonString);
        purchase.setProduct(productService.findById(purchase.getProduct().getId()));
        shoppingCart.addPurchase(purchase);
        return Purchase.toJson(shoppingCart.getPurchases());
    }

    @RequestMapping(path = "/update_purchase", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String updateShoppingCart (@RequestBody String jsonString) {
        Gson gson = new Gson();
        UpdateCart updateCart = gson.fromJson(jsonString, UpdateCart.class);
        long productId = updateCart.getProductid();
        int quantity = updateCart.getQuantity();

        if (quantity>0) {
            shoppingCart.getByProductId(productId).setQuantity(quantity);
        } else {
            shoppingCart.removePurchaseByProductId(productId);
        }
        return Purchase.toJson(shoppingCart.getPurchases());
    }

    @RequestMapping(path = "/delivery_cost", method = RequestMethod.GET, produces = {"application/json"}, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getDeliveryCost() {
        Gson gson = new Gson();
        return gson.toJson(shoppingCart.deliveryCost(), double.class);
    }

    private class UpdateCart {
        private int productid;
        private int quantity;

        public int getProductid() {
            return productid;
        }

        public void setProductid(int productid) {
            this.productid = productid;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
