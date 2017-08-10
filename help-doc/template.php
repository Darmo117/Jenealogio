<?php
$localhost = [
    '127.0.0.1',
    '::1',
    'localhost'
];
if (in_array($_SERVER['REMOTE_ADDR'], $localhost))
  define('ROOT', '');
else
  define('ROOT', '/products/jenealogio/help-doc');

if (!isset($noMenu) || !is_bool($noMenu)) {
  $noMenu = false;
}
if (!isset($title)) {
  $title = 'Jenealogio doc';
}
if (!isset($content)) {
  $content = null;
}
if (!isset($lang)) {
  $lang = 'en_US';
}

if (!$noMenu) {
  $bundle = loadLangFile($lang);
}
?>
  <!DOCTYPE html>

  <!--suppress HtmlUnknownTarget -->
  <html>
  <head>
    <title><?= $title ?></title>
    <meta charset="UTF-8" />
    <link rel="stylesheet" type="text/css" href="<?= ROOT ?>/style.css" />
    <link rel="stylesheet" type="text/css" href="<?= ROOT ?>/treeview.css" />
    <script language="JavaScript" src="<?= ROOT ?>/splitter.js"></script>
    <script>
      function collapseAll() {
        let tree = document.getElementById("treeview");
        let buttons = tree.getElementsByTagName("input");
        for (let i = 0; i < buttons.length; i++) {
          buttons[i].checked = false;
        }
      }
    </script>
  </head>
  <body onload="splitter.init(200)">
    <nav id="split-left">
      <div style="padding: 5px">
        <?php if (!$noMenu): ?>
          <div class="top-bar">
            <button title="<?= /** @noinspection PhpUndefinedVariableInspection */
            $bundle['button.collapse_all'] ?>" onclick="collapseAll()"><img src="<?= ROOT ?>/images/collapse.png"></button>
          </div>
          <div id="treeview" class="css-treeview">
            <ul>
              <li><a href="overview.php"><?= $bundle['tree.overview'] ?></a></li>
              <li><input type="checkbox" id="item-0" /><label for="item-0"><a href="getting_started.php"><?= $bundle['tree.getting_started'] ?></a></label>
                <ul>
                  <li><a href="getting_started.workbench.php"><?= $bundle['tree.getting_started.workbench'] ?></a></li>
                  <li><input type="checkbox" id="item-0-0" /><label for="item-0-0"><a href="getting_started.cards.php"><?= $bundle['tree.getting_started.cards'] ?></a></label>
                    <ul>
                      <li><a href="getting_started.cards.details.php"><?= $bundle['tree.getting_started.cards.details'] ?></a></li>
                      <li><a href="getting_started.cards.add.php"><?= $bundle['tree.getting_started.cards.add'] ?></a></li>
                      <li><a href="getting_started.cards.edit.php"><?= $bundle['tree.getting_started.cards.edit'] ?></a></li>
                      <li><a href="getting_started.cards.delete.php"><?= $bundle['tree.getting_started.cards.delete'] ?></a></li>
                    </ul>
                  </li>
                  <li><input type="checkbox" id="item-0-1" /><label for="item-0-1"><a href="getting_started.links.php"><?= $bundle['tree.getting_started.links'] ?></a></label>
                    <ul>
                      <li><a href="getting_started.links.details.php"><?= $bundle['tree.getting_started.links.details'] ?></a></li>
                      <li><a href="getting_started.links.add.php"><?= $bundle['tree.getting_started.links.add'] ?></a></li>
                      <li><a href="getting_started.links.edit.php"><?= $bundle['tree.getting_started.links.edit'] ?></a></li>
                      <li><a href="getting_started.links.delete.php"><?= $bundle['tree.getting_started.links.delete'] ?></a></li>
                    </ul>
                  </li>
                </ul>
              </li>
              <li><input type="checkbox" id="item-1" /><label for="item-1"><a href="options.php"><?= $bundle['tree.general_options'] ?></a></label>
                <ul>
                  <li><a href="options.languages.php"><?= $bundle['tree.general_options.languages'] ?></a></li>
                  <li><a href="options.colors.php"><?= $bundle['tree.general_options.colors'] ?></a></li>
                </ul>
              </li>
              <li><a href="legal.php"><?= $bundle['tree.legal'] ?></a></li>
            </ul>
          </div>
        <?php endif; ?>
      </div>
    </nav>
    <div id="splitter"></div>
    <main id="split-right">
      <div style="padding: 5px">
        <?php if (!$noMenu): ?>
          <div class="top-bar">
            <button title="<?= /** @noinspection PhpUndefinedVariableInspection */
            $bundle['button.home'] ?>" onclick="location.href='.'"><img src="<?= ROOT ?>/images/home.png"></button>
          </div>
        <?php endif; ?>
        <h1 id="main-title"><?= $title ?></h1>
        <?= $content ?>
      </div>
    </main>
  </body>
  </html>
<?php
/**
 * Loads the given lang file.
 *
 * @param string $lang language code
 *
 * @return array resources array
 */
function loadLangFile(string $lang): array {
  $raw = explode("\n", file_get_contents($_SERVER['DOCUMENT_ROOT'] . ROOT . '/lang/' . $lang . '.lang'));
  $array = [];

  foreach ($raw as $line) {
    if (preg_match('/(\w+(?:\.\w+)*)=(.*)/', $line, $matches)) {
      $array[$matches[1]] = $matches[2];
    }
  }

  return $array;
}