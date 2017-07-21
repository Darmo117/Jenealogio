/**
 * Splitter from: https://codepen.io/cnupm99/pen/pvNyYX
 */

splitter = (function (obj) {
  let splitter, cont1, cont2;
  let last_x, window_width;
  let min_width;

  obj.init = function (minWidth) {
    min_width = minWidth;
    window_width = window.innerWidth;
    splitter = document.getElementById("splitter");
    cont1 = document.getElementById("split-left");
    cont2 = document.getElementById("split-right");
    let dx = cont1.clientWidth;
    splitter.style.marginLeft = dx + "px";
    dx += splitter.clientWidth;
    cont2.style.marginLeft = dx + "px";
    dx = window_width - dx;
    cont2.style.width = dx + "px";
    splitter.addEventListener("mousedown", spMouseDown);
  };

  function spMouseDown(e) {
    splitter.removeEventListener("mousedown", spMouseDown);
    window.addEventListener("mousemove", spMouseMove);
    window.addEventListener("mouseup", spMouseUp);
    last_x = e.clientX;
  }

  function spMouseUp() {
    window.removeEventListener("mousemove", spMouseMove);
    window.removeEventListener("mouseup", spMouseUp);
    splitter.addEventListener("mousedown", spMouseDown);
    resetPosition(last_x);
  }

  function spMouseMove(e) {
    resetPosition(e.clientX);
  }

  function resetPosition(nowX) {
    let dx = nowX - last_x;
    dx += cont1.clientWidth;
    if (dx < min_width)
      dx = min_width;
    cont1.style.width = dx + "px";
    splitter.style.marginLeft = dx + "px";
    dx += splitter.clientWidth;
    cont2.style.marginLeft = dx + "px";
    dx = window_width - dx;
    cont2.style.width = dx + "px";
    last_x = nowX;
  }

  return obj;
})({});