$(document).ready ->
	topicDict = {}

	# get the topics
	$.get "/topics", (data) -> 
		#fill dict
		for topic in data
			topicDict[topic.id] = topic

		$(".topics").Chevron "render", topics: data, (result) =>
			$('body').append(result)
			$('.topic-wrapper').on('click', (e) -> 
				openCloseTopic(e))
			$('.image-description').hover(((e) -> showDesc(e)), -> $(".description").remove())

	openCloseTopic = (e) ->
		id = e.currentTarget.id
		inner = $("##{ id }>.inner ")
		if inner.css('display') is "none"
			$("##{ id }>.topic-header ").css('border-bottom', '3px solid lightgrey')
			$("##{ id }>.inner ").show()
			$("##{ id }>.inner ").animate({
				height: "50%"
				}, 700)
			$("##{ id }").addClass('active')
		else
			$("##{ id }>.topic-header ").css('border-bottom', 'none')
			$("##{ id }>.inner ").hide()
			$("##{ id }").removeClass('active')

	showDesc = (e) ->
		id = e.currentTarget.id
		tId = id.split("desc")[1]
		description = topicDict[tId].description
		$("##{ id }").append("<div class='description'></div>")
		$(".description").text(description)
		offset = $(e.currentTarget).offset()
		$(".description").css('left', offset.left + "px")
		$(".description").css('top', offset.top + "px")
		$(".description").css('width', $("##{ id }").width() + "px")
		console.log $("##{ id }").width() 
		$(".description").css('height', $("##{ id }").height() + "px")
