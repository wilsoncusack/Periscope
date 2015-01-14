$(document).ready ->

	$('input').on('keypress', (e) -> 
		search($('#searchBox').val()) if e.keyCode == 13
	)
	$('#searchButton').on('click', -> search($('#searchBox').val()))
	$("#next").on('click', -> next() if toPublish.length > 0)

	articleDict = {}
	toPublish = []
	count = 0

	# method called when someone hits enter in search box
	# if searchWord (String) is surrounded with quotes, the string is not modified
	# otherwise, the string is split on spaces and the array is passed on
	search = (searchWord) ->
		if searchWord isnt ""
			if searchWord.indexOf("\"") isnt -1
				toSend = searchWord.replace(/"/g, '')
			else 
				toSend = searchWord.split(" ")

			$.get("/articlesSearch", {words: toSend}, (data) -> renderSearchRes(data))

	# renderSearchRes: array of JSON article objects -> renders them on the page
	# I/P: data, an array of JSON article objects
	# O/P: renders the articles on the page, sets up action listners
	# 	and adjusts coloring for scores
	renderSearchRes = (data) -> 
		counter = 0
		for article in data
			article.id = counter
			articleDict[counter] = article
			counter+=1
		$(".articles").Chevron("render", articles: data, (results) =>
			$('#searchResults').prepend(results)
			$('.articleChooser').on('click', (e) -> addArticle(e))

			# change color
			for score in $('.polScore')
				colorScore($(score)))


	# adjusts the color to match the score
	# blue is negative, red is positive
	# score is jQuery object of a polScore div 
	colorScore = (div) -> 
		console.log div
		score = div.text()
		if score > 0
			div.css('background-color', 'rgb(' + score + ',0,0)')
		else if score < 0
			div.css('background-color', 'rgb(0,0,' + score + ')')
		else 
			div.css('background-color', 'lightgrey')

	# adds the article to the array of articles to publish
	# highlights box on page
	# e is the event of a click on the dom element
	addArticle = (e) -> 
		return if e.target.tagName is "A" # don't change color if link is click
		if count is 6
			alert "You already have six articles"
			return
		div = $(e.currentTarget)[0]
		id = parseInt(div.id)
		article = articleDict[id]
		toPublish.push(article)
		# change color 
		$(div).addClass('selected')
		$(div).removeClass('notSelected')

		$("#" + id).off()
		$("#" + id).on('click', (e) -> removeArticle(e))
		count += 1
		$('#next').addClass('enabled');
		$('#next').removeClass('disabled');

	# removes the article from the array of articles to publish 
	# unhighlights on page
	# e is event of a click on the box
	removeArticle = (e) -> 
		return if e.target.tagName is "A"
		div = $(e.currentTarget)[0]
		id = parseInt(div.id)
		article = articleDict[id]
		# change color
		$(div).removeClass('selected')
		$(div).addClass('notSelected')

		$("#" + id).off()
		$("#" + id).on('click', (e) -> addArticle(e))
		i = 0
		while i < toPublish.length
			if toPublish[i] is article 
				toPublish.splice(i, 1)
			i++
		count -= 1
		if count < 1
			$('#next').addClass('disabled');
			$('#next').removeClass('enabled');

	next = ->
		$('#suggested').remove();
		$('#searchDiv').remove();
		$('#searchResults').remove();

		$(".toPublish").Chevron("render", articles: toPublish, (results) =>
			$('body').append(results))


