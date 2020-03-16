$( document ).ready(function() {
    $.ajax({
        url: '/is-authenticated',
        dataType: 'json',
        success: function( data ) {
            console.log('Authenticated.')
            $.ajax({
                url: '/get-user',
                dataType: 'json',
                success: function( data ) {
                    $("#token").text(JSON.stringify(data.result, null, 2))
                },
                error: function( data ) {
                  console.log("ERROR:  " + JSON.stringify(data));
                  window.location.replace("/");
                }
            });
        },
        error: function( data ) {
          console.log("ERROR:  " + JSON.stringify(data));
          window.location.replace("/");
        }
    });
})