document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const categoryFilter = document.getElementById('categoryFilter');
    const brandFilter = document.getElementById('brandFilter');
    const sortFilter = document.getElementById('sortFilter');
    const clearSearch = document.getElementById('clearSearch');
    const clearAllFilters = document.getElementById('clearAllFilters');
    const resultsCount = document.getElementById('resultsCount');
    const noResults = document.getElementById('noResults');
    const productsContainer = document.getElementById('productsContainer');

    if (!searchInput) return; // Si no hay productos, no ejecutar el script

    let allProducts = Array.from(document.querySelectorAll('[data-name]'));
    let filteredProducts = [...allProducts];

    function populateBrandFilter() {
        const brands = new Set();
        allProducts.forEach(card => {
            const brand = card.dataset.brand;
            if (brand && brand.trim()) {
                brands.add(brand.trim());
            }
        });

        brandFilter.innerHTML = '<option value="">Todas las marcas</option>';

        Array.from(brands).sort().forEach(brand => {
            const option = document.createElement('option');
            option.value = brand;
            option.textContent = brand;
            brandFilter.appendChild(option);
        });
    }

    function filterProducts() {
        const searchTerm = searchInput.value.toLowerCase().trim();
        const categoryValue = categoryFilter.value.toLowerCase();
        const brandValue = brandFilter.value.toLowerCase();

        filteredProducts = allProducts.filter(card => {
            const name = (card.dataset.name || '').toLowerCase();
            const cod = (card.dataset.cod || '').toLowerCase();
            const brand = (card.dataset.brand || '').toLowerCase();
            const category = (card.dataset.category || '').toLowerCase();

            const matchesSearch = !searchTerm ||
                name.includes(searchTerm) ||
                cod.includes(searchTerm) ||
                brand.includes(searchTerm);

            const matchesCategory = !categoryValue || category.includes(categoryValue);

            const matchesBrand = !brandValue || brand.includes(brandValue);

            return matchesSearch && matchesCategory && matchesBrand;
        });

        updateDisplay();
    }

    function sortProducts() {
        const sortValue = sortFilter.value;

        filteredProducts.sort((a, b) => {
            switch(sortValue) {
                case 'name':
                    return (a.dataset.name || '').localeCompare(b.dataset.name || '');
                case 'cod':
                    return (a.dataset.cod || '').localeCompare(b.dataset.cod || '');
                case 'brand':
                    return (a.dataset.brand || '').localeCompare(b.dataset.brand || '');
                case 'price-asc':
                    return parseFloat(a.dataset.price || 0) - parseFloat(b.dataset.price || 0);
                case 'price-desc':
                    return parseFloat(b.dataset.price || 0) - parseFloat(a.dataset.price || 0);
                default:
                    return 0;
            }
        });

        updateDisplay();
    }

    function updateDisplay() {
        allProducts.forEach(card => {
            card.classList.add('hidden');
        });

        filteredProducts.forEach((card, index) => {
            card.classList.remove('hidden');
            card.style.order = index;
        });

        const hasFilters = searchInput.value || categoryFilter.value || brandFilter.value;
        if (filteredProducts.length === 0) {
            noResults.style.display = 'block';
            if (productsContainer) productsContainer.style.display = 'none';
            resultsCount.textContent = 'No se encontraron productos';
        } else {
            noResults.style.display = 'none';
            if (productsContainer) productsContainer.style.display = 'flex';
            if (hasFilters) {
                resultsCount.textContent = `Mostrando ${filteredProducts.length} de ${allProducts.length} productos`;
            } else {
                resultsCount.textContent = `Mostrando todos los productos (${allProducts.length})`;
            }
        }

        clearSearch.style.display = searchInput.value ? 'block' : 'none';
        clearAllFilters.style.display = hasFilters ? 'inline' : 'none';
    }

    searchInput.addEventListener('input', function() {
        filterProducts();
    });

    categoryFilter.addEventListener('change', function() {
        filterProducts();
    });

    brandFilter.addEventListener('change', function() {
        filterProducts();
    });

    sortFilter.addEventListener('change', function() {
        sortProducts();
    });

    clearSearch.addEventListener('click', function() {
        searchInput.value = '';
        filterProducts();
        searchInput.focus();
    });

    clearAllFilters.addEventListener('click', function() {
        searchInput.value = '';
        categoryFilter.value = '';
        brandFilter.value = '';
        sortFilter.value = 'name';
        filterProducts();
    });

    populateBrandFilter();
    updateDisplay();
});


