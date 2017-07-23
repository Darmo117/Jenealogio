<?php
ob_start();
?>
<p>Choose a language:</p>
<ul>
  <li><a href="en_US/">English</a></li>
  <li><a href="fr_FR/">Français</a></li>
  <li><a href="eo/">Esperanto</a></li>
</ul>
<?php
$content = ob_get_clean();
$title = 'Index';
$noMenu = true;
require_once 'template.php';