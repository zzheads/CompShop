package com.zzheads.CompShop.web.api;

import com.zzheads.CompShop.model.Purchase;
import com.zzheads.CompShop.model.SortingOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Scope("request")
@Controller
public class SortingOrderApi {
    @Autowired
    private SortingOrder sortingOrder;

    @RequestMapping(path = "/sorting", method = RequestMethod.POST, produces = {"application/json"}, consumes = {"application/json"})
    public void setSortingOrder(@RequestBody String order) {
        sortingOrder.setOrder(order);
    }
}
