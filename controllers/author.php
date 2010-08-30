<?php

require_once ("lib/php-on-couch/couch.php");
require_once 'lib/php-on-couch/couchClient.php';

class AuthorController
{

	public function execute($name)
	{
		$client = new couchClient ('http://localhost:5984','tweets');
		
		return $client->key($name)->group(true)->getView("authors", "tweets");
		
	}
}