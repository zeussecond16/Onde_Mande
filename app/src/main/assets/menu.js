// ====== DATA MENU — ganti nama, harga, dan foto sesuai kebutuhan ======
const MENU = [
  { id: 1,  name: "Ayam Gulai",  price: 15000, img: "img/naspad ayam gulai.webp" },
  { id: 2,  name: "Ayam Pop",        price: 14000, img: "img/naspad ayam pop.webp" },
  { id: 3,  name: "Cumi Terong",      price: 20000, img: "img/naspad cumi terong.webp" },
  { id: 4,  name: "Dendeng Lambok",  price: 20000, img: "img/naspad dendeng lambok.webp" },
  { id: 5,  name: "Gulai Otak",      price: 15000, img: "img/naspad gulai otak.webp" },
  { id: 6,  name: "Gulai Tunjang",     price: 15000, img: "img/naspad gulai tunjang.webp" },
  { id: 7,  name: "Ikan Bakar",    price: 17000, img: "img/naspad ikan kembung bakar.webp" },
  { id: 8,  name: "Cabe Ijo",    price: 12000, img: "img/naspad kikil cabe hijau.webp" },
  { id: 9,  name: "Paruh Goreng",       price: 15000, img: "img/naspad paruh goreng.webp" },
  { id: 10, name: "Telur Balado",    price: 13000,  img: "img/naspad telur balado.webp" },
];

const qty = {};                      // { id: jumlah }
MENU.forEach(m => (qty[m.id] = 0));

const rupiah = n => "Rp " + n.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");

const listEl  = document.getElementById("menuList");
const barEl   = document.getElementById("cartBar");
const countEl = document.getElementById("cartCount");
const totalEl = document.getElementById("cartTotal");

// ---- bikin card ----
listEl.innerHTML = MENU.map(m => `
  <article class="item" data-id="${m.id}">
    <img src="${m.img}" alt="${m.name}" />
    <div class="info">
      <h3>${m.name}</h3>
      <p class="price">${rupiah(m.price)}</p>
    </div>
    <div class="qty">
      <button class="minus" data-act="minus" aria-label="Kurangi" disabled>−</button>
      <span class="num">0</span>
      <button class="plus" data-act="plus" aria-label="Tambah">+</button>
    </div>
  </article>
`).join("");

// ---- klik + / − (satu listener untuk semua card) ----
listEl.addEventListener("click", e => {
  const btn = e.target.closest("button[data-act]");
  if (!btn) return;
  const card = btn.closest(".item");
  const id = Number(card.dataset.id);

  qty[id] = Math.max(0, qty[id] + (btn.dataset.act === "plus" ? 1 : -1));

  card.querySelector(".num").textContent = qty[id];
  card.querySelector(".minus").disabled = qty[id] === 0;
  updateCart();
});

// ---- hitung total ----
function updateCart() {
  let items = 0, total = 0;
  MENU.forEach(m => { items += qty[m.id]; total += qty[m.id] * m.price; });

  countEl.textContent = items + " item";
  totalEl.textContent = rupiah(total);
  barEl.classList.toggle("show", items > 0);
}

// ---- tombol Pesan (kerangka) ----
document.getElementById("orderBtn").addEventListener("click", () => {
  // TODO: kirim pesanan (misalnya buka WhatsApp / pindah ke halaman checkout)
  const order = MENU.filter(m => qty[m.id] > 0)
                    .map(m => `${qty[m.id]}x ${m.name}`).join(", ");
  alert("Pesanan: " + order);
});