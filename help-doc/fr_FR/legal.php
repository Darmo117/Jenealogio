<?php
ob_start();
?>
  <p>
    Le contenu ce guide est © Jenealogio 2017.
  </p>
  <h2>Licence</h2>
  <p>
    Permission is granted to copy, distribute and/or modify this
    document under the terms of the GNU Free Documentation License,
    Version 1.3 or any later version published by the Free Software
    Foundation; with no Invariant Sections, no Front-Cover Texts, and
    no Back-Cover Texts.  A copy of the license is included in the
    section entitled “GNU Free Documentation License”.
  </p>
<?php
$content = ob_get_clean();
$title = 'Notice';
$lang = 'fr_FR';
require_once '../template.php';