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
        "<div class='grid-100'>"
            +"<div class='grid-5'>"
                +"<p>"+product.id+"</p>"
            +"</div>"
            +"<div class='grid-90'>"
                +"<p>"+product.name+"</p>"
            +"</div>"
            +"<div class='grid-5'>"
                +"<p>$"+product.purchase_price+"</p>"
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
                    console.log(productsFromAmazon[i]);
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