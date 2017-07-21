<?php
ob_start();
?>
  Tout d'abord, sélectionnez la fiche à supprimer.<br>
  Une fois de plus, il y plusieus manières de faire&#8239;:
  <ul>
    <li>aller à <span class="menu">Édition &gt; <img src="../images/delete_card.png"> Supprimer une fiche</span>&#8239;;</li>
    <li>ou cliquer sur <span class="button"><img src="../images/delete_card_32.png"> Supprimer une fiche</span>&#8239;;</li>
    <li>ou appuyer sur <span class="key-stroke">Supprimer</span></li>
  </ul>
  Vous allez devoir confirmer l'action. Cliquez sur <span class="button">Oui</span>. La fiche
  devrait avoir disparu de l'Espace de travail, ainsi que ses relations.<br />
  Si la fiche était liée à une autre, le lien est lui ausi supprimé.<br />
  <strong>N.B.&#8239;: une fois la fiche supprimée, elle est définitivement perdue et ne peut plus être récupérée.</strong>
  <h2>En rapport</h2>
  <p><a href="getting_started.links.php">Liens</a></p>
<?php
$content = ob_get_clean();
$title = 'Supprimer une fiche';
require_once '../template.php';