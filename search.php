<?php 
require ('controllers/search.php');
$controller =new SearchController(); 
$tweets = $controller->execute($_GET["q"]);
?>

<h1>Twitter Digger</h1>

<form action="search.php" method="get">
	<input type="text" name="q" value="<?php echo $_GET["q"]?>" />
</form>

<ul>
<?php 
foreach ($tweets as $tweet)
{
?>
	<li>
	<div></div><img src="<?php echo $tweet->user->avatar_url; ?>" alt="<?php echo $tweet->user->login; ?>"/> <?php echo $tweet->user->login; ?>: <?php echo $tweet->text; ?></div>
	<div><?php echo $tweet->created_at->format('Y-m-d H:i'); ?></div>
	<a href="/retweet.php">Retweet</a>
	</li>
<?php } ?>	
</ul>