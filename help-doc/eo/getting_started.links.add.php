<?php
ob_start();
?>
  Por aldoni ligilo, sekvu unu metodon el la tri:
  <ul>
    <li>iru al <span class="menu">Eldoni &gt; <img src="../images/add_link.png"> Aldoni ligilo…</span>;</li>
    <li>aŭ alklaku <span class="button"><img src="../images/add_link_32.png"> Aldoni ligilo…</span>;</li>
    <li>aŭ subtenu <span class="key-stroke">Ctrl+L</span></li>
  </ul>
  Nun, selektu la unuan karton, ke vi volas konekti, post, alklaku la duan.
  Vi vidus tiu fenestro:<br />
  <img class="center" src="../images/add_link_dialog-eo.png"><br />
  Tiu unua markobutono (1) indikas ĉu la rilaton estas edziĝo.<br />
  La du sekvantaj tekstujoj (2) estas la komencodato kaj komencejo de la rilato. La dato povas
  esti nekompleta.<br />
  La markobutono en la zono (3) indikas ĉu la rilato estas finita. Se estas aktiva, la sube
  tekstujo estas aktiva.<br />
  La sube du tekstujoj (4) enhavas la nomojn de la gekunulinoj.<br />
  En la zono (3) vi povas aldoni/forigi infanoj. Por aldoni infano, selektu la elementojn en la
  suba listo kaj alklaku la <img src="../images/arrow_up.png"> butonon. Por forigi infano,
  selektu la elementojn en la supre listo kaj alklaku la <img src="../images/arrow_down.png">
  butonon.
  <p>
    Iam vi finis, alklaku <span class="button">Validigi</span> por krei la ligilo. Se vi alklakis
    tro frue, ne maltrankviliĝu, vi povos eldoni poste.
  </p>
  <p>
    Nun, estus linio inter la du kartoj kaj la infanoj.
  </p>
  <p class="next-topic">Sekva temo: <a href="getting_started.links.edit.php">Eldoni ligilo</a></p>
<?php
$content = ob_get_clean();
$title = 'Aldoni ligilo';
$lang = 'eo';
require_once '../template.php';