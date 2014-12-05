$(document).ready ->
  # Loading the landing page
  $(".landingPage").Chevron("render", name: "Slim Shay", (result) =>
    $('body').append(result)
    $("#logo").on 'click', => renderMain()
    # Listening to the email address input field
    $("#email_button").on 'click', =>
      email = $("input[name='email_input']").val()
      addEmail(email))


  addEmail = (email) ->
      $('.landing-wrapper').remove()

      $.ajax
        url: '/insertEmail',
        type: 'POST',
        data: {email: email},
        dataType: 'json',
        contentType: "application/json",
        success: (data) -> console.log JSON.stringify(data)

      $('body').append("<div class='signed-up'> You'll here from us soon! </div>")

  renderMain = ->
    $('.landing-wrapper').remove()
    $('.blue').remove()
    # articles = getArticles();
    $(".home").Chevron("render", articles: "articles", (results) =>
      $('body').append(results));
