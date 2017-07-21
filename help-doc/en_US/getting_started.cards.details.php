<?php
ob_start();
?>
  You can view detailed information for each card by double-clicking it.<br />
  <img src="../images/card_detailed_view-en_US.png"><br />
  The zones are detailled bellow:
  <ol>
    <li>the image you have selected;</li>
    <li>the person's full name followed by the use name inside parentheses and the other names in italic;</li>
    <li><img src="../images/baby.png">: birth date and location;</li>
    <li>
      <img src="../images/heart.png">: all the relationships this person is/has been in. Each
      relationship follows this format: <em>date (location)[ - end date]</em>, the end date is
      displayed if the relationship has ended and the <img src="../images/heart.png"> will be
      replaced by <img src="../images/heart_broken.png">;
    </li>
    <li>
      <img src="../images/tombstone.png">: death date and location. If the person is dead a
      <img src="../images/tombstone.png"> will appear next to their name;
    </li>
    <li>
      the person's age. If the death date is not known, the age is calculated based on the current
      date; otherwise the death date will be used instead. If the birth date is not known, no age
      will be displayed;
    </li>
    <li>the comment zone.</li>
  </ol>
  <p class="next-topic">Next topic: <a href="getting_started.cards.add.php">Adding cards</a></p>
<?php
$content = ob_get_clean();
$title = 'Detailed view';
require_once '../template.php';