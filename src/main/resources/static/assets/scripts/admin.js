function startAdmin(){
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
                    root.append(printProduct(allProducts[i]));
                }
        },
        error: getErrorMsg
    });
}

function printProduct (product) {
    var htmlString =
        "<div id='product#"+product.id+"' class='grid-100'>"
            +"<div class='grid-5'>"
                +"<p>"+product.asin+"</p>"
            +"</div>"
            +"<div class='grid-10'>"
                +"<img src='"+product.photo+"' height='30px'/>"
            +"</div>"
            +"<div class='grid-30'>"
                +"<p>"+product.name+"</p>"
            +"</div>"
            +"<div class='grid-30'>"
                +"<p>"+product.description+"</p>"
            +"</div>"
            +"<div class='grid-5'>"
                +"<p>$"+product.purchase_price+"</p>"
            +"</div>"
            +"<div class='grid-5'>"
                +"<button id='"+product.id+"' class='mac' onclick='deleteProduct(this.id)'>[-]</button>"
            +"</div>"
            +"<div class='grid-5'>"
                +"<button id='"+product.id+"' class='mac' onclick='getCostDelivery(this.id)'>Delivery Cost</button>"
            +"</div>"
        +"</div>";
    return htmlString;
}

function printProductFromAmazon (product, count) {
    if (product.description==null) product.description="n/a";
    if (product.height==null) product.height="-";
    if (product.length==null) product.length="-";
    if (product.width==null) product.width="-";
    if (product.weight==null) product.weight="-";
    if (product.units_l==null) product.units_l="n/a";
    if (product.units_w==null) product.units_w="n/a";
    if (product.purchase_price==null) product.purchase_price="-";
    var htmlString =
        "<div class='grid-100'>"
        +"<div class='grid-10'>"
            +"<p id='asin#"+count+"'>"+product.asin+"</p>"
            +"<meta name='name#"+product.asin+"' content='"+product.name+"'/>"
            +"<meta name='description#"+product.asin+"' content='"+product.description+"'/>"
            +"<meta name='height#"+product.asin+"' content='"+product.height+"'/>"
            +"<meta name='length#"+product.asin+"' content='"+product.length+"'/>"
            +"<meta name='width#"+product.asin+"' content='"+product.width+"'/>"
            +"<meta name='weight#"+product.asin+"' content='"+product.weight+"'/>"
            +"<meta name='units_l#"+product.asin+"' content='"+product.units_l+"'/>"
            +"<meta name='units_w#"+product.asin+"' content='"+product.units_w+"'/>"
        +"</div>"
        +"<div class='grid-10'>"
            +"<img src='"+product.photo+"' height='30px'/>"
        +"</div>"
        +"<div class='grid-20'>"
            +"<p>"+product.name+"</p>"
        +"</div>"
        +"<div class='grid-30'>"
            +"<p>"+product.description+"</p>"
        +"</div>"
        +"<div class='grid-10'>"
            +"<p>"+product.height+"*"+product.length+"*"+product.width+" "+product.units_l+"</p>"
        +"</div>"
        +"<div class='grid-10'>"
            +"<p>"+product.weight+" "+product.units_w+"</p>"
        +"</div>"
        +"<div class='grid-10'>"
            +"<p>$"+product.purchase_price+"</p>"
        +"</div>"
        +"</div>";
    return htmlString;
}

function getFromAmazon () {
    var root = $("#root");
    var keywords = document.getElementById("keywords").value;
    var indexSearch = document.getElementById("indexSearch").value;
    $.ajax({
        url: "/itemSearch/"+keywords+"/"+indexSearch,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
        success: function (productsFromAmazon) {
            root.children().remove();
            if (productsFromAmazon!=null) {
                for (var i = 0; i < productsFromAmazon.length; i++) {
                    root.append(printProductFromAmazon(productsFromAmazon[i], i));
                }
            } else {
                printFlashMessage("Ничего не найдено по этому запросу","info");
            }
        },
        error: getErrorMsg
    });
}

function applyToBase () {
    var asins = getAsins();
    console.log(asins);
    for (var i=0;i<asins.length;i++) {
        $.ajax({
            url: "/itemLookup/"+asins[i],
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
            success: function (product) {
                if (product.purchase_price == null) {
                    printFlashMessage("Не удается получить информацию о цене продукта с ASIN="+product.asin, "failure");
                }
                if (product.small_image == null && product.medium_image == null && product.large_image == null) {
                    printFlashMessage("Не удается получить информацию о изображении продукта с ASIN="+product.asin, "failure");
                }
                product.name = getPropertyOfProduct("name", product.asin);
                product.description = getPropertyOfProduct("description", product.asin);
                product.height = getPropertyOfProduct("height", product.asin);
                product.length = getPropertyOfProduct("length", product.asin);
                product.width = getPropertyOfProduct("width", product.asin);
                product.weight = getPropertyOfProduct("weight", product.asin);
                product.units_l = getPropertyOfProduct("units_l", product.asin);
                product.units_w = getPropertyOfProduct("units_w", product.asin);
                product.supplier = {
                    name: "amazon.com"
                };
                product.category = {
                  name: getSelectedIndex()
                };
                console.log(product);
                $.ajax({
                    url: "/product",
                    type: "POST",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(product, null, "\t"),
                    headers: {"X-CSRF-Token": $("meta[name='_csrf']").attr("content")},
                    success: function (product) {
                        printFlashMessage("Продукт (id="+product.id+", ASIN="+product.asin+") успешно сохранен в базе", "success");
                    },
                    error: getErrorMsg
                });
            },
            error: getErrorMsg
        });
    }
}

function getSelectedIndex () {
    console.log("getSelectedIndex");
    var id = "indexSearch";
    var selectedIndex = document.getElementById(id).value;
    return selectedIndex;
}

function getAsins () {
    var asins = [];
    var count = 0;
    var id;
    while (true) {
        id = "asin#"+count;
        if (document.getElementById(id)!=null)
            asins.push(document.getElementById(id).innerText);
        else
            return asins;
        count++;
    }
}

function getProduct (asin) {
    var product = {
        asin : asin,
        name: getPropertyOfProduct("name", asin),
        description: getPropertyOfProduct("description", asin),
        height: getPropertyOfProduct("height", asin),
        length: getPropertyOfProduct("length", asin),
        width: getPropertyOfProduct("width", asin),
        weight: getPropertyOfProduct("weight", asin),
        units_l: getPropertyOfProduct("units_l", asin),
        units_w: getPropertyOfProduct("units_w", asin)
    };
    return product;
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
                document.getElementById("product#" + id).remove();
                printFlashMessage("Продукт (id=" + id + ") успешно удален из базы", "success");
            },
            error: getErrorMsg
        });
    } else {
        printFlashMessage("Данный продукт еще не сохранен в базе. Невозможно удалить.", "failure");
    }
}

function getCostDelivery(id) {
    // var pickupRequest = {
    //     weight: product.weight,
    //     dimensions: product.height + "x" + product.length + "x" + product.width,
    //     delivery_pickup: "msk_1",
    //     insurance: "false",
    //     items_value: product.retail_price,
    //     units_length: product.units_l,
    //     units_weight: product.units_w
    // };

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