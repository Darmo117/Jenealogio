<?php
ob_start();
?>
  Tout d'abord, sélectionnez le lien à supprimer.<br />
  Une fois de plus, il y a plusieurs manières de faire&#8239;:
  <ul>
    <li>aller à <span class="menu">Édition &gt; <img src="../images/delete_link.png"> Supprimer un lien</span>&#8239;;</li>
    <li>ou cliquer sur <span class="button"><img src="../images/delete_link_32.png"> Supprimer un lien</span>&#8239;;</li>
    <li>ou appuyer sur <span class="key-stroke">Supprimer</span></li>
  </ul>
  Il devrait vous être demandé de confirmer l'action. Cliquez sur <span class="button">Oui</span>.
  Le lien devrait avoir disparu de l'Espace de travail.<br />
  <strong>N.B.&#8239;: une fois le lien supprimé, il est définitivement perdu et ne pourra pas être retrouvé.</strong>
<?php
$content = ob_get_clean();
$title = 'Supprimer un lien';
$lang = 'fr_FR';
require_once '../template.php';