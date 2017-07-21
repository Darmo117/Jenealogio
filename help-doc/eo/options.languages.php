<?php
ob_start();
?>
  Vi povas atingi la menuon tra <span class="menu">Opcioj &gt; Lingvo</span>.
  Vi povas elekti inter la haveblajn lingvojn.<br />
  Tiuj estas:
  <ul>
    <li>Anglo</li>
    <li>Franco</li>
    <li>Esperanto</li>
  </ul>
  Post vi elektas la lingvon, vi devas rekomenciĝi la programo.
  <p class="next-topic">Sekva temo: <a href="options.colors.php">Koloroj</a></p>
<?php
$content = ob_get_clean();
$title = 'Lingvoj';
$lang = 'eo';
require_once '../template.php';