<?php 

require ("model/tweet.php");
require_once ("lib/php-on-couch/couch.php");
require_once 'lib/php-on-couch/couchClient.php';
require_once 'lib/php-on-couch/couchDocument.php';

class SearchController
{
	public function execute($query)
	{
		$tweets = array();
		
		// create a new cURL resource
		$ch = curl_init();
		
		curl_setopt($ch, CURLOPT_URL, "http://search.twitter.com/search.json?rpp=100&result_type=recent&q=".$query);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		// curl_setopt($ch, CURLOPT_PROXY, "http://proxy.uniovi.es:8888");
		
		// grab URL and pass it to the browser
		$tweets_json = curl_exec($ch);
		$tweets_json = json_decode($tweets_json);
		
		// close cURL resource, and free up system resources
		curl_close($ch);


		$client = new couchClient ('http://localhost:5984','tweets');

		foreach ($tweets_json->results as $tweet_json)
		{
			$tweet = new Tweet();
			$tweet->text = $tweet_json->text;
			$tweet->source = $tweet_json->source;
			$tweet->created_at = new DateTime($tweet_json->created_at);
			
			$tweet->user = new User();
			$tweet->user->login = $tweet_json->from_user;
			$tweet->user->avatar_url = $tweet_json->profile_image_url;
			
			$tweets[] = $tweet;

			// No we try to store the tweet in our couch database
			try {
				$tweet_doc = $client->getDoc($tweet_json->id."");
			}catch ( Exception $e ) {
				$tweet_doc = new couchDocument($client);
				$tweet_doc->set( array('_id'=>$tweet_json->id."",'author'=> $tweet_json->from_user,'message'=>$tweet_json->text, "client" => $tweet_json->source, "created_at" => $tweet_json->created_at ));
			}
		}

		return $tweets;
	}
}

?>
