<?php
ob_start();
?>
  To edit a link, you have to select it first by clicking it.<br />
  Now follow one of these methods:
  <ul>
    <li>go to <span class="menu">Edit &gt; <img src="../images/edit_link.png"> Edit Link…</span>;</li>
    <li>or click <span class="button"><img src="../images/edit_link_32.png"> Edit Link…</span>;</li>
    <li>or press <span class="key-stroke">Ctrl+E</span></li>
  </ul>
  You should be seeing the same dialog as when you added a link. Once you edited the link, click
  <span class="button">Validate</span> to apply changes.
  <p>
    The link should now be updated in the Workbench.
  </p>
  <p class="next-topic">Next topic: <a href="getting_started.links.delete.php">Deleting links</a></p>
<?php
$content = ob_get_clean();
$title = 'Editing links';
require_once '../template.php';