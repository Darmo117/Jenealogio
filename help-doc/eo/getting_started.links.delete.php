<?php
ob_start();
?>
  Unue, selektu la ligilo ke vi volas forigi.<br>
  Denove, estas tri manieroj:
  <ul>
    <li>iru al <span class="menu">Eldoni &gt; <img src="../images/delete_link.png"> Forigi ligilo</span>;</li>
    <li>aŭ alklaku <span class="button"><img src="../images/delete_link_32.png"> Forigi ligilo</span>;</li>
    <li>aŭ subtenu <span class="key-stroke">Forigi</span></li>
  </ul>
  Vi devus konfirmi la ago. Alklaku <span class="button">Jes</span>. La ligilo estus forigita el la
  Stablo.<br>
  <b>N.B.: iam la ligilo estas forigita, ĝi estas definitive perdita.</b>
<?php
$content = ob_get_clean();
$title = 'Forigi ligilo';
$lang = 'eo';
require_once '../template.php';