<?php
ob_start();
?>
  <p>
    Ligilo representas rilaton inter du personoj. Estas linio, ke konektas du kartojn.
    La defaŭlta koloro estas nigra. Gepatra interrilato estas representata kiel <span style="color: blue">blua</span>
    linio (defaŭlta). Se rilato finitus (eksedziĝo, k.t.p.) la linio estus punktita. Edzeca ligiloj
    estas pli dika.<br />
    Ekzemplo:<br />
    <img src="../images/links_example.png"><br />
    Tie, la nigra linio representas la geedzecon kaj la blua linio konektas al la (sola) infano.
  </p>
  <p>
    Superflugata ligilo estas <span style="color: red">ruĝa</span> (defaŭlta). Selektata ligilo estas
    <span style="color: #00ff00">verda</span> (defaŭlta).<br />
    La koloroj estas ŝanĝeblaj en la <span class="menu">Opcioj</span> menuo.
  </p>
  <p>
    La sekva kvar temoj eksplikas kiel vi povas montri, aldoni, eldoni kaj forigi ligilon.
  </p>
  <h2>Enhavo</h2>
  <ul>
    <li><a href="getting_started.links.details.php">Detala vido</a></li>
    <li><a href="getting_started.links.add.php">Aldoni ligilo</a></li>
    <li><a href="getting_started.links.edit.php">Eldoni ligilo</a></li>
    <li><a href="getting_started.links.delete.php">Forigi ligilo</a></li>
  </ul>
  <h2>Rilatita</h2>
  <p><a href="options.php">Ĝeneralaj opcioj</a></p>
<?php
$content = ob_get_clean();
$title = 'Ligiloj';
$lang = 'eo';
require_once '../template.php';