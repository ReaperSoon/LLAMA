function updateCartTotal() {
    var price = 0;
    $("#cart-rows").find(".price").each(function() {
        price += Number($(this).text());
    });
    $("#price").text("Total price : $"+price.toFixed(2));
}

$(function(){
    $(".orderby").click(function(){
        $("#products").load("/orderBy/"+$(this).find("input").val());
        setTimeout(function(){
            $(".small-rating").each(function() {
                $(this).rating(smallRatingOpts);
            });
        }, 100);
    })

    $("#resetFilter").click(function() {
        location.href = "/resetFilter";
    })

    $("#searchForm").submit(function(){
        var url = "/filterProduct/"+$(this).find("input[type=radio]:checked").val()+"/"+$("#search").val();
        $.ajax({
            url: url,
            method: "get",
            success: function(data) {
                $("#products").html(data);
                $(".small-rating").each(function() {
                    $(this).rating(smallRatingOpts);
                });
            }
        })
    })

    $(".comment-form").on('submit',function() {
        var form = this;
        $.ajax({
            url: "/addComment",
            method: "post",
            data: $(this).serialize(),
            success: function(data){
                $(form).parents('.tab-pane').find(".comment-list").append(data);
                $(".need-init").each(function() {
                    $(this).rating(smallRatingOpts);
                });
                $(form).find("input[name!=productId], textarea").each(function(){
                    if ($(this).attr('name') == "rating")
                        $(this).rating("clear");
                    $(this).val("");
                    $(this).text("");
                })
            }
        })
    })

    $(".add-cart").on('click',function(){
        var input = $(this).parents(".form-inline").find("input");
        $.ajax({
            url: "/addCart/"+input.data("id")+"/"+input.val(),
            method: "get",
            success: function(data) {
                $("#cart-rows").append(data);
                updateCartTotal();
            }
        })
    });

    $(document).delegate(".remove-from-cart",'click', function() {
        var itemKey = $(this).parents(".cart-row").data("key");
        $.get("/removeFromCart/"+itemKey);
        $(this).parents(".cart-row").remove();
        updateCartTotal();
    });

    $(".quantity-less, .quantity-more").on('click',function() {
        var input = $(this).parents(".input-group").find("input");
        if ($(this).hasClass("quantity-less"))
            input.val(input.val()>1?parseInt(input.val()-1):1);
        else
            input.val(parseInt(input.val())+1);
    });

    updateCartTotal();
    $(".tooltip-init").each(function(){
        $(this).tooltip();
    })
});