<?php
ob_start();
?>
  <p>
    Vous pouvez personnaliser les options en allant dans le menu <span class="menu">Options</span>.
    Ici, vous pouvez changer la <a href="options.languages.php">langue</a> ou les
    <a href="options.colors.php">couleurs</a>.<br />
    Certaines options pourraient nécessiter le redémarrage de l'application pour être prises en compte.
  </p>
  <h2>Contenu</h2>
  <ul>
    <li><a href="options.languages.php">Langues</a></li>
    <li><a href="options.colors.php">Couleurs</a></li>
  </ul>
<?php
$content = ob_get_clean();
$title = 'Options générales';
$lang = 'fr_FR';
require_once '../template.php';