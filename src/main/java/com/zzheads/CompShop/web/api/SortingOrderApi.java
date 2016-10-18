package com.zzheads.CompShop.web.api;

import com.google.gson.Gson;
import com.zzheads.CompShop.model.SortingOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Scope("request")
@Controller
public class SortingOrderApi {
    @Autowired
    private SortingOrder sortingOrder;

    @RequestMapping(path = "/sorting/{order}", method = RequestMethod.GET, produces = {"application/json"}, consumes = {"application/json"})
    public @ResponseBody String setSortingOrder(@PathVariable String order) {
        Gson gson = new Gson();
        sortingOrder.setOrder(order);
        return gson.toJson(order);
    }
}
