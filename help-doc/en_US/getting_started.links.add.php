<?php
ob_start();
?>
  To add a link, follow one of these methods:
  <ul>
    <li>go to <span class="menu">Edit &gt; <img src="../images/add_link.png"> Add Link…</span>;</li>
    <li>or click <span class="button"><img src="../images/add_link_32.png"> Add Link…</span>;</li>
    <li>or press <span class="key-stroke">Ctrl+L</span></li>
  </ul>
  Now, select the first card you want to connect, then click the second one.
  You should now be seeing this form:<br />
  <img class="center" src="../images/add_link_dialog-en_US.png"><br />
  This first checkbox (1) lets you indicate if the relationship is a wedding.<br />
  The following two fields (2) are the start date and location of the relationship. The date can
  be incomplete.<br />
  The checkbox in zone (3) is used to tell if the relation has ended. If it is checked, the date
  field bellow will be enabled.<br />
  The two fields below (4) contain the names of the couple.<br />
  Zone (5) lets you add/remove children from the relationship. To add children, select the items
  from the bottom list then click the <img src="../images/arrow_up.png"> button. To remove added
  children, select the items in the top list then click the <img src="../images/arrow_down.png">
  button. You can search for specific people with the searchbar at the bottom.
  <p>
    Once you're finished, click <span class="button">Validate</span> to create the link. If you
    clicked before finishing, don't worry you can come back and edit it later.
  </p>
  <p>
    Now, there should be lines between the two cards and the children.
  </p>
  <p class="next-topic">Next topic: <a href="getting_started.links.edit.php">Editing links</a></p>
<?php
$content = ob_get_clean();
$title = 'Adding links';
require_once '../template.php';