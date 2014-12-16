var smallRatingOpts = {
              size:"xs",
              showCaption:false,
              showClear:false
            }

$(function(){
    $(document).delegate(".llama-picture",'click',function(){
        var productId = $(this).data("product-id");
        $("#modal").load("/bigPicture/"+productId).modal("toggle");
    })
    $(".llama-picture").hover(function(){
        $(this).css("cursor", "pointer");
    }, function(){
        $(this).css("cursor", "default");
    });

    $(".close").click(function(){
        $(this).parent().slideUp();
    });

    $(document).delegate(".number-input", 'keypress', function(evt) {
        var charCode = (evt.which) ? evt.which : evt.keyCode
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            evt.stopPropagation();
            return false;
        }
        return true;
    });

    $(".small-rating").rating(smallRatingOpts);
	
	$("#salutlol").click(function(){
		$("#contentlol").html("<iframe height='800' width='800' src='http://192.168.0.1:8080/LamaClient/searchid'></iframe>");
	});
})
