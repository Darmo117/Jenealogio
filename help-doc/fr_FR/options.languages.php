<?php
ob_start();
?>
  Vous pouvez accéder à ce menu en allant dans <span class="menu">Options &gt; Langues</span>.
  Vous pouvez choisir entre les différentes langues disponibles.<br>
  Celles-ci sont&#8239;:
  <ul>
    <li>Anglais</li>
    <li>Français</li>
    <li>Espéranto</li>
  </ul>
  Après avoir choisi la langue, vous devrez redémarrer l'application pour que les changements
  soient appliqués.
  <p class="next-topic">Prochain sujet&#8239;: <a href="options.colors.php">Couleurs</a></p>
<?php
$content = ob_get_clean();
$title = 'Langues';
require_once '../template.php';