<?php
ob_start();
?>
  <p>
    Parcourez les sujets depuis l'aborescence à gauche. Cliquez sur un sujet pour l'afficher.
  </p>
  <h2>Légende</h2>
  <p>
    Les menus et items sont écrits en gras et en italique (<span class="menu">Menu</span>).<br />
    Les boutons sont écrits en gras (<span class="button">Button</span>).<br />
    Les combinaisons de touches sont soulignées (<span class="key-stroke">Combinaison</span>).
  </p>
<?php
$content = ob_get_clean();
$title = 'Utilisation de l\'aide';
$lang = 'fr_FR';
require_once '../template.php';