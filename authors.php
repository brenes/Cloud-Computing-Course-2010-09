<?php 
require ('controllers/authors.php');
$controller =new AuthorsController(); 
$authors = $controller->execute();
?>

<h1>Twitter Digger</h1>

<form action="search.php" method="get">
	<input type="text" name="q" value="<?php echo $_GET["q"]?>" />
</form>

<?php var_dump($authors) ?>
<div><?php echo $authors->total_rows ?> usuarios han sido encontrados por Twitter Diggest</div>
<ul>
<?php 
foreach ($authors->rows as $author)
{
?>
	<li>
		<?php echo $author->key; ?>: <?php echo $author->value; ?> tweets
	</li>
<?php } ?>	
</ul>