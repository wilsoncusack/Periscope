$(document).ready ->
  # Loading the landing page
  $("#landingPage").Chevron "render", name: "Slim Shay", (result) ->
    $('body').append(result)
    # Listening to the email address input field
    $("input[name='email_input']").on 'input', -> console.log('heresa')
