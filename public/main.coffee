$(document).ready ->
  # Loading the landing page
  $(".landingPage").Chevron("render", name: "Slim Shay", (result) =>
    $('body').append(result)
    $("#logo").on 'click', -> @render()
    # Listening to the email address input field
    $("#email_button").on 'click', =>
      email = $("input[name='email_input']").val()
      $('.landing-wrapper').remove()
      $.ajax
        url: '/insertEmail',
        type: 'POST',
        data: {email: email},
        dataType: 'json',
        contentType: "application/json",
        success: (data) -> console.log JSON.stringify(data)
      $('body').append("<div class='signed-up'> You'll here from us soon! </div>"))


  render = ->
    console.log 'here'
