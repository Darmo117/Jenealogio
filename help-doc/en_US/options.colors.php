<?php
ob_start();
?>
  <p>
    This menu lets you change the colors of most of the components. You can access it by going to
    <span class="menu">Options &gt; <img src="../images/color_wheel.png"> Colors</span>.
  </p>
  <p>
    You should see a dialog with a tree and a colored button. To change a color, select the desired
    option in the tree then click the button to choose new color. Once you are finished, click
    <span class="button">Validate</span> to apply changes. You don't need to restart the app as
    changes are applied immediatly.
  </p>
<?php
$content = ob_get_clean();
$title = 'Colors';
require_once '../template.php';