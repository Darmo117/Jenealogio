<?php
ob_start();
?>
  Une fiche d'identité est un composant graphique qui représente une personne. Il peut être
  déplacé à travers l'Espace de travail avec la souris. Les apparences par défaut sont&#8239;:
  <ul>
    <li><img src="../images/id_card_man.png">&nbsp;homme</li>
    <li><img src="../images/id_card_woman.png">&nbsp;femme</li>
    <li><img src="../images/id_card_unknown.png">&nbsp;genre inconnu</li>
  </ul>
  Les couleurs peuvent être modifiées depuis le menu <span class="menu">Options</span>.
  <p>
    Le texte affiché par les fiches est le nom complet de la personne.<br />
    Lorqu'une carte est sélectionnée, sa bordure change de couleur (<span style="color: blue">bleue</span>
    par défaut).
    Ex.&#8239;: <img src="../images/card_selected.png">
  </p>
  <h2>Contenu</h2>
  <ul>
    <li><a href="getting_started.cards.details.php">Vue détaillée</a></li>
    <li><a href="getting_started.cards.add.php">Ajouter une fiche</a></li>
    <li><a href="getting_started.cards.edit.php">Éditer une fiche</a></li>
    <li><a href="getting_started.cards.delete.php">Supprimer une fiche</a></li>
  </ul>
  <p class="next-topic">Prochain sujet&#8239;: <a href="getting_started.links.php">Liens</a></p>
  <h2>En rapport</h2>
  <p><a href="options.php">Options générales</a></p>
<?php
$content = ob_get_clean();
$title = 'Fiches d\'identité';
$lang = 'fr_FR';
require_once '../template.php';