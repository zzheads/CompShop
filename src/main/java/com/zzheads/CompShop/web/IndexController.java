package com.zzheads.CompShop.web;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.zzheads.CompShop.model.*;
import com.zzheads.CompShop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope ("request")
public class IndexController {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ShoppingCart shoppingCart;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AwsService awsService;
    @Autowired
    private CurrencyService currencyService;

    @ModelAttribute("total")
    public double evaluateTotal() {
        return shoppingCart.evaluateTotal();
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
