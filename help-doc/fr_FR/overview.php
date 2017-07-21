<?php
ob_start();
?>
  <p>
    Les sujets suivant fournissent une aide sur l'utilisation de l'Espace de travail et la création
    d'arbres généalogiques. Ils expliquent aussi la personnalisation des options.
  </p>
  <h2>L'Espace de travail</h2>
  <p>
    L'expression Espace de travail désigne l'interface d'édition. Il est constitué de la zone
    d'édition, de la barre d'outils et de la barre de menus en haut de la fenêtre.
  </p>
  <p class="next-topic">Prochain sujet&#8239;: <a href="getting_started.php">Pour commencer…</a></p>
<?php
$content = ob_get_clean();
$title = 'Vue générale';
require_once '../template.php';