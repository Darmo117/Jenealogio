<?php
ob_start();
?>
  <p>
    Un lien est la représentation d'une relation. Il est représenté par une ligne reliant deux
    fiches. La couleur par défaut est noire. Une ligne <span style="color: blue">bleue</span> (défaut)
    représente un lien de parenté. Si une relation a été rompue (divorce, etc.) le trait est en
    pointillés. Les liens de mariage ont une ligne plus épaisse.<br>
    Exemple&#8239;:<br>
    <img src="../images/links_example.png"><br>
    Ici, la ligne noire représente le lien de mariage et la ligne bleue est connectée au seul
    enfant du couple.
  </p>
  <p>
    Lorsqu'un lien est survolé avec la souris, il devient <span style="color: red">rouge</span> par défaut.
    Un lien sélectionné apparaîtra <span style="color: #00ff00">vert</span> (couleur par défaut).<br>
    Les couleurs peuvent être modifiées dans le menu <span class="menu">Options</span>.
  </p>
  <p>
    Les quatre prochains sujets expliquent comment visualiser, ajouter, éditer et supprimer un lien.
  </p>
  <h2>Contenu</h2>
  <ul>
    <li><a href="getting_started.links.details.php">Vue détaillée</a></li>
    <li><a href="getting_started.links.add.php">Ajouter un lien</a></li>
    <li><a href="getting_started.links.edit.php">Éditer un lien</a></li>
    <li><a href="getting_started.links.delete.php">Supprimer un lien</a></li>
  </ul>
  <h2>En rapport</h2>
  <p><a href="options.php">Options générales</a></p>
<?php
$content = ob_get_clean();
$title = 'Liens';
$lang = 'fr_FR';
require_once '../template.php';