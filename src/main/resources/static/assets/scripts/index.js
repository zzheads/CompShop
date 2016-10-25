var allProducts = [];
console.log("index.js");

function getAllProducts() {
    var productList = $("#productsList");
    $.ajax({
        url: "/product",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (products) {
            allProducts = products;
            productList.children().remove();
            if (allProducts!=null) {
                for (var i = 0; i < allProducts.length; i++) {
                    var product = $("<div id='productCard #"+allProducts[i].id+"' class='col s6 no-margin'></div>");
                    var ahref = $("<a href='/details/'"+allProducts[i].id+"'></a>").append(getHtmlProduct(allProducts[i]));
                    product.append(ahref);
                    productList.append(product);
                }
            }
        },
        error: getErrorMsg
    });
}

function updateShoppingCart(newTotal) {
    var $divShoppingCart = $("#shoppingCart");
    var newTotalString = formatDecimal(newTotal,',','.') + " руб";
    var newHtml = "<a class='waves-effect waves-teal btn-flat no-margin white-text right'><i class='material-icons right'>shopping_cart</i>"+newTotalString+"</a>";
    $divShoppingCart.html(newHtml);
}

function addProductToCart (buttonId) {
    console.log(buttonId);
    var id = getIdFromElementId(buttonId);
    var product ={
        id: id
    };
    var purchase = {
        product: product,
        quantity: 1
    };

    $.ajax({
        url: "/add_purchase",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(purchase, null, "\t"),
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (purchases) {
            $.ajax({
                url: "/delivery_cost",
                type: "GET",
                dataType: "json",
                contentType: "application/json",
                success: function (delivery_cost) {
                    console.log("Check here");
                    updateShoppingCart(getTotal(purchases) * getAmazonPercent() * getDollarRate() + delivery_cost * getDollarRate());
                },
                error: getErrorMsg
            });
        },
        error: getErrorMsg
    });
}

function getProductDetails (buttonId) {
    var id = getIdFromElementId(buttonId);
    var root = $("#root");
    $.ajax({
        url: "/product/"+id,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (product) {
            root.children().remove();
            root.append("<div class='grid-100 empty-box-20'>");
            root.append(getHtmlDetailProduct(product));
            root.append("</div>");
        },
        error: getErrorMsg
    });
}

function getTotal (purchases) {
    var total = 0;
    for (var i=0;i<purchases.length;i++) {
        total += purchases[i].product.retail_price * purchases[i].quantity;
    }
    return total;
}

function getIdFromElementId (elementId) {
    return elementId.split('#').pop();
}

function getHtmlProduct (product) {
    var productCard = $("<div id='productCard #" + product.id + "' class='card horizontal'></div>");
    var cardImage = $("<div class='card-image'></div>");
    cardImage.append("<div style='float: left; width: 150px; height: 150px; overflow: hidden; background: url(\"" + product.medium_image + "\") center center no-repeat; margin: 20px;'></div>");
    var cardStacked = $("<div class='card-stacked'></div>");
    cardStacked.append("<div class='card-content'><h6>" + product.name + "</h6></div>");
    var cardAction = $("<div class='card-action'></div>");
    cardAction.append("<span class='red-text text-accent-4 left'>" + formatDecimal(product.retail_price * getDollarRate() * getAmazonPercent(),',','.') + "</span>");
    cardAction.append("<span class='left'>. руб</span>");
    cardAction.append("<a href='#' class='btn-flat orange darken-3 white-text right'>Купить</a>");
    cardStacked.append(cardAction);
    productCard.append(cardImage);
    productCard.append(cardStacked);
    return productCard;
}

function getThumbnail (product, detailed) {
    var src_small = product.small_image, src_medium = product.medium_image, src_large = product.large_image;
    var small_res = "width: 80px; height: 80px;", large_res = "width: 180px; height: 180px;";
    var htmlString = "";

    if (detailed) {
        htmlString = "<div class='zoomable' style='float: left; "+large_res+" overflow: hidden; background: url(\""+src_medium+"\") center center no-repeat; margin-right: 10px;' onclick='zoom(\""+src_large+"\")'></div>";
    } else {
        htmlString = "<div style='float: left; "+small_res+" overflow: hidden; background: url(\""+src_small+"\") center center no-repeat; margin-right: 10px;'></div>";
    }

    console.log("Thumbnail is here");
    return htmlString;
}

