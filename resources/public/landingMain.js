// Generated by CoffeeScript 1.8.0
$(document).ready(function() {
  var addEmail, alreadyAdded, renderMain, success;
  $(".landingPage").Chevron("render", {
    name: "Slim Shay"
  }, (function(_this) {
    return function(result) {
      $('body').append(result);
      $("#email_address").on('keypress', function(e) {
        var email;
        if (e.keyCode !== 13) {
          return;
        }
        email = $("input[name='email_input']").val();
        return addEmail(email);
      });
      return $("#email_button").on('click', function() {
        var email;
        email = $("input[name='email_input']").val();
        return addEmail(email);
      });
    };
  })(this));
  addEmail = function(emailAddress) {
    var toSend;
    if (emailAddress.indexOf("@") !== -1 && emailAddress.indexOf(".") !== -1 && emailAddress.length < 40) {
      $('.error').remove();
      toSend = {
        email: emailAddress
      };
      return $.post("/insertEmail", toSend, function(data) {
        if (data.status === 1) {
          return success();
        } else if (data.status === 0) {
          return alreadyAdded();
        } else {
          return $('.landing-wrapper').append("<span class='error'> Please enter a valid email address </span>");
        }
      });
    } else {
      if ($('.error').length === 0) {
        return $('.landing-wrapper').append("<span class='error'> Please enter a valid email address </span>");
      }
    }
  };
  renderMain = function() {
    $('.landing-wrapper').remove();
    $('.blue').remove();
    return $(".home").Chevron("render", {
      articles: "articles"
    }, (function(_this) {
      return function(results) {
        return $('body').append(results);
      };
    })(this));
  };
  alreadyAdded = function() {
    $('.landing-wrapper').remove();
    return $('body').append("<div class='already-signed-up'> Looks like you already signed up! \n We appreciate the enthusiasm and will get back to you soon! </div>");
  };
  return success = function() {
    $('.landing-wrapper').remove();
    return $('body').append("<div class='signed-up'> You'll hear from us soon! </div>");
  };
});
