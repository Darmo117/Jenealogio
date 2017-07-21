<?php
ob_start();
?>
  <p>
    When the Workbench is launched, the first thing you see is a blank space. This is where you will
    edit your family tree after you have created a project. You can create a new tree by selecting
    <span class="menu">File &gt; <img src="../images/new_tree.png"> New Tree…</span> or clicking
    <span class="button"><img src="../images/new_tree_32.png"> New Tree…</span> or by pressing
    <span class="key-stroke">Ctrl+N</span>.
    You will be prompted to enter a name for the tree. Once you've done that, click
    <span class="button">Validate</span>.
  </p>
  <p>
    After the project is created, you should be seeing that some buttons are now enabled. We can
    start working from now on.
  </p>
  <p class="next-topic">Next topic: <a href="getting_started.cards.php">Cards</a></p>
<?php
$content = ob_get_clean();
$title = 'The Workbench';
require_once '../template.php';