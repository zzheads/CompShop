package com.zzheads.CompShop.web.api;

import com.zzheads.CompShop.model.PickupRequest;
import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.service.ProductService;
import com.zzheads.CompShop.service.QwintryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class QwintryApi {
    private final QwintryService qwintryService;
    private final ProductService productService;

    @Autowired
    public QwintryApi(ProductService productService, QwintryService qwintryService) {
        this.productService = productService;
        this.qwintryService = qwintryService;
    }

    @RequestMapping(path = "/costpickup", method = RequestMethod.GET)
    public @ResponseBody String costPickupAll () {
        int count = 0;
        for (Product product : productService.findAll()) {
            try {
                PickupRequest pickupRequest = product.getPickupRequest("msk_1", "false");
                String costDelivery = qwintryService.getCostPickup(pickupRequest.toJson());
                product.setDeliveryMsk(Double.parseDouble(costDelivery));
                productService.save(product);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
                return ("-1");
            }
        }
        return Integer.toString(count);
    }

    @RequestMapping(path = "/costpickup/{id}", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody String costPickup (@PathVariable Long id) {
        Product product = productService.findById(id);
        PickupRequest pickupRequest = product.getPickupRequest("msk_1", "false");
        try {
            return qwintryService.getCostPickup(pickupRequest.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(path = "/costpickup/{city}", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody String calculateDeliveryForCity (@PathVariable String city) {
        int count = 0;
        for (Product product : productService.findAll()) {
            try {
                String deliveryPoint = qwintryService.getLocationsByCity(city).get(0);
                PickupRequest pickupRequest = product.getPickupRequest(deliveryPoint, "false");
                String costDelivery = qwintryService.getCostPickup(pickupRequest.toJson());
                product.setDeliveryMsk(Double.parseDouble(costDelivery));
                productService.save(product);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
                return ("-1");
            }
        }
        return Integer.toString(count);
    }
}
