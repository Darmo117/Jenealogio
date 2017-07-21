<?php
ob_start();
?>
  First, select the card you want to delete.<br />
  Once again, there are three ways:
  <ul>
    <li>go to <span class="menu">Edit &gt; <img src="../images/delete_card.png"> Delete Card</span>;</li>
    <li>or click <span class="button"><img src="../images/delete_card_32.png"> Delete Card</span>;</li>
    <li>or press <span class="key-stroke">Delete</span></li>
  </ul>
  You should be asked to confirm the action. Click <span class="button">Yes</span>. The card
  should be removed from the Workbench.<br />
  If the card was linked to another, as are all its relations.<br />
  <strong>N.B: once a card is removed, it is lost forever and cannot be retrieved.</strong>
  <h2>Related</h2>
  <p><a href="getting_started.links.php">Links</a></p>
<?php
$content = ob_get_clean();
$title = 'Deleting cards';
require_once '../template.php';