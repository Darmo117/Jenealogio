<?php
ob_start();
?>
  To edit a card, you have to select it first by clicking it.<br />
  Now follow one of these methods:
  <ul>
    <li>go to <span class="menu">Edit &gt; <img src="../images/edit_card.png"> Edit Card…</span>;</li>
    <li>or click <span class="button"><img src="../images/edit_card_32.png"> Edit Card…</span>;</li>
    <li>or press <span class="key-stroke">Ctrl+E</span>;</li>
    <li>or open the detailed view, then click <span class="button">Edit</span></li>
  </ul>
  You should be seeing the same dialog as when you added a card. Once you edited the card, click
  <span class="button">Validate</span> to apply changes.
  <p>
    The card should now be updated in the Workbench (only if you changed the name and/or the gender).
  </p>
  <p class="next-topic">Next topic: <a href="getting_started.cards.delete.php">Deleting cards</a></p>
  <h2>Related</h2>
  <p><a href="getting_started.cards.details.php">Detailed view</a></p>
<?php
$content = ob_get_clean();
$title = 'Editing cards';
require_once '../template.php';