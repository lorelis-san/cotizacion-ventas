class ProductLoader {
constructor() {
    const userRole = document.getElementById('userRole')?.dataset?.role;
        this.isAdmin = userRole === 'ADMIN';
        this.page = 0;
        this.size = 9;
        this.isLoading = false;
        this.endReached = false;
        this.grid = null;
        this.loadingElement = null;

        this.init();
    }

    init() {
        // Crear elementos del DOM si no existen
        this.ensureDOMElements();
        // Configurar event listeners
        this.setupScrollListener();
        this.loadProductos();
    }

    ensureDOMElements() {
        this.grid = document.getElementById('producto-grid');
        this.loadingElement = document.getElementById('loading');

        if (!this.grid) {
            console.error('Elemento #producto-grid no encontrado');
            return;
        }
    }

    setupScrollListener() {
        let ticking = false;

        window.addEventListener('scroll', () => {
            if (!ticking) {
                requestAnimationFrame(() => {
                    if (this.isNearBottom()) {
                        this.loadProductos();
                    }
                    ticking = false;
                });
                ticking = true;
            }
        });
    }

    isNearBottom() {
        return (window.innerHeight + window.scrollY) >= document.body.offsetHeight - 200;
    }

    async loadProductos() {
        if (this.isLoading || this.endReached) return;

        this.isLoading = true;
        this.showLoading();

        try {
            const response = await fetch(`/api/productos?page=${this.page}&size=${this.size}`);

            if (!response.ok) {
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }

            const data = await response.json();

            if (!data.content || data.content.length === 0) {
                this.endReached = true;
                this.showNoMoreProducts();
                return;
            }

            this.renderProductos(data.content);
            this.page++;

        } catch (error) {
            console.error('Error cargando productos:', error);
            this.showError(error.message);
        } finally {
            this.isLoading = false;
            this.hideLoading();
        }
    }

    renderProductos(productos) {
        const fragment = document.createDocumentFragment();

        productos.forEach(producto => {
            const card = this.createProductCard(producto);
            fragment.appendChild(card);
        });

        this.grid.appendChild(fragment);
    }

    createProductCard(prod) {

        const div = document.createElement('div');
        div.className = 'producto';
        // Sanitizar datos
        const name = this.sanitizeText(prod.name || 'Producto sin nombre');
        const category = this.sanitizeText(prod.category || 'Sin categoría');
        const brand = this.sanitizeText(prod.brand || 'Sin marca');
        const model = this.sanitizeText(prod.model || '');
        const code = this.sanitizeText(prod.cod || '');
        const startYear = prod.startYear || '';
        const endYear = prod.endYear || '';
        const salePrice = this.formatPrice(prod.salePrice);
        const dealerPrice = this.formatPrice(prod.costDealer);
        const imageUrl = prod.imageUrl || 'data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 100 100"><rect width="100" height="100" fill="%23f8f9fa"/><text x="50" y="50" text-anchor="middle" dy=".3em" fill="%23666">Sin imagen</text></svg>';

        div.innerHTML = `
            <div class="producto-image">
                <img src="${imageUrl}"
                     loading="lazy"
                     alt="${name}"
                     onerror="this.src='data:image/svg+xml,<svg xmlns=\\"http://www.w3.org/2000/svg\\" width=\\"100\\" height=\\"100\\" viewBox=\\"0 0 100 100\\"><rect width=\\"100\\" height=\\"100\\" fill=\\"%23f8f9fa\\"/><text x=\\"50\\" y=\\"50\\" text-anchor=\\"middle\\" dy=\\".3em\\" fill=\\"%23666\\">Sin imagen</text></svg>'" />
            </div>
            <div class="producto-content">
                <div class="producto-category">${category}</div>
                 <h4 class="producto-title">${name}</h4>
                ${code ? `<div class="producto-code">${code}</div>` : ''}

                <p class="producto-brand">${brand}${model ? ` - ${model}` : ''}</p>
                ${(startYear || endYear) ? `
                  <div class="producto-years">
                    ${startYear}${endYear && endYear !== startYear ? ` - ${endYear === 9999 ? 'Actualidad' : endYear}` : ''}
                  </div>
                ` : ''}

                <div class="producto-prices">
                    <div class="producto-price sale">
                        <span><span class="currency">S/</span> ${salePrice}</span>
                        <span class="label">Precio Venta</span>
                    </div>
                    ${dealerPrice && parseFloat(prod.costDealer) > 0 ? `
                    <div class="producto-price dealer">
                        <span><span class="currency">S/</span> ${dealerPrice}</span>
                        <span class="label">Precio Dealer</span>
                    </div>
                    ` : ''}
                </div>

                <div class="product-actions mt-auto">
                    <a href="/view/${prod.id}" class="btn btn-action btn-view">
                        <i class="fas fa-eye"></i>
                        <span>Ver</span>
                    </a>
                     ${this.isAdmin ? `
                      <div class="action-group admin-only">
                          <a href="/edit/${prod.id}" class="btn btn-outline-primary btn-sm">
                              <i class="fas fa-edit"></i>
                          </a>
                          <button class="btn btn-outline-danger btn-sm"
                                  data-id="${prod.id}"
                                  data-numero="${code}"
                                  data-bs-toggle="modal"
                                  data-bs-target="#eliminarProductoModal"
                                  onclick="cargarDatosEliminar(this)">
                              <i class="fas fa-trash-alt"></i>
                          </button>
                      </div>` : ''}
                </div>
            </div>
        `;
        return div;
    }

    sanitizeText(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    formatPrice(price) {
        if (!price && price !== 0) return '0.00';
        return Number(price).toLocaleString('es-PE', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    }

    showLoading() {
        if (this.loadingElement) {
            this.loadingElement.innerHTML = `
                <div class="loading-spinner"></div>
                Cargando productos...
            `;
            this.loadingElement.style.display = 'block';
        }
    }

    hideLoading() {
        if (this.loadingElement) {
            this.loadingElement.style.display = 'none';
        }
    }

    showNoMoreProducts() {
        if (this.loadingElement) {
            this.loadingElement.innerHTML = `
                <div class="no-more-products">
                    ✨ Has visto todos nuestros productos ✨
                </div>
            `;
            this.loadingElement.style.display = 'block';
        }
    }

    showError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.innerHTML = `
            <strong>Error al cargar productos:</strong> ${message}
            <br><small>Intenta recargar la página</small>
        `;

        this.grid.parentNode.insertBefore(errorDiv, this.grid.nextSibling);

        setTimeout(() => {
            errorDiv.remove();
        }, 5000);
    }
}

function cargarDatosEliminar(button) {
    const codigo = button.getAttribute('data-numero');
    const id = button.getAttribute('data-id');

    document.getElementById('codEliminar').textContent = codigo || 'Sin código';

    // Guardar el ID para la eliminación
    document.getElementById('confirmarEliminarBtn').setAttribute('data-product-id', id);
}
//////////////////////
// Añadir esta clase extendida para integrar la búsqueda sin perder diseño
class ProductSearchLoader extends ProductLoader {
    constructor() {
        super();

        this.searchInput = document.getElementById('buscadorProducto');
        this.originalGrid = null;
        this.bindSearch();
    }

    bindSearch() {
        if (this.searchInput) {
            this.searchInput.addEventListener('input', this.debounce(() => {
                const term = this.searchInput.value.trim();
                if (term.length > 0) {
                    this.buscarProductos(term);
                } else {
                    this.resetScrollInfinite();
                }
            }, 500));
        }
    }

    async buscarProductos(termino) {
        try {
            const response = await fetch(`/api/productos/buscarProducto/${encodeURIComponent(termino)}`);
            if (!response.ok) throw new Error('No encontrado');

            const productos = await response.json();
            this.grid.innerHTML = '';
            this.renderProductos(productos);

            // detener scroll infinito
            this.endReached = true;
        } catch (err) {
            console.error('Error en búsqueda:', err);
            this.grid.innerHTML = '<p>No se encontraron productos.</p>';
        }
    }

    resetScrollInfinite() {
        this.page = 0;
        this.endReached = false;
        this.grid.innerHTML = '';
        this.loadProductos();
    }

    debounce(func, delay) {
        let timeout;
        return function (...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), delay);
        };
    }
}

