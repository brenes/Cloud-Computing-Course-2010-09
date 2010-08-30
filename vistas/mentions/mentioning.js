// Map

function(doc) {

	regexp = new RegExp("(^|[^a-zA-Z0-9_\s])[@＠]([a-zA-Z0-9_]{1,20})(?=(.|$))", "g");
	matches = doc.message.match(regexp);
	if (matches)
	{
		for (var i in matches)
		{
			emit(doc.author, matches[i]);
		}
	}	
}

// Reduce

function(keys, values, rereduce) 
{
	return values
	
}