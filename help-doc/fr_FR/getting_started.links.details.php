<?php
ob_start();
?>
  Vous pouvez visualiser les informations détaillées d'un lien en double-cliquant dessus.<br />
  <img src="../images/link_detailed_view-fr_FR.png"><br />
  Les zones sont détaillées ci-dessus&#8239;:
  <ol>
    <li>indique si la relation est un mariage&#8239;;</li>
    <li>le nom complet des deux conjoints&#8239;;</li>
    <li>la date et le lieu du début de la relation&#8239;;</li>
    <li><img src="../images/heart_broken.png">&#8239;: si la relation a été rompue, la date de rupture&#8239;;</li>
    <li><img src="../images/baby.png">&#8239;: la liste de tout les enfants&#8239;;</li>
  </ol>
  <p class="next-topic">Prochain sujet&#8239;: <a href="getting_started.links.add.php">Ajouter un lien</a></p>
<?php
$content = ob_get_clean();
$title = 'Vue détaillée';
require_once '../template.php';