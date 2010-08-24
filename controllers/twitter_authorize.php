<?php

require_once("config.php");
require_once("lib/oauth/library/OAuthStore.php");
require_once("lib/oauth/library/OAuthRequester.php");

class TwitterAuthorizeController
{

	public function execute()
	{
		
		$options = array('consumer_key' => TWITTER_CONSUMER_KEY, 'consumer_secret' => TWITTER_CONSUMER_SECRET);
		OAuthStore::instance("2Leg", $options);

		// Obtain a request token
		$request = new OAuthRequester(TWITTER_REQUEST_TOKEN_URL, "POST");
		$result = $request->doRequest(0);

		parse_str($result['body'], $params);
		
		// Now redirect the user to the authorization page so we can request an access token
		header("Location: ".TWITTER_AUTHORIZE_URL."?oauth_token=".$params["oauth_token"]);
	}
}