package com.zzheads.CompShop.web.api;

import com.zzheads.CompShop.model.City;
import com.zzheads.CompShop.model.PickupRequest;
import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.model.ShoppingCart;
import com.zzheads.CompShop.service.CityService;
import com.zzheads.CompShop.service.ProductService;
import com.zzheads.CompShop.service.QwintryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Scope("request")
@Controller
public class QwintryApi {
    private final QwintryService qwintryService;
    private final ProductService productService;
    private final ShoppingCart shoppingCart;
    private final CityService cityService;

    @Autowired
    public QwintryApi(ProductService productService, QwintryService qwintryService, ShoppingCart shoppingCart, CityService cityService) {
        this.productService = productService;
        this.qwintryService = qwintryService;
        this.shoppingCart = shoppingCart;
        this.cityService = cityService;
    }

    @RequestMapping(path = "/costpickup", method = RequestMethod.GET)
    public @ResponseBody String costPickupAll () throws Exception {
        City cityForDelivery = shoppingCart.getCity();
        if (cityForDelivery.getPickupPoints() == null) {
            cityForDelivery.setPickupPoints(qwintryService.getLocationsByCity(cityForDelivery.getName()));
        }
        String deliveryPoint = cityForDelivery.getPickupPoints().get(0); // первая попавшаяся точка
        int count = 0;
        for (Product product : productService.findAll()) {
            try {
                PickupRequest pickupRequest = product.getPickupRequest(deliveryPoint, "false");
                String costDelivery = qwintryService.getCostPickup(pickupRequest.toJson());
                product.setDelivery(Double.parseDouble(costDelivery));
                productService.save(product);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Integer.toString(count);
    }

    @RequestMapping(path = "/costpickup/{id}", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody String costPickup (@PathVariable Long id) throws Exception {
        Product product = productService.findById(id);
        return getDeliveryCost(product, shoppingCart, qwintryService);
    }

    @RequestMapping(path = "/costpickup_bycityname/{city}", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody String calculateDeliveryForCity (@PathVariable String city) throws Exception {
        City newCityForDelivery = cityService.findByCityName(city);
        shoppingCart.setCity(newCityForDelivery);
        return costPickupAll();
    }

    public static String getDeliveryCost (Product product, ShoppingCart shoppingCart, QwintryService qwintryService) throws Exception {
        City cityForDelivery = shoppingCart.getCity();
        if (cityForDelivery.getPickupPoints() == null) {
            cityForDelivery.setPickupPoints(qwintryService.getLocationsByCity(cityForDelivery.getName()));
        }
        String deliveryPoint = cityForDelivery.getPickupPoints().get(0); // первая попавшаяся точка
        PickupRequest pickupRequest = product.getPickupRequest(deliveryPoint, "false");
        try {
            return qwintryService.getCostPickup(pickupRequest.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