function getHtmlDetailProduct(product) {
    var category = JSON.parse(product.category);
    var supplier = JSON.parse(product.supplier);
    var toRubles = getAmazonPercent()*getDollarRate();
    var description = getDescription(product.description);
    var htmlString =
        "<div id='product#"+product.id+"' class='grid-100 lightgray-box'>"
            +"<div class='grid-20'>"
                +getThumbnail(product, true)
            +"</div>"
            +"<div class='grid-60'>"
                +"<p>("+category.name+"):</p>"
                +"<h2 class='product-title'>"+product.name+"</h2>";

    htmlString = htmlString + "<h4><ul>"
    for (var i=0;i<description.length;i++) {
        htmlString = htmlString + "<li>" + description[i] + "</li>";
    }
    htmlString = htmlString + "</ul></h4>"

    var city_delivery = document.getElementById("selectCity").value;

    htmlString = htmlString + "<p><table><tr><td>Цена: </td> <td style='padding-left: 10px; padding-right: 10px' class='cost'> "+(product.retail_price*toRubles).toFixed(0).toLocaleUpperCase()+"</td><td> RUB</td></tr></table></p>"
                +"<p><table><tr><td>Доставка (до "+city_delivery+"): </td> <td style='padding-left: 10px; padding-right: 10px' class='cost'> "+(product.delivery_msk*getDollarRate()).toFixed(0).toLocaleUpperCase()+"</td><td> RUB</td></tr></table></p>"
                +"<p><table><tr><td>Цена с доставкой: </td> <td style='padding-left: 10px; padding-right: 10px' class='cost'> "+(product.retail_price*toRubles+product.delivery_msk*getDollarRate()).toFixed(0).toLocaleUpperCase()+"</td><td> RUB</td></tr></table></p>"
                +"<p>Поставщик: "+supplier.name+"</p>"
            +"</div>"
            +"<div class='grid-20'>"
                +"<button id='button#"+product.id+"' type='button' class='primary' onclick='addProductToCart(this.id)'>Купить</button>"
            +"</div>"
        +"</div>";

    return htmlString;
}

function getHtmlShoppingCart(purchases) {
    var toRubles = getDollarRate()*getAmazonPercent();
    var htmlString = "";
    if (purchases == null || purchases.length == 0) {
        printFlashMessage("Ваша корзина пуста.", "info");
    } else {
        htmlString =
            "<div id='checkPurchases' class='grid-100 lightgray-box'>"
                +"<div class='grid-100'>"
                    +"<h2>Ваша корзина:</h2>"
                +"</div>"

                +getPurchaseString(purchases)

                +"<div class='prefix-85 grid-15'>"
                    +"<h2>Итого: "+(getTotal(purchases)*toRubles).toFixed(0).toLocaleUpperCase()+"</h2>"
                +"</div>"
                +"<div class='prefix-85 grid-15'>"
                    +"<button class='modern' type='button' onclick='checkOut()'>Оплатить</button>"
                +"</div>"
            +"</div>";
    }
    return htmlString;
}

function getPurchaseString (purchases) {
    var toRubles = getDollarRate()*getAmazonPercent();
    var string = "";
    for (var i=0;i<purchases.length;i++) {
        string +=
            "<div class='grid-100'>"
                +"<table>"
                    +"<tr>"
                        +"<td style='vertical-align: middle'>"
                            +"<div class='grid-100'>"
                                +getThumbnail(purchases[i].product, false)
                            +"</div>"
                        +"</td>"
                        +"<td style='vertical-align: middle'>"
                            +"<div class='grid-100'>"
                                +"<p>"
                                    +"<h3>"+purchases[i].product.name+"</h3>";

                                    var description = getDescription(purchases[i].product.description);
                                    string = string + "<h5><ul>"
                                    for (var j=0;j<description.length;j++) {
                                        string = string + "<li>" + description[j] + "</li>";
                                    }
                                    string = string + "</ul></h5>";

                                string = string + "</p>"
                            +"</div>"
                        +"</td>"
                        +"<td style='vertical-align: middle'>"
                            +"<div class='grid-10'>"
                                +"<h4>"+(purchases[i].product.retail_price*toRubles).toFixed(0).toLocaleUpperCase()+" RUB</h4>"
                            +"</div>"
                        +"</td>"
                        +"<td style='vertical-align: middle'>"
                            +"<div class='grid-10'>"
                                +"<style>"
                                    +"input[type=number]::-webkit-inner-spin-button {"
                                    +"opacity: 1"
                                    +"}"
                                +"</style>"
                                +"<input id='quantity#"+i+"' type='number' value='"+purchases[i].quantity+"' min='0' max='999' class='simple-input center' onchange='updateCart(this.id)'/>"
                            +"</div>"
                        +"</td>"
                        +"<td style='vertical-align: middle'>"
                            +"<div class='grid-10'>"
                                +"<h3>"+(purchases[i].quantity*purchases[i].product.retail_price*toRubles).toFixed(0).toLocaleUpperCase()+" RUB</h3>"
                            +"</div>"
                        +"</td>"
                    +"</tr>"
                +"</table>"
            +"</div>";
    }
    return string;
}

