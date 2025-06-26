async function buscarProducto() {
    const termino = document.getElementById('buscadorProducto').value.trim();

    if (!termino) return;

    try {
        const response = await fetch(`/api/productos/buscarProducto/${encodeURIComponent(termino)}`);

        if (!response.ok) {
            throw new Error('No se encontraron productos');
        }

        const productos = await response.json();

        // Limpiar el grid actual
        const grid = document.getElementById('producto-grid');
        grid.innerHTML = '';

        productos.forEach(prod => {
            const div = document.createElement('div');
            div.className = 'producto';

            const name = prod.name || 'Sin nombre';
            const cod = prod.cod || '';
            const salePrice = prod.salePrice?.toFixed(2) || '0.00';

            div.innerHTML = `
                <div class="producto-content p-3">
                    <div class="producto-code">${cod}</div>
                    <h4 class="producto-title">${name}</h4>
                    <div class="producto-prices">
                        <div class="producto-price sale">
                            <span><span class="currency">S/</span> ${salePrice}</span>
                        </div>
                    </div>
                </div>
            `;

            grid.appendChild(div);
        });

    } catch (error) {
        console.error('Error en búsqueda:', error);
        alert("No se encontraron productos con ese término");
    }
}
