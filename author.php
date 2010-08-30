<?php
require ('controllers/author.php');
$controller =new AuthorController();
$authors = $controller->execute($_GET["name"]);
$author = $authors->rows[0];
?>

<h1>Twitter Digger</h1>

<form action="search.php" method="get"><input type="text" name="q"
	value="<?php echo $_GET["q"]?>" /></form>

<h2><?php echo $author->key; ?></h2>
<div>
<p>Escribió:</p>
	<ul>
	<?php foreach($author->value as $tweet)	{ ?>
		<li><?php echo $tweet; ?></li>
		<?php } ?>
	</ul>
</div>