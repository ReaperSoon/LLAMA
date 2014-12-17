$(function(){
    var total = 0.;
    $(".sum").each(function(){
        total += parseFloat($(this).text());
    });
    $("#billTotal").text("$"+total);

    $(document).delegate(".remove-from-cart",'click', function(){
        if(typeof $("#billTotal")[0] !== 'undefined'){
            setTimeout(function(){
                location.reload();
            }, 200);
        }
    })

});