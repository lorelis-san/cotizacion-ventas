//js de mi proyecto
async function buscarProducto() {
    const termino = document.getElementById('busquedaProducto').value.trim();
    const contenedor = document.getElementById('resultadosProducto');
    contenedor.innerHTML = '';
    contenedor.classList.add('hidden');

    if (!termino) {
       console.log("Ingrese un término de búsqueda");
        return;
    }

    try {
        const response = await fetch(`/api/productos/buscar/${termino}`);
        if (!response.ok) throw new Error("Error al buscar productos");

        const productos = await response.json();
        if (productos.length === 0) {
            contenedor.innerHTML = "<p>No se encontraron productos</p>";
        } else {
            contenedor.classList.remove('hidden');
            productos.forEach(p => {
                const div = document.createElement('div');
                div.className = 'producto-item';
                div.innerHTML = `
                     <div class="me-3">
                          <img src="${p.imageUrl || '/images/no-image.png'}"
                               alt="${p.name}"
                               class="img-thumbnail"
                               style="width: 60px; height: 60px; object-fit: contain;"
                               onerror="this.src='/images/no-image.png'">
                     </div>
                     <div class="flex-grow-1">
                          <strong>${p.name}</strong> <span>(${p.cod})</span><br>
                          <span class="text-success fw-bold">S/. ${p.salePrice}</span>
                     </div>
                     <div class="me-2">
                          <input type="number" id="cantidad_${p.id}" value="1" min="1"
                                 class="form-control form-control-sm" style="width: 70px;"/>
                     </div>
                     <div>
                          <button onclick="agregarAlCarrito(${p.id}, '${p.name}', ${p.salePrice}, '${p.imageUrl || ''}')"
                                  class="btn btn-dark btn-sm">
                              <i class="fas fa-plus"></i> Agregar
                          </button>
                     </div>
                `;
                  contenedor.appendChild(div);
            });
        }
    } catch (error) {
        console.error("Error al buscar productos", error);
        alert("Hubo un problema al buscar productos");
    }
}
let timeoutId;

function filtrarBusqueda() {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => {
        buscarProducto(); // llama a tu función existente
    }, 300); // 300ms de espera después de que el usuario deja de tipear
}


/////////////
const carrito = [];

function agregarAlCarrito(id, nombre, precio) {
    const cantidad = parseInt(document.getElementById(`cantidad_${id}`).value);
    if (!cantidad || cantidad < 1) return alert("Cantidad inválida");

    const existente = carrito.find(p => p.id === id);
    if (existente) {
        existente.cantidad += cantidad;
    } else {
        carrito.push({ id, nombre, precio, cantidad, imageUrl: imagenUrl});
    }

    renderizarCarrito();
}
function renderizarCarrito() {
    const contenedor = document.getElementById('carrito');
    contenedor.innerHTML = '';

    if (carrito.length === 0) {
        contenedor.innerHTML = '<p>El carrito está vacío</p>';
        return;
    }

    // Cabecera
    contenedor.innerHTML = `
        <div class="carrito-header row fw-bold mb-2">
            <div class="col-4">Producto</div>
            <div class="col-2">Cantidad</div>
            <div class="col-2">Precio</div>
            <div class="col-3">Subtotal</div>
            <div class="col-1 text-center"></div>

        </div>
    `;

    let total = 0;

    carrito.forEach(p => {
        const subtotal = p.precio * p.cantidad;
        total += subtotal;

        const item = document.createElement('div');
        item.className = 'carrito-item row align-items-center mb-2';
        item.innerHTML = `
            <div class="col-4">
                <div class="d-flex align-items-center">
                    <img src="${p.imagenUrl || '/images/no-image.png'}"
                         alt="${p.nombre}"
                         class="img-thumbnail me-2"
                         style="width: 40px; height: 40px; object-fit: contain;"
                         onerror="this.src='/images/no-image.png'">
                    <span>${p.nombre}</span>
                </div>
            </div>
            <div class="col-2">
                <div class="input-group input-group-sm">
                    <button class="btn btn-outline-secondary" onclick="cambiarCantidad(${p.id}, -1)">-</button>
                    <input type="text" class="form-control text-center" value="${p.cantidad}" readonly>
                    <button class="btn btn-outline-secondary" onclick="cambiarCantidad(${p.id}, 1)">+</button>
                </div>
            </div>
            <div class="col-2">S/ ${p.precio.toFixed(2)}</div>
            <div class="col-3 fw-bold text-success">S/ ${subtotal.toFixed(2)}</div>
            <div class="col-1 text-center">
                <button class="btn btn-sm btn-danger" onclick="eliminarDelCarrito(${p.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        `;
        contenedor.appendChild(item);
    });

    const totalRow = document.createElement('div');
    totalRow.className = 'carrito-total row fw-bold mt-3';
    totalRow.innerHTML = `
        <div class="col-8 text-end">Total:</div>
        <div class="col-3">S/ ${total.toFixed(2)}</div>
    `;
    contenedor.appendChild(totalRow);
}

    // Nueva función para cambiar cantidad con botones +/-
    function cambiarCantidad(id, cambio) {
        const producto = carrito.find(p => p.id === id);
        if (producto) {
            const nuevaCantidad = producto.cantidad + cambio;
            if (nuevaCantidad > 0) {
                producto.cantidad = nuevaCantidad;
                renderizarCarrito();
            }
        }
    }
    function eliminarDelCarrito(id) {
        const index = carrito.findIndex(p => p.id === id);
        if (index !== -1) {
            carrito.splice(index, 1);
            renderizarCarrito();
        }
    }
