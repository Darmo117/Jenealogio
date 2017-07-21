<?php
ob_start();
?>
  Karto de identigo estas komponanton ke, representas personon. Ili estas movebla kun la muso.
  La defaŭltaj aspektoj estas:
  <ul>
    <li><img src="../images/id_card_man.png">&nbsp;viro</li>
    <li><img src="../images/id_card_woman.png">&nbsp;virino</li>
    <li><img src="../images/id_card_unknown.png">&nbsp;nekonata sekso</li>
  </ul>
  La koloroj estas ŝanĝeblaj en la <span class="menu">Opcioj</span> menuo.
  <p>
    La videbla teksto en karto estas la kompleta nomo de la persono.<br />
    Selektita karto havas <span style="color: blue">blua</span> bordo (defaŭlta).
    Ekzemplo: <img src="../images/card_selected.png">
  </p>
  <h2>Enhavo</h2>
  <ul>
    <li><a href="getting_started.cards.details.php">Detala vido</a></li>
    <li><a href="getting_started.cards.add.php">Aldoni karto</a></li>
    <li><a href="getting_started.cards.edit.php">Eldoni karto</a></li>
    <li><a href="getting_started.cards.delete.php">Forigi karto</a></li>
  </ul>
  <p>Sekva temo: <a href="getting_started.links.php">Ligiloj</a></p>
  <h2>Rilatita</h2>
  <p><a href="options.php">Ĝeneralaj opcioj</a></p>
<?php
$content = ob_get_clean();
$title = 'Kartoj de identigo';
$lang = 'eo';
require_once '../template.php';