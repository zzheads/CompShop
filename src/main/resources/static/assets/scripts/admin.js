var productsFromAmazon = [];
console.log("admin.js");

function cartProductFromAmazon (product, count) {
    if (product.description==null) product.description="n/a";
    if (product.height==null) product.height="-";
    if (product.length==null) product.length="-";
    if (product.width==null) product.width="-";
    if (product.weight==null) product.weight="-";
    if (product.units_l==null) product.units_l="n/a";
    if (product.units_w==null) product.units_w="n/a";
    if (product.purchase_price==null) product.purchase_price="-";
    if (product.small_image==null) product.small_image="n/a";
    if (product.medium_image==null) product.medium_image="n/a";
    if (product.large_image==null) product.large_image="n/a";

    var productCard = $("<div id='productCard #" + product.id + "' class='card horizontal'></div>");
    var cardImage = $("<div class='card-image'></div>");
    cardImage.append("<div style='float: left; width: 150px; height: 150px; overflow: hidden; background: url(\"" + product.medium_image + "\") center center no-repeat; margin: 20px;'></div>");
    var cardStacked = $("<div class='card-stacked'></div>");
    var cardContent = $("<div class='card-content'></div>");
    cardContent.append("<h6 id='asin#"+count+"'>"+product.asin+"</h6>");
    cardContent.append("<h6>"+product.name+"</h6>");
    cardContent.append(getDescriptionAsList(product.description));
    cardContent.append("<h6>"+product.height+"x"+product.length+"x"+product.width+" "+product.units_l+"</h6>");
    cardContent.append("<h6>"+product.weight+" "+product.units_w+"</h6>");
    cardStacked.append(cardContent);
    var cardAction = $("<div class='card-action'></div>");
    cardAction.append("<span class='red-text text-accent-4 left'>" + formatDecimal(product.purchase_price,',','.') + "</span>");
    cardAction.append("<span class='left'>$</span>");
    cardStacked.append(cardAction);
    productCard.append(cardImage);
    productCard.append(cardStacked);
    return productCard;
}

function getDescriptionAsList (description) {
    var desc = description.split('%n');
    var list = $("<ul></ul>");
    for (var i=0;i<desc.length;i++) {
        list.append("<li class='fontsmall grey-text'>"+desc[i]+"</li>");
    }
    return list;
}

function updateProducts (products) {
    var root = $("#productsList");
    if (products != null) {
        for (var i=0;i<products.length;i++) {
            products[i] = updateProduct(products[i]);
        }
    }
    root.children().remove();
    if (products != null) {
        for (i=0;i<products.length;i++) {
            var product = $("<div class='col s6 no-margin'></div>").append(cartProductFromAmazon(products[i], i));
            root.append(product);
        }
    }
}

function getFromAmazon () {
    var keywords = document.getElementById("keywords").value;
    var indexSearch = document.getElementById("indexSearch").value;
    $.ajax({
        url: "/itemSearch/"+keywords+"/"+indexSearch,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (products) {
            productsFromAmazon = products;
            updateProducts(productsFromAmazon);
        },
        error: getErrorMsg
    });
}

function updateProduct (product) {
    if (product.name == null) product.name = getPropertyOfProduct("name", product.asin);
    if (product.purchase_price == null) product.purchase_price = getPropertyOfProduct("purchase_price", product.asin);
    if (product.small_image == null) product.small_image = getPropertyOfProduct("small_image", product.asin);
    if (product.medium_image == null) product.medium_image = getPropertyOfProduct("medium_image", product.asin);
    if (product.large_image == null) product.large_image = getPropertyOfProduct("large_image", product.asin);
    if (product.description == null) product.description = getPropertyOfProduct("description", product.asin);
    if (product.height == null) product.height = getPropertyOfProduct("height", product.asin);
    if (product.length == null) product.length = getPropertyOfProduct("length", product.asin);
    if (product.width == null) product.width = getPropertyOfProduct("width", product.asin);
    if (product.weight == null) product.weight = getPropertyOfProduct("weight", product.asin);
    if (product.units_l == null) product.units_l = getPropertyOfProduct("units_l", product.asin);
    if (product.units_w == null) product.units_w = getPropertyOfProduct("units_w", product.asin);
    if (product.supplier == null) product.supplier = {name: "amazon.com"};
    if (product.category == null) product.category = {name: getSelectedIndex()};
    return product;
}

