<?php
ob_start();
?>
  <p>
    This chapter provides information about how to use the Workbench.
  </p>
  <h2>Contents</h2>
  <ul>
    <li><a href="getting_started.workbench.php">The Workbench</a></li>
    <li><a href="getting_started.cards.php">Identity cards</a></li>
    <li><a href="getting_started.links.php">Links</a></li>
  </ul>
  <p class="next-topic">Next topic: <a href="options.php">General options</a></p>
<?php
$content = ob_get_clean();
$title = 'Getting started';
require_once '../template.php';