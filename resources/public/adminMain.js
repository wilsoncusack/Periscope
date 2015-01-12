// Generated by CoffeeScript 1.8.0
$(document).ready(function() {
  var addArticle, articleDict, colorScore, count, next, removeArticle, renderSearchRes, search, toPublish;
  $('input').on('keypress', function(e) {
    if (e.keyCode === 13) {
      return search($('#searchBox').val());
    }
  });
  $('#searchButton').on('click', function() {
    return search($('#searchBox').val());
  });
  articleDict = {};
  toPublish = [];
  count = 0;
  search = function(searchWord) {
    var toSend;
    if (searchWord !== "") {
      if (searchWord.indexOf("\"") !== -1) {
        toSend = searchWord.replace(/"/g, '');
      } else {
        toSend = searchWord.split(" ");
      }
      return $.get("/articlesSearch", {
        words: toSend
      }, function(data) {
        return renderSearchRes(data);
      });
    }
  };
  renderSearchRes = function(data) {
    var article, counter, _i, _len;
    counter = 0;
    for (_i = 0, _len = data.length; _i < _len; _i++) {
      article = data[_i];
      article.id = counter;
      articleDict[counter] = article;
      counter += 1;
    }
    return $(".articles").Chevron("render", {
      articles: data
    }, (function(_this) {
      return function(results) {
        var score, _j, _len1, _ref, _results;
        $('#searchResults').prepend(results);
        $('.articleChooser').on('click', function(e) {
          return addArticle(e);
        });
        _ref = $('.polScore');
        _results = [];
        for (_j = 0, _len1 = _ref.length; _j < _len1; _j++) {
          score = _ref[_j];
          _results.push(colorScore($(score)));
        }
        return _results;
      };
    })(this));
  };
  colorScore = function(div) {
    var score;
    console.log(div);
    score = div.text();
    if (score > 0) {
      return div.css('background-color', 'rgb(' + score + ',0,0)');
    } else if (score < 0) {
      return div.css('background-color', 'rgb(0,0,' + score + ')');
    } else {
      return div.css('background-color', 'lightgrey');
    }
  };
  addArticle = function(e) {
    var article, div, id;
    if (e.target.tagName === "A") {
      return;
    }
    if (count === 6) {
      alert("You already have six articles");
      return;
    }
    div = $(e.currentTarget)[0];
    id = parseInt(div.id);
    article = articleDict[id];
    toPublish.push(article);
    $(div).addClass('selected');
    $(div).removeClass('notSelected');
    $("#" + id).off();
    $("#" + id).on('click', function(e) {
      return removeArticle(e);
    });
    count += 1;
    $('#publish').addClass('enabled');
    return $('#publish').removeClass('disabled');
  };
  removeArticle = function(e) {
    var article, div, i, id;
    if (e.target.tagName === "A") {
      return;
    }
    div = $(e.currentTarget)[0];
    id = parseInt(div.id);
    article = articleDict[id];
    $(div).removeClass('selected');
    $(div).addClass('notSelected');
    $("#" + id).off();
    $("#" + id).on('click', function(e) {
      return addArticle(e);
    });
    i = 0;
    while (i < toPublish.length) {
      if (toPublish[i] === article) {
        toPublish.splice(i, 1);
      }
      i++;
    }
    count -= 1;
    if (count < 1) {
      $('#publish').addClass('disabled');
      return $('#publish').removeClass('enabled');
    }
  };
  return next = function() {
    return $('notSelected').remove();
  };
});