// NUEVA CLASE PARA FILTROS AVANZADOS

class ProductFilterLoader extends ProductLoader {
    constructor() {
        super();

        this.filtroCategoria = document.getElementById('filtroCategoria');
        this.filtroMarca = document.getElementById('filtroMarca');
        this.filtroAnio = document.getElementById('filtroAnio');
        this.btnLimpiarFiltros = document.getElementById('btnLimpiarFiltros');
        this.filtrosActivosContainer = document.getElementById('filtrosActivos');

        this.filtrosActivos = {
            categoria: '',
            marca: '',
            anioo: ''
        };

        this.initFiltros();
    }

    async initFiltros() {
        await this.cargarOpcionesFiltros();
        this.setupFiltrosEventListeners();
    }

    async cargarOpcionesFiltros() {
        try {
            const response = await fetch('/api/productos/filtros/opciones');
            if (!response.ok) throw new Error('Error al cargar opciones de filtros');

            const opciones = await response.json();

            // Llenar selectores
            this.llenarSelector(this.filtroCategoria, opciones.categorias, 'Todas las categorías');
            this.llenarSelector(this.filtroMarca, opciones.marcas, 'Todas las marcas');
            this.llenarSelector(this.filtroAnio, opciones.anios, 'Todos los años');

        } catch (error) {
            console.error('Error cargando opciones de filtros:', error);
        }
    }

