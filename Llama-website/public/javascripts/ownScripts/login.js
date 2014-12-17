function setFormUrl() {
    setTimeout(function(){
        $("#url").val(location.href);
    }, 200);
}

$(function() {
    $("#editAccount").click(function(){
        $("#modal").load("/editAccount").modal("toggle");
    });
    $(document).delegate("#edit-account-form","submit", function(){
        $.ajax({
            url: "/doCreateAccount",
            method: "post",
            data: $(this).serialize(),
            success: function() {
                $("#modal").modal("toggle");
            }
        });
    });

    $(".show-create-account").on('click',function(){
        $("#modal").load("/createAccount").modal('toggle');
        setFormUrl();
    });
    $(".show-login").on('click',function(){
        $("#modal").load("/showLogin").modal('toggle');
        setFormUrl();
        $("#login-form").submit(function() {
            $.ajax({
                url: '/login',
                method: 'post',
                data: $("#login-form").serialize(),
                success: function(data) {
                    if (typeof data.error === 'undefined')
                        location.reload();
                    $("#modal-alert-message").text(data.error);
                    $("#modal-alert").slideDown();
                }
            });
        });
    });

})