<?php
ob_start();
?>
  Vous pouvez accéder aux informations détaillées d'une fiche en double-cliquant dessus.<br />
  <img src="../images/card_detailed_view-fr_FR.png"><br />
  Les zones sont détaillées ci-dessous&#8239;:
  <ol>
    <li>l'image sélectionnée&#8239;;</li>
    <li>
      le nom complet de la personne suivit du nom d'usage entre parenthèses et des autres
      prénoms en italique&#8239;;
    </li>
    <li><img src="../images/baby.png">: la date et le lieu de naissance&#8239;;</li>
    <li>
      <img src="../images/heart.png">: toutes les relations de la personne passées ou actuelles.
      Chaque relation suit le format suivant&#8239;: <em>date (lieu)[ - date de rupture]</em>, la
      date de rupture n'est affichée que si la relation a été rompue (divorce, etc.), le
      <img src="../images/heart.png"> sera aussi remplacé par <img src="../images/heart_broken.png">&#8239;;
    </li>
    <li>
      <img src="../images/tombstone.png">: la date et le lieu de décès. Si la personne est décédée,
      une <img src="../images/tombstone.png"> sera affichée à côté de son nom&#8239;;
    </li>
    <li>
      l'âge de la personne. Si la date de décès est inconnue, l'âge est calculé à partir de la
      date actuelle&#8239;; dans le cas contraire, la date de décès est utilisée. Si la date de
      naissance est inconnue, l'âge ne sera pas affiché&#8239;;
    </li>
    <li>la zone de commentaire.</li>
  </ol>
  <p class="next-topic">Prochain sujet&#8239;: <a href="getting_started.cards.add.php">Ajouter une fiche</a></p>
<?php
$content = ob_get_clean();
$title = 'Vue détaillée';
$lang = 'fr_FR';
require_once '../template.php';