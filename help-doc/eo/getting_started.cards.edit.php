<?php
ob_start();
?>
  Por eldoni karto, vi devas selekti ĝin alklakante.<br />
  Tiam, sekvi unu metodo:
  <ul>
    <li>iri al <span class="menu">Eldoni &gt; <img src="../images/edit_card.png"> Eldoni karto…</span>;</li>
    <li>aŭ alklaku <span class="button"><img src="../images/edit_card_32.png"> Eldoni karto…</span>;</li>
    <li>aŭ subtenu <span class="key-stroke">Ctrl+E</span>;</li>
    <li>aŭ malfermu la detalan vidon, kaj alklaku <span class="button">Eldoni</span></li>
  </ul>
  Vi vidos la sama fenestro ke kiam vi aldonis karto. Iam vi eldonis la karto, alklaku
  <span class="button">Validigi</span> por apliki la ŝanĝoj.
  <p>
    La karto devus esti ĝisdatigitan en la Stablo (nur vi ŝanĝis la nomon kaj/aŭ
    la sekson).
  </p>
  <p class="next-topic">Sekva temo: <a href="getting_started.cards.delete.php">Forigi karto</a></p>
  <h2>Rilatita</h2>
  <p><a href="getting_started.cards.details.php">Detala vido</a></p>
<?php
$content = ob_get_clean();
$title = 'Eldoni karto';
$lang = 'eo';
require_once '../template.php';