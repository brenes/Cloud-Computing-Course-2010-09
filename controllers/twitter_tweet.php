<?php

require_once("config.php");
require_once("lib/oauth/library/OAuthStore.php");
require_once("lib/oauth/library/OAuthRequester.php");

class TwitterTweetController
{
	public function execute()
	{
		
		$options = array('consumer_key' => TWITTER_CONSUMER_KEY, 'consumer_secret' => TWITTER_CONSUMER_SECRET);
		OAuthStore::instance("2Leg", $options);
		
		$request = new OAuthRequester(TWITTER_UPDATE_STATUS_API, 'POST');
		$request->setParam("oauth_consumer_key", TWITTER_CONSUMER_KEY);
		$request->setParam("oauth_token", $_SESSION["oauth"]["oauth_token"]);
		$request->setParam("status", "probando a las ".date("Y-m-d H:i:s"));

		$secrets = array(
		"token" => $_SESSION["oauth"]["oauth_token"], 
		"token_secret" => $_SESSION["oauth"]["oauth_token_secret"],
		"signature_methods" => array("HMAC-SHA1"), 
		"consumer_key" => TWITTER_CONSUMER_KEY,
		"consumer_secret" => TWITTER_CONSUMER_SECRET

		);

		$result = $request->doRequest($_SESSION["oauth"]["user_id"], array(), array("secrets" => $secrets));


		return $result;
	}
}