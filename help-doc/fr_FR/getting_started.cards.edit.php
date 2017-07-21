<?php
ob_start();
?>
  Pour éditer une fiche, vous devez tout d'abord la sélectionner en cliquant dessus.<br />
  Ensuite, suivez une des méthodes suivantes&#8239;:
  <ul>
    <li>aller à <span class="menu">Édition &gt; <img src="../images/edit_card.png"> Éditer la fiche…</span>&#8239;;</li>
    <li>ou cliquer sur <span class="button"><img src="../images/edit_card_32.png"> Éditer la fiche…</span>&#8239;;</li>
    <li>ou appuyer sur <span class="key-stroke">Ctrl+E</span>&#8239;;</li>
    <li>ou bien ouvrez la vue détaillée puis cliquez sur <span class="button">Éditer</span></li>
  </ul>
  Vous devriez voir la même fenêtre que lorsque vous avez ajouté une fiche. Une fois la fiche
  éditée, cliquez sur <span class="button">Valider</span> pour appliquer les changements.
  <p>
    La fiche devrait maintemant être mise à jour dans l'Espace de travail (seulement si vous avez
    changé le nom et/ou le genre).
  </p>
  <p class="next-topic">Prochain sujet&#8239;: <a href="getting_started.cards.delete.php">Supprimer une fiche</a></p>
  <h2>En rapport</h2>
  <p><a href="getting_started.cards.details.php">Vue détaillée</a></p>
<?php
$content = ob_get_clean();
$title = 'Éditer une fiche';
$lang = 'fr_FR';
require_once '../template.php';