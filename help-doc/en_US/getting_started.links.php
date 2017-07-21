<?php
ob_start();
?>
  <p>
    A link is the graphical representation of the relationship between two people. It is a line that
    connects two cards. The default color is black. A <span style="color: blue">blue</span> line (default)
    represents a parental link. If a relation has ended (divorce, etc.) the line will be dashed.
    Marriage bonds have a wider line.<br />
    Example:<br />
    <img src="../images/links_example.png"><br />
    Here, the black line represents the marriage and the blue line connects to the
    (only) child the couple has.
  </p>
  <p>
    When a link is hovered by the mouse, it becomes <span style="color: red">red</span> by default.
    A selected link will appear <span style="color: #00ff00">green</span> (default color).<br />
    Colors can be changed in the <span class="menu">Options</span> menu.
  </p>
  <p>
    The next four topics will explain how to show, add, edit or delete links.
  </p>
  <h2>Contents</h2>
  <ul>
    <li><a href="getting_started.links.details.php">Detailed view</a></li>
    <li><a href="getting_started.links.add.php">Adding links</a></li>
    <li><a href="getting_started.links.edit.php">Editing links</a></li>
    <li><a href="getting_started.links.delete.php">Deleting links</a></li>
  </ul>
  <h2>Related</h2>
  <p><a href="options.php">General Options</a></p>
<?php
$content = ob_get_clean();
$title = 'Links';
require_once '../template.php';