    llenarSelector(selector, opciones, placeholder) {
        if (!selector) return;

        selector.innerHTML = `<option value="">${placeholder}</option>`;

        opciones.forEach(opcion => {
            const option = document.createElement('option');
            option.value = opcion;
            option.textContent = opcion;
            selector.appendChild(option);
        });
    }

    setupFiltrosEventListeners() {
        if (this.filtroCategoria) {
            this.filtroCategoria.addEventListener('change', () => this.aplicarFiltros());
        }

        if (this.filtroMarca) {
            this.filtroMarca.addEventListener('change', () => this.aplicarFiltros());
        }

        if (this.filtroAnio) {
            this.filtroAnio.addEventListener('change', () => this.aplicarFiltros());
        }

        if (this.btnLimpiarFiltros) {
            this.btnLimpiarFiltros.addEventListener('click', () => this.limpiarFiltros());
        }
    }

    async aplicarFiltros() {
        this.filtrosActivos.categoria = this.filtroCategoria?.value || '';
        this.filtrosActivos.marca = this.filtroMarca?.value || '';
        this.filtrosActivos.anio = this.filtroAnio?.value || '';

        const hayFiltros = Object.values(this.filtrosActivos).some(valor => valor !== '');

        if (hayFiltros) {
            await this.filtrarProductos();
        } else {
            this.resetScrollInfinite();
        }
        this.actualizarFiltrosActivos();
    }

    async filtrarProductos() {
        try {
            this.showLoading();

            const params = new URLSearchParams();
            if (this.filtrosActivos.categoria) params.append('categoria', this.filtrosActivos.categoria);
            if (this.filtrosActivos.marca) params.append('marca', this.filtrosActivos.marca);
            if (this.filtrosActivos.anio) params.append('anio', this.filtrosActivos.anio);

            const response = await fetch(`/api/productos/filtrar?${params.toString()}`);
            if (!response.ok) throw new Error('Error al filtrar productos');

            const productos = await response.json();

            this.grid.innerHTML = '';
            this.renderProductos(productos);

            this.endReached = true;

        } catch (error) {
            console.error('Error filtrando productos:', error);
            this.showError('Error al aplicar filtros');
        } finally {
            this.hideLoading();
        }
    }

    limpiarFiltros() {
        if (this.filtroCategoria) this.filtroCategoria.value = '';
        if (this.filtroMarca) this.filtroMarca.value = '';
        if (this.filtroAnio) this.filtroAnio.value = '';

        this.filtrosActivos = {
            categoria: '',
            marca: '',
            anio: ''
        };

        this.resetScrollInfinite();
        this.actualizarFiltrosActivos();
    }

