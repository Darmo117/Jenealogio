<?php
ob_start();
?>
  First, select the link you want to delete.<br />
  Once again, there are three ways:
  <ul>
    <li>go to <span class="menu">Edit &gt; <img src="../images/delete_link.png"> Delete Link</span>;</li>
    <li>or click <span class="button"><img src="../images/delete_link_32.png"> Delete Link</span>;</li>
    <li>or press <span class="key-stroke">Delete</span></li>
  </ul>
  You should be asked to confirm the action. Click <span class="button">Yes</span>. The link
  should be removed from the Workbench.<br />
  <strong>N.B.: once a link is removed, it is lost forever and cannot be retreived.</strong>
<?php
$content = ob_get_clean();
$title = 'Deleting links';
require_once '../template.php';