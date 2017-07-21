<?php
ob_start();
?>
  <p>
    Tiu ĉapitro provizas informojn pri la uzo de la Stablo.
  </p>
  <h2>Enhavo</h2>
  <ul>
    <li><a href="getting_started.workbench.php">La Stablo</a></li>
    <li><a href="getting_started.cards.php">Kartoj de identigo</a></li>
    <li><a href="getting_started.links.php">Ligiloj</a></li>
  </ul>
  <p class="next-topic">Sekva temo: <a href="options.php">Ĝeneralaj opcioj</a></p>
<?php
$content = ob_get_clean();
$title = 'Komencante';
$lang = 'eo';
require_once '../template.php';