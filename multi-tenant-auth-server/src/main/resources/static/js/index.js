$( document ).ready(function() {
    $.ajax({
        url: '/is-authenticated',
        dataType: 'json',
        success: function( data ) {
            //console.log("SUCCESS: " + JSON.stringify(data));
            //console.log(data.httpStatusCode)
          if (data.httpStatusCode === 200) {
            window.location.replace("/home.html");
          }
        },
        error: function( data ) {
          console.log("ERROR:  " + JSON.stringify(data));
        }
    });
})