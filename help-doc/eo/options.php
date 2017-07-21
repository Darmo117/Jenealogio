<?php
ob_start();
?>
  <p>
    Vi povas personigi opciojn irante al <span class="menu">Opcioj</span> menuo. Tie, vi povas
    ŝanĝi la <a href="options.languages.php">lingvon</a> aŭ la <a href="options.colors.php">kolorojn</a>.<br />
    Por iuj opcioj, vi devas rekomenciĝi la programon.
  </p>
  <h2>Enhavo</h2>
  <ul>
    <li><a href="options.languages.php">Lingvoj</a></li>
    <li><a href="options.colors.php">Koloroj</a></li>
  </ul>
<?php
$content = ob_get_clean();
$title = 'Ĝeneralaj opcioj';
$lang = 'eo';
require_once '../template.php';