function updateImages(products) {
    for (var i=0;i<productsFromAmazon.length;i++) {
        var asin = productsFromAmazon[i].asin;
        var small_image = findProperty(products, "small_image", asin);
        var medium_image = findProperty(products, "medium_image", asin);
        var large_image = findProperty(products, "large_image", asin);
        if (small_image != null) productsFromAmazon[i].small_image = small_image;
        if (medium_image != null) productsFromAmazon[i].medium_image = medium_image;
        if (large_image != null) productsFromAmazon[i].large_image = large_image;
    }
}

function updatePrices(products) {
    for (var i=0;i<productsFromAmazon.length;i++) {
        var asin = productsFromAmazon[i].asin;
        var price = findProperty(products, "purchase_price", asin);
        if (price != null) {
            productsFromAmazon[i].purchase_price = price;
            productsFromAmazon[i].retail_price = price*1.1;
        }
    }
}

function findProperty(products, nameProperty, asin) {
    for (var i=0;i<products.length;i++) {
        if (products[i].asin == asin)
            return products[i][nameProperty];
    }
    return null;
}

function getImages() {
    var asins = getAsins();
    $.ajax({
        url: "/itemsImages",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        data: JSON.stringify(asins, null, "\t"),
        success: function (products) {
            updateImages(products);
            updateProducts(productsFromAmazon);
            printFlashMessage("Фото всех"+products.length+" продуктов обновлены", "success");
        },
        error: getErrorMsg
    });
}

function getPrices() {
    var asins = getAsins();
    $.ajax({
        url: "/itemsPrices",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        data: JSON.stringify(asins, null, "\t"),
        success: function (products) {
            updatePrices(products);
            updateProducts(productsFromAmazon);
            printFlashMessage("Цена всех"+products.length+" продуктов обновлена", "success");
        },
        error: getErrorMsg
    });
}

function applyToBase () {
    $.ajax({
        url: "/products",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(productsFromAmazon, null, "\t"),
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (products) {
            printFlashMessage("Все "+products.length+" продуктов успешно сохранены в базе", "success");
        },
        error: getErrorMsg
    });
}

function getSelectedIndex () {
    console.log("getSelectedIndex");
    var id = "indexSearch";
    var selectedIndex = document.getElementById(id).value;
    return selectedIndex;
}

function getAsins () {
    var asins = [];
    for (var i=0;i<productsFromAmazon.length;i++)
        asins.push(productsFromAmazon[i].asin);
    return asins;
}

function getPropertyOfProduct (nameOfProperty, asin) {
    var name = nameOfProperty+"#"+asin;
    return $("meta[name='"+name+"']").attr("content");
}

function deleteProduct (id) {
    if (id != null) {
        $.ajax({
            url: "/product/" + id,
            type: "DELETE",
            dataType: "json",
            contentType: "application/json",
            headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
            success: function () {
                document.getElementById("productCard #" + id).remove();
                var $total = document.getElementById("countProducts");
                $total.innerText = parseInt($total.innerText)-1;
                printFlashMessage("Продукт (id=" + id + ") успешно удален из базы", "success");
            },
            error: getErrorMsg
        });
    } else {
        printFlashMessage("Данный продукт еще не сохранен в базе. Невозможно удалить.", "failure");
    }
}

function getCostDelivery(id) {
    $.ajax({
        url: "/costpickup/"+id,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (cost) {
            printFlashMessage("Стоимость доставки составит $" + cost, "info");
        },
        error: getErrorMsg
    });
}

function calcDelivery () {
    $.ajax({
        url: "/costpickup",
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (count) {
            printFlashMessage("Стоимость доставки посчитана и установлена "+count+" продуктам в базе", "success");
        },
        error: getErrorMsg
    });
}