    actualizarFiltrosActivos() {
        if (!this.filtrosActivosContainer) return;

        const filtrosHTML = [];

        if (this.filtrosActivos.categoria) {
            filtrosHTML.push(`
                <span class="filtro-activo">
                    Categoría: ${this.filtrosActivos.categoria}
                    <button type="button" class="btn-close" onclick="productFilterLoader.quitarFiltro('categoria')"></button>
                </span>
            `);
        }

        if (this.filtrosActivos.marca) {
            filtrosHTML.push(`
                <span class="filtro-activo">
                    Marca: ${this.filtrosActivos.marca}
                    <button type="button" class="btn-close" onclick="productFilterLoader.quitarFiltro('marca')"></button>
                </span>
            `);
        }

        if (this.filtrosActivos.anio) {
            filtrosHTML.push(`
                <span class="filtro-activo">
                    Anio: ${this.filtrosActivos.anio}
                    <button type="button" class="btn-close" onclick="productFilterLoader.quitarFiltro('anio')"></button>
                </span>
            `);
        }

        if (filtrosHTML.length > 0) {
            this.filtrosActivosContainer.innerHTML = `
                <strong>Filtros activos:</strong><br>
                ${filtrosHTML.join('')}
            `;
            this.filtrosActivosContainer.style.display = 'block';
        } else {
            this.filtrosActivosContainer.style.display = 'none';
        }
    }

    quitarFiltro(tipoFiltro) {
        switch (tipoFiltro) {
            case 'categoria':
                if (this.filtroCategoria) this.filtroCategoria.value = '';
                break;
            case 'marca':
                if (this.filtroMarca) this.filtroMarca.value = '';
                break;
            case 'anio':
                if (this.filtroAnio) this.filtroAnio.value = '';
                break;
        }
        this.aplicarFiltros();
    }

    resetScrollInfinite() {
        this.page = 0;
        this.endReached = false;
        this.grid.innerHTML = '';
        this.loadProductos();
    }
}
// CLASE COMBINADA QUE INTEGRA BÚSQUEDA Y FILTROS
class ProductSearchAndFilterLoader extends ProductFilterLoader {
    constructor() {
        super();

        this.searchInput = document.getElementById('buscadorProducto');
        this.setupSearchListener();
    }

    setupSearchListener() {
        if (this.searchInput) {
            this.searchInput.addEventListener('input', this.debounce(() => {
                const term = this.searchInput.value.trim();
                if (term.length > 0) {
                    this.buscarProductos(term);
                    this.limpiarFiltrosSinAplicar();
                } else {
                    this.aplicarFiltros();
                }
            }, 500));
        }
    }

    async buscarProductos(termino) {
        try {

        const response = await fetch(`/api/productos/buscarProducto?termino=${encodeURIComponent(termino)}`);

            if (!response.ok) throw new Error('No encontrado');

            const productos = await response.json();
            this.grid.innerHTML = '';
            this.renderProductos(productos);

            this.endReached = true;
        } catch (err) {
            console.error('Error en búsqueda:', err);
            this.grid.innerHTML = '<p>No se encontraron productos.</p>';
        }
    }

    limpiarFiltrosSinAplicar() {
        if (this.filtroCategoria) this.filtroCategoria.value = '';
        if (this.filtroMarca) this.filtroMarca.value = '';
        if (this.filtroAnio) this.filtroAnio.value = '';

        this.filtrosActivos = {
            categoria: '',
            marca: '',
            anio: ''
        };

        this.actualizarFiltrosActivos();
    }

    async aplicarFiltros() {
        if (this.searchInput && this.searchInput.value.trim().length > 0) {
            return;
        }

        await super.aplicarFiltros();
    }

    debounce(func, delay) {
        let timeout;
        return function (...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), delay);
        };
    }
}

let productFilterLoader;

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        productFilterLoader = new ProductSearchAndFilterLoader();
    });
} else {
    productFilterLoader = new ProductSearchAndFilterLoader();
}
