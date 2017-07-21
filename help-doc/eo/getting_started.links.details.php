<?php
ob_start();
?>
  Vi povas vidi la detalajn informojn pri ligilo duobla-klakante.<br />
  <img src="../images/link_detailed_view-eo.png"><br />
  La zonoj estas detalaj ĉi sube:
  <ol>
    <li>tiu montras ĉu la rilaton estas edziĝo;</li>
    <li>la kompleta nomo de la du gekunulinoj;</li>
    <li>la komencodato kaj ejo de la rilato;</li>
    <li><img src="../images/heart_broken.png">: se la rilato estas finita, la finodato;</li>
    <li><img src="../images/baby.png">: la listo de la infanoj;</li>
  </ol>
  <p class="next-topic">Sekva temo: <a href="getting_started.links.add.php">Aldoni ligilo</a></p>
<?php
$content = ob_get_clean();
$title = 'Detala vido';
$lang = 'eo';
require_once '../template.php';