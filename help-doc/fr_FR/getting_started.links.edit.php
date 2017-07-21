<?php
ob_start();
?>
  Pour éditer un lien, vous devez d'abord le sélectionner en cliquant dessus.<br />
  Ensuite, suivez une de ces méthodes&#8239;:
  <ul>
    <li>aller à <span class="menu">Édition &gt; <img src="../images/edit_link.png"> Éditer un lien…</span>&#8239;;</li>
    <li>ou cliquer sur <span class="button"><img src="../images/edit_link_32.png"> Éditer un lien…</span>&#8239;;</li>
    <li>ou appuyer sur <span class="key-stroke">Ctrl+E</span></li>
  </ul>
  Vous devriez voir la même fenêtre que lorsque vous avez ajouté un lien. Une fois que vous avez
  éditer le lien, cliquez sur <span class="button">Valider</span> pour appliquer les modifications.
  <p>
    Le lien devrait être mis à jour dans l'Espace de travail.
  </p>
  <p class="next-topic">Prochain sujet&#8239;: <a href="getting_started.links.delete.php">Supprimer un lien</a></p>
<?php
$content = ob_get_clean();
$title = 'Éditer un lien';
require_once '../template.php';