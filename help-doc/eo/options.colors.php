<?php
ob_start();
?>
  <p>
    Vi povas ŝanĝi la kolorojn de preskaŭ ĉiuj la komponantoj tra tiu menuo.
    La menuo estas atingita irante tra <span class="menu">Opcioj &gt;
    <img src="../images/color_wheel.png"> Koloroj</span>.
  </p>
  <p>
    Vi vidus fenestron kun arbo kaj kolorita butono. Por ŝanĝi koloro, selektu la volitan
    opcion en la arbo, tiam alklaku la butonon por elekti la nova kolor. Alklaku
    <span class="button">Validigi</span> por apliki la ŝanĝoj. Vi ne bezonas rekomenciĝi
    la programon.
  </p>
<?php
$content = ob_get_clean();
$title = 'Koloroj';
$lang = 'eo';
require_once '../template.php';