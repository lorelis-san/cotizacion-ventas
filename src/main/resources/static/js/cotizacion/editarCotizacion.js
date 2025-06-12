async function buscarProducto() {
    const termino = document.getElementById("busquedaProducto").value.trim();

    if (!termino) {
        document.getElementById("resultadosProducto").innerHTML = "";
        return;
    }

    const response = await fetch(`/api/productos/buscar/${termino}`);
    const productos = await response.json();

    const resultadosDiv = document.getElementById("resultadosProducto");
    resultadosDiv.innerHTML = "";

    if (productos.length === 0) {
        resultadosDiv.innerHTML = "<p>No se encontraron productos.</p>";
        return;
    }

    productos.forEach(producto => {
        const productoHTML = `
            <div class="card mb-2" style="background-color: white; color: #0e0c28;">
                <div class="card-body d-flex align-items-center justify-content-between flex-wrap">
                    <div class="d-flex align-items-center gap-3 flex-grow-1 flex-wrap">
                        <div class="me-3 text-center">
                            <div class="fw-bold">CÓD:</div>
                            <div>${producto.cod}</div>
                        </div>
                        <div class="me-3">
                            <h5 class="mb-0">${producto.name}</h5>
                            <small>Precio: S/ ${producto.salePrice}</small>
                        </div>
                    </div>
                    <div class="d-flex align-items-center gap-2 mt-2 mt-sm-0">
                        <input type="number" id="cantidad_${producto.id}" class="form-control form-control-sm" placeholder="Cantidad" min="1" value="1" style="width: 80px;" />
                        <button class="btn btn-flat btn-sm" onclick="agregarAlCarrito(${producto.id}, '${producto.name}', ${producto.salePrice}, event)">Agregar</button>
                    </div>

                </div>
            </div>
            <br>
        `;
        resultadosDiv.innerHTML += productoHTML;
    });

}

let timeoutId;

function filtrarBusqueda() {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => {
        buscarProducto(); // llama a tu función existente
    }, 300); // 300ms de espera después de que el usuario deja de tipear
}

function agregarAlCarrito(id, nombre, precio) {
   if (event) event.preventDefault(); // evita envío accidental

    console.log("👉 [INICIO] agregarAlCarrito");
    console.log("🟦 Datos recibidos:", { id, nombre, precio });

    if (id == null || nombre == null || precio == null) {
        console.error("❌ Uno o más datos del producto son nulos o indefinidos", { id, nombre, precio });
        alert("Error interno al agregar producto: datos incompletos.");
        return;
    }

    const cantidadInput = document.getElementById(`cantidad_${id}`);
    console.log("🔎 Buscando input cantidad:", cantidadInput);

    if (!cantidadInput) {
        console.error("❌ No se encontró el input de cantidad para el producto con ID", id);
        return;
    }

    const cantidad = parseInt(cantidadInput.value);
    console.log("📦 Cantidad ingresada:", cantidad);

    if (!cantidad || cantidad < 1) {
        console.warn("⚠️ Cantidad inválida");
        alert("Cantidad inválida");
        return;
    }

    const contenedor = document.getElementById("contenedor-productos");
    const filas = contenedor.querySelectorAll(".producto-item");
    console.log("🔄 Filas actuales en carrito:", filas.length);

    for (const fila of filas) {
        const inputProducto = fila.querySelector('input[name$=".productoId"]');
        const idProductoEnCarrito = parseInt(inputProducto?.value);
        console.log("🧾 Revisando fila con producto ID:", idProductoEnCarrito);

        if (idProductoEnCarrito === id) {
            console.log("✅ Producto ya existe en el carrito. Actualizando cantidad...");
            const cantidadExistenteInput = fila.querySelector('input[name$=".cantidad"]');
            cantidadExistenteInput.value = parseInt(cantidadExistenteInput.value) + cantidad;
            actualizarSubtotal(cantidadExistenteInput);
            actualizarTotales();
            return;
        }
    }

    const index = filas.length;
    const subtotal = (precio * cantidad).toFixed(2);
    console.log("📊 Agregando nuevo producto al carrito con index:", index);

    const nuevaFila = document.createElement("div");
    nuevaFila.className = "row align-items-center mb-2 producto-item";
    nuevaFila.innerHTML = `
        <div class="col-md-3">
            <input type="hidden" name="detalles[${index}].productoId" value="${id}" />
            ${nombre}
        </div>
        <div class="col-md-2">
            <input type="number" step="0.01" class="form-control" name="detalles[${index}].precioUnitario" value="${precio}" readonly />
        </div>
        <div class="col-md-2">
            <input type="number" min="1" class="form-control" name="detalles[${index}].cantidad" value="${cantidad}" onchange="actualizarSubtotal(this)" />
        </div>
        <div class="col-md-2">
            <input type="text" class="form-control" name="detalles[${index}].subtotal" value="${subtotal}" readonly />
        </div>
        <div class="col-md-2">
            <button type="button" class="btn btn-danger btn-sm" onclick="eliminarFila(this)"> <i class="fas fa-trash-alt"></i></button>

        </div>
    `;
    contenedor.appendChild(nuevaFila);
    actualizarTotales();

    console.log("✅ Producto agregado correctamente al carrito");
}

function actualizarSubtotal(inputCantidad) {
    const fila = inputCantidad.closest('.producto-item');
    const precioUnitario = parseFloat(fila.querySelector('input[name$=".precioUnitario"]').value);
    const cantidad = parseInt(inputCantidad.value);
    const subtotal = (precioUnitario * cantidad).toFixed(2);
    fila.querySelector('input[name$=".subtotal"]').value = subtotal;
    actualizarTotales();
}

function eliminarFila(boton) {
    const fila = boton.closest('.producto-item');
    fila.remove();
    reindexarDetalles();
    actualizarTotales();
}

function reindexarDetalles() {
    const filas = document.querySelectorAll('#contenedor-productos .producto-item');
    filas.forEach((fila, index) => {
        const inputs = fila.querySelectorAll('input');
        inputs.forEach(input => {
            input.name = input.name.replace(/\[\d+\]/, `[${index}]`);
        });
    });
}

function actualizarTotales() {
    let subtotal = 0;
    const filas = document.querySelectorAll('#contenedor-productos .producto-item');

    filas.forEach(fila => {
        const sub = parseFloat(fila.querySelector('input[name$=".subtotal"]').value);
        subtotal += sub;
    });

    const igv = subtotal * 0.18;
    const total = subtotal + igv;
    document.querySelector('input[name="subtotal"]').value = subtotal.toFixed(2);
//    document.querySelector('input[name="igv"]').value = igv.toFixed(2);
    document.querySelector('input[name="total"]').value = total.toFixed(2);

}


function guardarCotizacion() {
    console.log("[INICIO] guardarCotizacion");

    const form = document.getElementById("formCotizacion");

    // Reindexa los productos antes de enviar
    reindexarDetalles();

    // Valida que haya al menos un producto
    const productos = form.querySelectorAll('.producto-item');
    if (productos.length === 0) {
        alert("Debes agregar al menos un producto");
        return;
    }

    form.submit();
}

