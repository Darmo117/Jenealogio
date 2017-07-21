<?php
ob_start();
?>
  <p>
    Ce chapitre fournit des informations à propos de l'utilisation de l'Espace de travail.
  </p>
  <h2>Contenu</h2>
  <ul>
    <li><a href="getting_started.workbench.php">L'Espace de travail</a></li>
    <li><a href="getting_started.cards.php">Fiches d'identité</a></li>
    <li><a href="getting_started.links.php">Liens</a></li>
  </ul>
  <p class="next-topic">Prochain sujet&#8239;: <a href="options.php">Options générales</a></p>
<?php
$content = ob_get_clean();
$title = 'Pour commencer…';
require_once '../template.php';