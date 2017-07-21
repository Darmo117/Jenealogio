<?php
ob_start();
?>
  Por eldoni ligilo, vi devas selekti ĝin alklakante.<br />
  Tiam, sekvi unu metodo:
  <ul>
    <li>iru al <span class="menu">Eldoni &gt; <img src="../images/edit_link.png"> Eldoni ligilo…</span>;</li>
    <li>aŭ alklaku <span class="button"><img src="../images/edit_link_32.png"> Eldoni ligilo…</span>;</li>
    <li>aŭ subtenu <span class="key-stroke">Ctrl+E</span></li>
  </ul>
  Vi vidus la sama fenesetro ke kiam vi aldonis ligilo. Iam vi eldonis la ligilo, alklaku
  <span class="button">Validigi</span> por apliki la ŝanĝoj.
  <p>
    La ligilo estus ĝisdatigita en la Stablo.
  </p>
  <p class="next-topic">Sekva temo: <a href="getting_started.links.delete.php">Forigi ligilo</a></p>
<?php
$content = ob_get_clean();
$title = 'Eldoni ligilo';
$lang = 'eo';
require_once '../template.php';