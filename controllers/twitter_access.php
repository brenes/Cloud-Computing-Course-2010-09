<?php

require_once("config.php");
require_once("lib/oauth/library/OAuthStore.php");
require_once("lib/oauth/library/OAuthRequester.php");

class TwitterAccessController
{
	public function execute($request_token)
	{
		$options = array('consumer_key' => TWITTER_CONSUMER_KEY, 'consumer_secret' => TWITTER_CONSUMER_SECRET);
		OAuthStore::instance("2Leg", $options);
		
		$request = new OAuthRequester(TWITTER_ACCESS_TOKEN_URL, 'POST');
		$request->setParam("oauth_token", $_GET["oauth_token"]);
		$result = $request->doRequest();
			
		parse_str($result['body'], $params);
		$_SESSION["oauth"] = array();
		$_SESSION["oauth"]["oauth_token"] = $params["oauth_token"];
		$_SESSION["oauth"]["oauth_token_secret"] = $params["oauth_token_secret"];
		$_SESSION["oauth"]["user_id"] = $params["user_id"];
		$_SESSION["oauth"]["screen_name"] = $params["screen_name"];
	}
}