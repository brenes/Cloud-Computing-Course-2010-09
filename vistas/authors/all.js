// Map

function(doc)
{
  emit(doc["author"], 1);
}

// Reduce

function(keys, values, rereduce) 
{
	  return values.length;  
}