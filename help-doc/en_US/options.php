<?php
ob_start();
?>
  <p>
    You can customize options by going to <span class="menu">Options</span>. There, you can change
    the <a href="options.languages.php">language</a> or the <a href="options.colors.php">colors</a>.<br />
    Some options may need the app to be restarted to be applied.
  </p>
  <h2>Contents</h2>
  <ul>
    <li><a href="options.languages.php">Languages</a></li>
    <li><a href="options.colors.php">Colors</a></li>
  </ul>
<?php
$content = ob_get_clean();
$title = 'General options';
require_once '../template.php';