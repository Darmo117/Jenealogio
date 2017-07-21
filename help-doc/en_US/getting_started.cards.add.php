<?php
ob_start();
?>
  There are three ways to add an identity card:
  <ul>
    <li>got to <span class="menu">Edit &gt; <img src="../images/add_card.png"> Add Card…</span>;</li>
    <li>or click <span class="button"><img src="../images/add_card_32.png"> Add Card…</span> in the tool bar;</li>
    <li>or press <span class="key-stroke">Ctrl+A</span></li>
  </ul>
  You should be seeing a form dialog named “Add Card”. This is where you have to enter information
  about the person. You can leave a field empty if you don't have the information. Birth and Death
  fields can be incomplete. You can also select an image.
  <p>
    Once you are finished, click <span class="button">Validate</span> to create the card. If you
    clicked before finishing, don't worry you can come back and edit it later.
  </p>
  <p>
    Now, there should be a card at the top left corner of the Workbench. You can move it where you
    want it to be.
  </p>
  <p class="next-topic">Next topic: <a href="getting_started.cards.edit.php">Editing cards</a></p>
<?php
$content = ob_get_clean();
$title = 'Adding cards';
require_once '../template.php';