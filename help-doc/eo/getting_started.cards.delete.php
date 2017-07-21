<?php
ob_start();
?>
  Unue, selektu la karto ke, vi volas forigi.<br />
  Denove, estas tri manieroj:
  <ul>
    <li>iru al <span class="menu">Eldoni &gt; <img src="../images/delete_card.png"> Forigi karto</span>;</li>
    <li>aŭ alklaku <span class="button"><img src="../images/delete_card_32.png"> Forigi karto</span>;</li>
    <li>aŭ subtenu <span class="key-stroke">Forigi</span></li>
  </ul>
  Vi devus konfirmi la ago. Alklaku <span class="button">Jes</span>. La karto kaj sia rilatoj
  devus esti forigita el la Stablo.<br />
  <strong>N.B.: iam la karto estas forigita, ĝi eterne estas perdita.</strong>
  <h2>Rilatita</h2>
  <p><a href="getting_started.links.php">Ligiloj</a></p>
<?php
$content = ob_get_clean();
$title = 'Forigi karto';
$lang = 'eo';
require_once '../template.php';