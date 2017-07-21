<?php
ob_start();
?>
  <p>
    La sekvantaj temoj eksplikas kiel utilizi la Stablo por krei genealogiaj arboj. Ili ankaŭ
    eksplikas opcionj de personigo.
  </p>
  <h2>Stablo</h2>
  <p>
    La vorto Stablo designas la ĉefa interfaco. ĝi estas komponata de la zono de redaktado,
    la supre ilobreto kaj la menuobreto al la supro de la fenestro.
  </p>
  <p class="next-topic">Sekva temo: <a href="getting_started.php">Komencante</a></p>
<?php
$content = ob_get_clean();
$title = 'Trarigardo';
$lang = 'eo';
require_once '../template.php';