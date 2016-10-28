package com.zzheads.CompShop.web;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.zzheads.CompShop.dto.UserDto;
import com.zzheads.CompShop.model.*;
import com.zzheads.CompShop.service.*;
import com.zzheads.CompShop.utils.FlashMessage;
import com.zzheads.CompShop.web.api.MailApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    
    private static final String[] INDEXES_SEARCH = {"Wireless", "PCHardware", "All", "UnboxVideo", "Appliances", "ArtsAndCrafts", "Automotive", "Baby", "Beauty", "Books", "Music", "Fashion",
            "FashionBaby", "FashionBoys", "FashionGirls", "FashionMen", "FashionWomen", "Collectibles", "MP3Downloads", "Electronics", "GiftCards", "Grocery", "HealthPersonalCare",
            "HomeGarden", "Industrial", "KindleStore", "Luggage", "Magazines", "Movies", "MusicalInstruments", "OfficeProducts", "LawnAndGarden", "PetSupplies", "Pantry",
            "Software", "SportingGoods", "Tools", "Toys", "VideoGames", "Wine", "Blended"};

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
            ShoppingCartService shoppingCartService,
            UserService userService,
            RoleService roleService,
            PasswordEncoder passwordEncoder)
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
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
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

    @RequestMapping(path = "/shoppingcart", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String shoppingCart (Model model) {
        model.addAttribute("purchases", shoppingCart.getPurchases());
        return "shopcart";
    }

    @RequestMapping(path = "/address", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getAddress (Model model) {
        String[] countries = {"Россия", "Белоруссия", "Казахстан"};
        String[] cities = {"Волгоград", "Москва", "Минск"};

        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        return "address";
    }

    @RequestMapping(path = "/address", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody public String postAddress (@RequestBody String jsonAddress) {
        Gson gson = new Gson();
        return gson.toJson("success");
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String signUp (Model model) {
        List<String> registeredUsernames = userService.getRegisteredUsernames();
        model.addAttribute("registeredUsernames", registeredUsernames);
        return "register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody public String registerNewUser (@RequestBody String jsonUser) {
        Gson gson = new Gson();
        UserDto userDto = gson.fromJson(jsonUser, UserDto.class);
        Role userRole = roleService.findByName("USER");
        if (userRole == null) {
            userRole = new Role("USER");
            roleService.save(userRole);
        }
        User user = new User(userDto.getUsername().toLowerCase(), passwordEncoder.encode(userDto.getPassword()), false, userRole);
        userService.save(user);

        // сформировать ссылку
        String confirm = passwordEncoder.encode(user.getId().toString()+user.getUsername()+user.getPassword());
        // отослать письмо с ссылкой
        final String HOST_NAME = "http://localhost:8080";
        String body = "Для активации вашего аккаунта на сайте zdelivery.ru перейдите по ссылке ниже: \n"+
                HOST_NAME+"/confirm?confirmString="+confirm;
        MailApi.sendMail("zzheads@gmail.com", "www.zdelivery.ru", body, "Пожалуйста подтвердите ваш аккаунт на сайте zdelivery.ru");
        return gson.toJson("success");
    }

    @RequestMapping(path = "/profile/{id}", method = RequestMethod.GET)
    public String profileById (@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "profile";
    }

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public String profile (Model model) {
        User user = userService.findByName(getLoggedUser());
        model.addAttribute("user", user);
        return "profile";
    }

    @RequestMapping(path = "/admin", method = RequestMethod.GET)
    public String admin (Model model) {
        Arrays.sort(INDEXES_SEARCH);
        model.addAttribute("count", productService.count());
        model.addAttribute("indexes", INDEXES_SEARCH);
        return "admin";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User("user", "password", true, new Role("USER")));
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);
            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
            // "flash" session attribute must not exist...do nothing and proceed normally
        }
        return "login";
    }

    @RequestMapping(path = "/confirm", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String confirmEmail (@RequestParam String confirmString, Model model) {
        for (User user : userService.findAll()) {
            if (passwordEncoder.matches(user.getId().toString()+user.getUsername()+user.getPassword(), confirmString)) {
                user.setEnabled(true);
                userService.save(user);
                model.addAttribute("username", user.getUsername());
                model.addAttribute("password", user.getPassword()); // хрень, все равно он уже зашифрован
                model.addAttribute("flash", new FlashMessage("Адрес электронной почты "+user.getUsername()+" успешно подтвержден.", FlashMessage.Status.SUCCESS));
                break;
            }
        }
        if (!model.containsAttribute("flash")) {
            model.addAttribute("flash", new FlashMessage("Ваша ссылка "+confirmString+" недействительна. Пользователь не найден.", FlashMessage.Status.FAILURE));
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
