<?php
ob_start();
?>
<ul>
  <li><a href="en_US/index.php">English</a></li>
  <li><a href="fr_FR/index.php">Français</a></li>
  <li><a href="eo/index.php">Esperanto</a></li>
</ul>
<?php
$content = ob_get_clean();
$title = 'Index';
$noMenu = true;
require_once 'template.php';