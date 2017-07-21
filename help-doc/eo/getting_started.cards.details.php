<?php
ob_start();
?>
  Vi povas vidi detalajn informojn por ĉiu karto duobla-klakante.<br />
  <img src="../images/card_detailed_view-eo.png"><br />
  La zonoj estas detalitaj ĉi tiu:
  <ol>
    <li>la selektita bildo;</li>
    <li>
      la kompleta nomo de la persono sekvita de la uzanta nomo inter parentezoj kaj la
      aliaj nomoj en kursiva;
    </li>
    <li><img src="../images/baby.png">: la naskiĝodato kaj la naskiĝejo;</li>
    <li>
      <img src="../images/heart.png">: ĉio rilatoj de la persono. La rilatoj sekvas tiun formaton:
      <em>dato (ejo)[ - finodato]</em>, la finodato nur estas montrata se la rilato estas
      finita (eksedziĝo, k.t.p.), la <img src="../images/heart.png"> estas anstataŭiĝota por
      <img src="../images/heart_broken.png">;
    </li>
    <li>
      <img src="../images/tombstone.png">: la mortodato kaj la mortejo. Se la persono estas
      mortita, <img src="../images/tombstone.png"> aperas ĉe ŝia/lia nomo;
    </li>
    <li>
      la aĝo de la persono. Se la mortodato estas nekonata, la aĝo estas komputita el la nuna dato;
      alie la mortodato anstataŭ estas uzita. Se la naskiĝodato estas nekonata, la aĝo ne aperigas;
    </li>
    <li>la komenta zono.</li>
  </ol>
  <p class="next-topic">Sekva temo: <a href="getting_started.cards.add.php">Aldoni karto</a></p>
<?php
$content = ob_get_clean();
$title = 'Detala vido';
$lang = 'eo';
require_once '../template.php';