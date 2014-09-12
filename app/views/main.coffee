$(document).ready ->
  # Loading the landing page
  $(".landingPage").Chevron("render", name: "Slim Shay", (result) =>
    $('body').append(result)
    # Listening to the email address input field
    $("#email_button").on 'click', =>
      email = $("input[name='email_input']").val()
      $('.landing-wrapper').remove()
      $('body').append("<div class='signed-up'> You'll here from us soon! </div>")
    $("input[name='email_input']").on 'input', -> console.log('heresa'))


  0


