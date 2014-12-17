$(function(){

// USER
    $(".user-edit").click(function() {
        var userId = $(this).parents('tr').data("id");
        $("#modal").load("/editUser/"+userId).modal('toggle');
    });

    $("#addUser").click(function(){
        $("#modal").load("/createAccount").modal('toggle');
        setTimeout(function(){
            $("#create-account-form").attr('action', '/adminCreateAccount');
        }, 200);
    });

// PRODUCT
    $("#addProduct").click(function(){
        $("#modal").load("/productForm").modal('toggle');
    });

    $(".product-edit").click(function() {
        var productId = $(this).parents('tr').data("id");
        $("#modal").load("/editProduct/"+productId).modal('toggle');
    });
})