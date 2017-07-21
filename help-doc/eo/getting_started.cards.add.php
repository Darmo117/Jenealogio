<?php
ob_start();
?>
  Estas tri manieroj por aldoni karto:
  <ul>
    <li>iru al <span class="menu">Eldoni &gt; <img src="../images/add_card.png"> Aldoni karto…</span>;</li>
    <li>aŭ alklaku <span class="button"><img src="../images/add_card_32.png"> Aldoni karto…</span> en la ilobreto;</li>
    <li>aŭ subtenu <span class="key-stroke">Ctrl+A</span></li>
  </ul>
  Vi vidus fenestron nomitan “Aldoni karto”. Estas tie ke vi devas eniri la informojn pri la persono.
  Vi povas lasi tekstujon vakuan se vi ne havas la informon. Naskiĝo kaj morto tekstujoj povas
  esti nekompleta. Vi ankaŭ povas selekti bildo.
  <p>
    Iam vi finis, alklaku <span class="button">Validigi</span> por krei la karto. Se vi alklakis
    tro frue, ne maltrankviliĝu, vi povos eldoni poste.
  </p>
  <p>
    Nun, estus nova karto supre maldekstre en la Stablo. Vi povas movi ĝin kie vi volas.
  </p>
  <p class="next-topic">Sekva karto: <a href="getting_started.cards.edit.php">Eldoni karto</a></p>
<?php
$content = ob_get_clean();
$title = 'Aldoni karto';
$lang = 'eo';
require_once '../template.php';