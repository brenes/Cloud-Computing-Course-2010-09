<?php 
require ('controllers/authors.php');
$controller =new AuthorsController(); 
$authors = $controller->execute();
?>

<h1>Twitter Digger</h1>

<form action="search.php" method="get">
	<input type="text" name="q" value="<?php echo $_GET["q"]?>" />
</form>

<div><?php echo $authors->total_rows ?> usuarios han sido encontrados por Twitter Diggest</div>
<ul>
<?php 
foreach ($authors->rows as $author)
{
?>
	<li>
		<a href="author.php?name=<?php echo $author->key; ?>"><?php echo $author->key; ?></a>: <?php echo $author->value; ?> tweets
	</li>
<?php } ?>	
</ul>