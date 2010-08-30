// Map

function(doc)
{
  emit(doc.author, doc.message);
}

// Reduce

function(keys, values, rereduce) 
{
	return values
	
}