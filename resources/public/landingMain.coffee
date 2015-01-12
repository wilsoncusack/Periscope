$(document).ready ->
  # Loading the landing page
  $(".landingPage").Chevron("render", name: "Slim Shay", (result) =>
    $('body').append(result)
    $("#logo").on 'click', => renderMain()
    # Listening to the email address input field
    $("#email_address").on 'keypress', (e) -> 
      return if e.keyCode isnt 13
      email = $("input[name='email_input']").val()
      addEmail(email)
    $("#email_button").on 'click', =>
      email = $("input[name='email_input']").val()
      addEmail(email))


  addEmail = (emailAddress) ->
    if emailAddress.indexOf("@") != -1 and emailAddress.indexOf(".") != -1 and emailAddress.length < 40
      $('.error').remove();
      toSend = {email: emailAddress}
      $.post("/insertEmail", toSend, 
        (data) -> 
          if data.status == 1
            success()
          else if data.status == 0
            alreadyAdded()
          else 
            # server is checking for basically the same bad input, incase they make requests manually
            $('.landing-wrapper').append("<span class='error'> Please enter a valid email address </span>"))
    else 
      if $('.error').length is 0
        $('.landing-wrapper').append("<span class='error'> Please enter a valid email address </span>")
      

  renderMain = ->
    $('.landing-wrapper').remove()
    $('.blue').remove()
    # articles = getArticles();
    $(".home").Chevron("render", articles: "articles", (results) =>
      $('body').append(results));

  alreadyAdded = -> 
    $('.landing-wrapper').remove()
    $('body').append("<div class='already-signed-up'> Looks like you already signed up! \n
                      We appreciate the enthusiasm and will get back to you soon!
                      </div>")
  

  success = ->
    $('.landing-wrapper').remove()
    $('body').append("<div class='signed-up'> You'll hear from us soon! </div>")
