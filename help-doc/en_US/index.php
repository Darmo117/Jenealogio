<?php
ob_start();
?>
  <p>
    Browse topics in the tree on the left. Click on a topic to have it displayed.<br />
  </p>
  <h2>Legend</h2>
  <p>
    Menus and items are written in bold and italic (<span class="menu">Menu</span>).<br />
    Buttons are written in bold only (<span class="button">Button</span>).<br />
    Keystrokes are underlined (<span class="key-stroke">Stroke</span>).
  </p>
<?php
$content = ob_get_clean();
$title = 'Using the help system';
require_once '../template.php';