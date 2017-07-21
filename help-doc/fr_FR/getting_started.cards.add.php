<?php
ob_start();
?>
  Il y a trois manière d'ajouter une fiche&#8239;:
  <ul>
    <li>en allant sur <span class="menu">Édition &gt; <img src="../images/add_card.png"> Ajouter un fiche…</span>&#8239;;</li>
    <li>
      en cliquant <span class="button"><img src="../images/add_card_32.png"> Ajouter une fiche…</span>
      dans la barre d'outils&#8239;;
    </li>
    <li>en appuyant sur <span class="key-stroke">Ctrl+A</span></li>
  </ul>
  Vous devriez voir un formulaire dans une fenêtre appelée «&#8239;Ajouter une fiche&#8239;». C'est
  ici que vous allez entrer les informations sur la personne. Vous pouvez laisser un champ vide si
  vous n'avez pas l'information. Les dates de naissance et de décès peuvent être incomplètes. Vous
  pouvez aussi choisir une image.
  <p>
    Une fois que vous avez terminé, cliquez sur <span class="button">Valider</span> pour créer la
    fiche. Si vous avez cliqué sans avoir terminé, pas de panique, vous pourrez toujours modifier
    plus tard.
  </p>
  <p>
    Maintenant, une fiche devrait être apparue dans le coin en haut à gauche de l'Espace de travail.
    Vous pouvez la déplacer où vous voulez.
  </p>
  <p class="next-topic">Prochain sujet&#8239;: <a href="getting_started.cards.edit.php">Éditer une fiche</a></p>
<?php
$content = ob_get_clean();
$title = 'Ajouter une fiche';
$lang = 'fr_FR';
require_once '../template.php';