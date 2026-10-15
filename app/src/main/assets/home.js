// Carousel foto bergulir: swipe + auto-scroll pelan
(function () {
  const track = document.getElementById("track");
  const cards = Array.from(track.children);
  const N = cards.length;

  const STEP = 120;        // jarak antar kartu (px)
  const ANGLE = 28;        // sudut putar per kartu (derajat)
  const DEPTH = 90;        // seberapa jauh kartu samping "mundur" (px)

  let pos = 0;             // posisi float (0 = kartu pertama di tengah)
  let dragging = false, startX = 0, startPos = 0, auto;

  function render() {
    cards.forEach((card, i) => {
      // jarak tiap kartu dari tengah, dibungkus supaya muter (infinite)
      let d = ((i - pos) % N + N) % N;
      if (d > N / 2) d -= N;

      const abs = Math.abs(d);
      const x = d * STEP;
      const rotY = -d * ANGLE;
      const z = -abs * DEPTH;
      const scale = 1 - Math.min(abs * 0.08, 0.4);
      const opacity = abs > 2.6 ? 0 : 1 - abs * 0.12;

      card.style.transform =
        `translateX(${x}px) translateZ(${z}px) rotateY(${rotY}deg) scale(${scale})`;
      card.style.opacity = opacity;
      card.style.zIndex = 100 - Math.round(abs * 10);
    });
  }

  // ---- gesture swipe ----
  track.addEventListener("pointerdown", (e) => {
    dragging = true;
    startX = e.clientX;
    startPos = pos;
    track.classList.add("dragging");
    track.setPointerCapture(e.pointerId);
    stopAuto();
  });

  track.addEventListener("pointermove", (e) => {
    if (!dragging) return;
    pos = startPos - (e.clientX - startX) / STEP;
    render();
  });

  function end() {
    if (!dragging) return;
    dragging = false;
    track.classList.remove("dragging");
    pos = Math.round(pos);   // snap ke kartu terdekat
    render();
    startAuto();
  }
  track.addEventListener("pointerup", end);
  track.addEventListener("pointercancel", end);

  // ---- auto scroll ----
  function startAuto() {
    stopAuto();
    auto = setInterval(() => { pos += 1; render(); }, 2800);
  }
  function stopAuto() { clearInterval(auto); }

  render();
  startAuto();
})();