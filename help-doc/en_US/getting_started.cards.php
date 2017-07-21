<?php
ob_start();
?>
  An identity card is a graphical component which represents a person. They can be moved around
  the Workbench with the mouse. The default skins are:
  <ul>
    <li><img src="../images/id_card_man.png">&nbsp;man</li>
    <li><img src="../images/id_card_woman.png">&nbsp;woman</li>
    <li><img src="../images/id_card_unknown.png">&nbsp;unknown gender</li>
  </ul>
  (Colors can be changed in the <span class="menu">Options</span> menu.)
  <p>
    The text shown by a card is the person's first and last names.<br />
    When a card is selected the border changes color (default <span style="color: blue">blue</span>).
    E.g.: <img src="../images/card_selected.png">
  </p>
  <h2>Contents</h2>
  <ul>
    <li><a href="getting_started.cards.details.php">Detailed view</a></li>
    <li><a href="getting_started.cards.add.php">Adding cards</a></li>
    <li><a href="getting_started.cards.edit.php">Editing cards</a></li>
    <li><a href="getting_started.cards.delete.php">Deleting cards</a></li>
  </ul>
  <p class="next-topic">Next topic: <a href="getting_started.links.php">Links</a></p>
  <h2>Related</h2>
  <p><a href="options.php">General Options</a></p>
<?php
$content = ob_get_clean();
$title = 'Identity cards';
require_once '../template.php';