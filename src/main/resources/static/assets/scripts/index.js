function getAllProducts() {
    var root = $("#root");
    $.ajax({
        url: "/product",
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

function addProductToCart (buttonId) {
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
            document.getElementById("shoppingCart").innerText = getTotal(purchases)*getAmazonPercent()*getDollarRate()+" RUB";
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
            root.append(getHtmlDetailProduct(product));
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
    var htmlString =
        "<div id='product#'"+product.id+" class='grid-100 lightgray-box'>"
            +"<div class='grid-15'>"
                +"<img src='"+product.photo+"' width='120px'/>"
            +"</div>"
            +"<a id='ahref#"+product.id+"' href='#'  onclick='getProductDetails(this.id)'>"
                +"<div class='grid-75'>"
                    +"<h3>Name: "+product.name+"</h3>"
                    +"<p>Цена: "+(product.retail_price*getAmazonPercent()*getDollarRate()).toFixed(0).toLocaleUpperCase()+" RUB</p>"
                    +"<p>с доставкой: "+(product.retail_price*getAmazonPercent()*getDollarRate()+product.delivery_msk*getDollarRate()).toFixed(0).toLocaleUpperCase()+" RUB</p>"
                +"</div>"
            +"</a>"
            +"<div class='grid-10'>"
                +"<button id='button#"+product.id+"' type='button' class='modern' onclick='addProductToCart(this.id)'>Купить</button>"
            +"</div>"
        +"</div>";

    return htmlString;
}

function getHtmlDetailProduct(product) {
    var category = JSON.parse(product.category);
    var supplier = JSON.parse(product.supplier);
    var toRubles = getAmazonPercent()*getDollarRate();
    var htmlString =
        "<div id='product#"+product.id+"' class='grid-100 lightgray-box'>"
            +"<div class='grid-30'>"
                +"<img src='"+product.photo+"' width='120px' onclick='zoom(this.src)'/>"
            +"</div>"
            +"<div class='grid-60'>"
                +"<p>("+category.name+"):</p>"
                +"<h2>"+product.name+"</h2>"
                +"<h4>"+product.description+"</h4>"
                +"<p>Цена: "+(product.retail_price*toRubles).toFixed(0).toLocaleUpperCase()+" RUB</p>"
                +"<p>Доставка (до Москвы): "+(product.delivery_msk*getDollarRate()).toFixed(0).toLocaleUpperCase()+" RUB</p>"
                +"<p class='cost'>Цена с доставкой: "+(product.retail_price*toRubles+product.delivery_msk*getDollarRate()).toFixed(0).toLocaleUpperCase()+" RUB</p>"
                +"<p>Поставщик: "+supplier.name+"</p>"
            +"</div>"
            +"<div class='grid-10'>"
                +"<button id='button#"+product.id+"' type='button' class='modern' onclick='addProductToCart(this.id)'>Купить</button>"
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
                                +"<img src='"+purchases[i].product.photo+"' height='100px'>"
                            +"</div>"
                        +"</td>"
                        +"<td style='vertical-align: middle'>"
                            +"<div class='grid-100'>"
                                +"<p>"
                                    +"<h3>"+purchases[i].product.name+"</h3>"
                                    +"<h5>"+purchases[i].product.description+"</h5>"
                                +"</p>"
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
    console.log($("meta[name='dollar_rate']").attr("content"));
    return $("meta[name='dollar_rate']").attr("content");
}

function getAmazonPercent() {
    console.log($("meta[name='amazon_percent']").attr("content"));
    return $("meta[name='amazon_percent']").attr("content");
}