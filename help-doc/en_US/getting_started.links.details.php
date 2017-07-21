<?php
ob_start();
?>
  You can view detailed information for each link by double-clicking it.<br />
  <img src="../images/link_detailed_view-en_US.png"><br />
  The zones are detailled bellow:
  <ol>
    <li>tells if the relationship is a wedding;</li>
    <li>full names of the two partners;</li>
    <li>start date and location of the relationship;</li>
    <li><img src="../images/heart_broken.png">: if the relation has ended, the date when it ended;</li>
    <li><img src="../images/baby.png">: list of all children;</li>
  </ol>
  <p class="next-topic">Next topic: <a href="getting_started.links.add.php">Adding links</a></p>
<?php
$content = ob_get_clean();
$title = 'Detailed view';
require_once '../template.php';