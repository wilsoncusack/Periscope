// Generated by CoffeeScript 1.8.0
$(document).ready(function() {
  var openCloseTopic, showDesc, topicDict;
  topicDict = {};
  $.get("/topics", function(data) {
    var topic, _i, _len;
    for (_i = 0, _len = data.length; _i < _len; _i++) {
      topic = data[_i];
      topicDict[topic.id] = topic;
    }
    return $(".topics").Chevron("render", {
      topics: data
    }, (function(_this) {
      return function(result) {
        $('body').append(result);
        $('.topic-wrapper').on('click', function(e) {
          return openCloseTopic(e);
        });
        return $('.image-description').hover((function(e) {
          return showDesc(e);
        }), function() {
          return $(".description").remove();
        });
      };
    })(this));
  });
  openCloseTopic = function(e) {
    var id, inner;
    id = e.currentTarget.id;
    inner = $("#" + id + ">.inner ");
    if (inner.css('display') === "none") {
      $("#" + id + ">.inner ").show();
      return $("#" + id).addClass('active');
    } else {
      $("#" + id + ">.inner ").hide();
      return $("#" + id).removeClass('active');
    }
  };
  return showDesc = function(e) {
    var description, id, offset, tId;
    id = e.currentTarget.id;
    tId = id.split("desc")[1];
    description = topicDict[tId].description;
    $("#" + id).append("<div class='description'></div>");
    $(".description").text(description);
    offset = $(e.currentTarget).offset();
    $(".description").css('left', offset.left + "px");
    $(".description").css('top', offset.top + "px");
    $(".description").css('width', $("#" + id).width() + "px");
    console.log($("#" + id).width());
    return $(".description").css('height', $("#" + id).height() + "px");
  };
});
