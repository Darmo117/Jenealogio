<?php
ob_start();
?>
  <p>
    Iam la programo estas komenciĝita, la unua afero vi vidas estas blanka spaco. Post vi kreis
    projekton, vi kreos via arbon tie. Vi povas krei novan arbon alklake
    <span class="menu">Dosiero &gt; <img src="../images/new_tree.png"> Nova arbo...</span> aŭ
    alklake <span class="button"><img src="../images/new_tree_32.png"> Nova arbo...</span> aŭ
    subtene <span class="key-stroke">Ctrl+N</span>.
    Vi devos eniri nomon por la arbo. Iam vi faras tio, alklaku <span class="button">Validigi</span>.
  </p>
  <p>
    Post la projekto estas kreita, vi vidus ke, io butonoj estas nun ŝaltitaj. Ni povas komenci
    labori.
  </p>
  <p class="next-topic">Sekva temo: <a href="getting_started.cards.php">Kartoj</a></p>
<?php
$content = ob_get_clean();
$title = 'La Stablo';
$lang = 'eo';
require_once '../template.php';