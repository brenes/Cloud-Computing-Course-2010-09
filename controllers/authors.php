<?php

require_once ("lib/php-on-couch/couch.php");
require_once 'lib/php-on-couch/couchClient.php';

class AuthorsController
{

	public function execute()
	{
		$client = new couchClient ('http://localhost:5984','tweets');
		
		return $client->skip(100)->group(true)->getView("authors", "all");
		
	}
}