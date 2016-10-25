package com.zzheads.CompShop.web;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.zzheads.CompShop.model.*;
import com.zzheads.CompShop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.NaN;

@Controller
@Scope ("request")
public class IndexController {
    private final SupplierService supplierService;
    private final ShoppingCart shoppingCart;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final AddressService addressService;
    private final AwsService awsService;
    private final CurrencyService currencyService;
    private final QwintryService qwintryService;
    private final CityService cityService;
    private final SortingOrder sortingOrder;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public IndexController(
            CurrencyService currencyService,
            AddressService addressService,
            ShoppingCart shoppingCart,
            AwsService awsService,
            QwintryService qwintryService,
            CategoryService categoryService,
            SupplierService supplierService,
            CityService cityService,
            ProductService productService,
            SortingOrder sortingOrder,
            ShoppingCartService shoppingCartService)
    {
        this.currencyService = currencyService;
        this.addressService = addressService;
        this.shoppingCart = shoppingCart;
        this.awsService = awsService;
        this.qwintryService = qwintryService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.cityService = cityService;
        this.productService = productService;
        this.sortingOrder = sortingOrder;
        this.shoppingCartService = shoppingCartService;
    }

    private void initShoppingCart() throws Exception {
        if (shoppingCart.getCity() == null) {
            shoppingCart.setCity(qwintryService.findCityByName("Волгоград"));
        }
        if (shoppingCart.getDeliveryCostPerKg() == 0.0) {
            shoppingCart.setDeliveryCostPerKg(shoppingCartService.getDeliveryCost());
        }
    }

    @ModelAttribute("products")
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @ModelAttribute("city_delivery")
    public String getCityDelivery() throws Exception {
        initShoppingCart();
        return shoppingCart.getCity().getName();
    }

    @ModelAttribute("total")
    public long evaluateTotal() throws Exception {
        initShoppingCart();
        return (long) (shoppingCart.evaluateTotal()*getRate()*getAmazonPercent()+evaluateDeliveryCost());
    }

    @ModelAttribute("delivery_cost")
    public double evaluateDeliveryCost() throws Exception {
        initShoppingCart();
        return shoppingCart.deliveryCost();
    }

    @ModelAttribute("sorting_order")
    public String getSortingOrder() {
        if (sortingOrder.getOrder() == null) {
            sortingOrder.setOrder("по Имени");
        }
        return sortingOrder.getOrder();
    }

    @ModelAttribute("amazon_percent")
    public double getAmazonPercent() {
        return DollarRateServiceImpl.AMAZON_PERCENT;
    }

    @ModelAttribute("dollar_rate")
    public double getRate() throws SAXException, UnirestException, ParserConfigurationException, IOException {
        double rate = currencyService.getTodayDollarRate();
        return rate;
    }

    @ModelAttribute ("cities")
    public Collection<String> getCititesForDelivery () throws Exception {
        Collection<City> allCities = cityService.findAll();
        if (allCities == null || allCities.size()==0) {
            allCities = qwintryService.getCities();
            for (City city : allCities)
                cityService.save(city);
        }
        return cityService.findAllCitiesNames();
    }

    @ModelAttribute("allCategories")
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @ModelAttribute("loggedUsername")
    public static String getLoggedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index (Model model) {
        return "index";
    }

    @RequestMapping(path = "/details/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String detailsById (@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "detail";
    }

    @RequestMapping(path = "/admin", method = RequestMethod.GET)
    public String admin (Model model) {
        model.addAttribute("count", productService.count());
        return "admin";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "USER";
            }
        });
        model.addAttribute("user", new User("user", "password", authorities));
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
            // "flash" session attribute must not exist...do nothing and proceed normally
        }
        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login"; //You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }

    

}
