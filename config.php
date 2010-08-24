<?php

define("TWITTER_CONSUMER_KEY", "OcPxDJaPTncYLoTaBd5Lw");
define("TWITTER_CONSUMER_SECRET", "xbVlw0Qp8VXA2FxRmcP7LTL3DxShXNAUbZaSCWSBWA");

define("TWITTER_OAUTH_HOST","https://twitter.com");
define("TWITTER_API_HOST","http://api.twitter.com");
define("TWITTER_REQUEST_TOKEN_URL", TWITTER_OAUTH_HOST . "/oauth/request_token");
define("TWITTER_AUTHORIZE_URL", TWITTER_OAUTH_HOST . "/oauth/authorize");
define("TWITTER_ACCESS_TOKEN_URL", TWITTER_OAUTH_HOST . "/oauth/access_token");
define("TWITTER_PUBLIC_TIMELINE_API", TWITTER_OAUTH_HOST . "/statuses/public_timeline.json");
define("TWITTER_UPDATE_STATUS_API", TWITTER_OAUTH_HOST . "/statuses/update.json");

define('OAUTH_TMP_DIR', function_exists('sys_get_temp_dir') ? sys_get_temp_dir() : realpath($_ENV["TMP"])); 
		
?>