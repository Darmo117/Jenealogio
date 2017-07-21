<?php
ob_start();
?>
  You can access this menu by going to <span class="menu">Options &gt; Language</span>.
  You can choose between available languages.<br />
  These are:
  <ul>
    <li>English</li>
    <li>French</li>
    <li>Esperanto</li>
  </ul>
  After you choose the language, you have to restart the app so the changes can be applied.
  <p class="next-topic">Next topic: <a href="options.colors.php">Colors</a></p>
<?php
$content = ob_get_clean();
$title = 'Languages';
require_once '../template.php';