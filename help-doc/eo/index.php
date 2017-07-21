<?php
ob_start();
?>
  <p>
    Tralegi la temoj en la arbo maldekstron. Alklaku temo por aperigi ĝin.
  </p>
  <h2>Legendo</h2>
  <p>
    Menuoj kaj elementoj estas grasaj kaj kursivaj (<span class="menu">Menuo</span>).<br />
    Butonoj estas grasaj (<span class="button">Butono</span>).<br />
    Klavofrapoj estas substrekitaj (<span class="key-stroke">Frapo</span>).
  </p>
<?php
$content = ob_get_clean();
$title = 'Utilizante la helpo';
$lang = 'eo';
require_once '../template.php';