<?php
session_start();
require("controllers/twitter_authorize.php");
require("controllers/twitter_access.php");
require("controllers/twitter_tweet.php");

// register at http://twitter.com/oauth_clients and fill these two

// Twitter test

if (!isset($_GET["oauth_token"]) && !isset($_SESSION["oauth"]))
{
	try
	{
		$controller = new TwitterAuthorizeController();
		$controller->execute();
	}
	catch(OAuthException2 $e)
	{
		echo "Exception" . $e->getMessage();
	}
}
else
{
	if (!isset($_SESSION["oauth"]))
	{
		$controller = new TwitterAccessController();
		$controller->execute($_GET["oauth_token"]);
	}
	try
	{
		$controller = new TwitterTweetController();
		$tweet = $controller->execute();
	}
	catch(OAuthException2 $e)
	{
		die("An error has happened: " . $e->getMessage());
	}
}
?>
<h1>Twitter Digger</h1>
<div>Felicidades, tu tweet ha sido publicado.</div>