function checkShoppingCart() {
    var root = $("#root");
    $.ajax({
        url: "/purchases",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (purchases) {
            root.children().remove();
            root.append(getHtmlShoppingCart(purchases));
        },
        error: getErrorMsg
    });
}

function showAStore () {
    var aStore = "<iframe src='http://astore.amazon.com/zzheads-20' width='90%' height='4000' frameborder='0' scrolling='no'></iframe>";
    var root = $("#root");
    root.children().remove();
    root.append(aStore);
}

function showAbout () {
    var about = "Все товары поставляются из США, основные поставщики - amazon.com, apple.com. Срок поставки зависит от конкретного товара, в среднем составляет не более 2-х недель с момента оплаты. Цены указаны в долларах США. Гарантия на комплектующие - от года до трех лет? на телефоны iPhone - 1 год, с момента поставки товара."
    +"Указанная стоимость товара включает в себя все расходы по транспортировке, растамаживанию и пр.";
    printFlashMessage(about, "info");
}

function updateCart (inputId) {
    var root = $("#root");
    var index = getIdFromElementId(inputId);
    var updatedQuantity = document.getElementById(inputId).value;
    var data = {
        index: index,
        quantity: updatedQuantity
    };

    $.ajax({
        url: "/update_purchase",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        data: JSON.stringify(data, null, "\t"),
        success: function (purchases) {
            root.children().remove();
            root.append(getHtmlShoppingCart(purchases));
            document.getElementById("shoppingCart").innerText = (getTotal(purchases)*getAmazonPercent()*getDollarRate()).toFixed(0).toLocaleUpperCase()+" RUB";
        },
        error: getErrorMsg
    });
}

function getSelectedCategory (categoryId) {
    console.log(categoryId);
    var root = $("#root");
    $.ajax({
        url: "/product_by_category/"+categoryId,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (allProducts) {
            root.children().remove();
            if (allProducts!=null)
                for (var i=0;i<allProducts.length;i++) {
                    root.append(getHtmlProduct(allProducts[i]));
                }
        },
        error: getErrorMsg
    });
}

function searchProducts (pattern) {
    if (pattern == "") {
        getAllProducts();
        return;
    }
    console.log(pattern);
    var root = $("#root");
    $.ajax({
        url: "/product_by_search/"+pattern,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (allProducts) {
            root.children().remove();
            if (allProducts!=null)
                for (var i=0;i<allProducts.length;i++) {
                    root.append(getHtmlProduct(allProducts[i]));
                }
        },
        error: getErrorMsg
    });
}

function getDollarRate() {
    console.log("DollarRate = "+$("meta[name='dollar_rate']").attr("content"));
    return $("meta[name='dollar_rate']").attr("content");
}

function getAmazonPercent() {
    console.log("AmazonPercent = "+$("meta[name='amazon_percent']").attr("content"));
    return $("meta[name='amazon_percent']").attr("content");
}

function getSelectedCity (city) {
    console.log(city);
    $.ajax({
        url: "/costpickup_bycityname/"+city,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (count) {
            printFlashMessage("Стоимость доставки пересчитана для всех "+count+"продуктов в базе.", "success");
            document.getElementById("deliveryCity").innerHTML=city;
            getAllProducts();
        },
        error: getErrorMsg
    });
}

function getDescription (stringDescription) {
    var description = stringDescription.split('%n');
    return description;
}

function changeSortingOrder (order) {
    console.log(order);
    $.ajax({
        url: "/sorting/"+order,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (newOrder) {
            getAllProducts();
        },
        error: getErrorMsg
    });
}

function checkOut() {

}