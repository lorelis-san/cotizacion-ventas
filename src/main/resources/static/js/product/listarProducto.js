 class ProductLoader {
                constructor() {
                const userRole = document.getElementById('userRole')?.dataset?.role;
                    this.isAdmin = userRole === 'ADMIN';
                    this.page = 0;
                    this.size = 10;
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
                console.log('Renderizando producto:', prod.cod, '| ¿Es admin?', this.isAdmin);

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
                            ${code ? `<div class="producto-code">${code}</div>` : ''}
                            <h4 class="producto-title">${name}</h4>
                            <p class="producto-brand">${brand}${model ? ` - ${model}` : ''}</p>
                            ${(startYear || endYear) ? `<div class="producto-years">${startYear}${endYear && endYear !== startYear ? ` - ${endYear}` : ''}</div>` : ''}

                            <div class="producto-prices">
                                <div class="producto-price sale">
                                    <span><span class="currency">S/</span> ${salePrice}</span>
                                    <span class="label">Precio Venta</span>
                                </div>
                                ${dealerPrice && parseFloat(prod.costDealer) > 0 ? `
                                <div class="producto-price dealer">
                                    <span><span class="currency">S/</span> ${dealerPrice}</span>
                                    <span class="label">Costo Dealer</span>
                                </div>
                                ` : ''}
                            </div>

                            <div class="product-actions">
                                <a href="/view/${prod.id}" class="btn btn-action btn-view">
                                    <i class="fas fa-eye"></i>
                                    <span>Ver</span>
                                </a>
                                 ${this.isAdmin ? `
  <div class="action-group admin-only">
      <a href="/edit/${prod.id}" class="btn btn-action btn-edit">
          <i class="fas fa-edit"></i>
      </a>
      <button class="btn btn-action btn-delete"
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

                    // Remover error después de 5 segundos
                    setTimeout(() => {
                        errorDiv.remove();
                    }, 5000);
                }
            }



            // Función para cargar datos en el modal de eliminar (debe existir globalmente)
            function cargarDatosEliminar(button) {
                const codigo = button.getAttribute('data-numero');
                const id = button.getAttribute('data-id');

                document.getElementById('codEliminar').textContent = codigo || 'Sin código';

                // Guardar el ID para la eliminación
                document.getElementById('confirmarEliminarBtn').setAttribute('data-product-id', id);
            }


            // Llamar esta función según el rol del usuario
            // toggleAdminActions(true); // Para mostrar acciones de admin
            // toggleAdminActions(false); // Para ocultar acciones de admin



if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        new ProductLoader();

    });
} else {
    new ProductLoader();

}

