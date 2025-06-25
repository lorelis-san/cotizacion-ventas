document.addEventListener('DOMContentLoaded', function() {
     const searchInput = document.getElementById('searchInput');
     const categoryFilter = document.getElementById('categoryFilter');
     const brandFilter = document.getElementById('brandFilter');
     const yearFilter = document.getElementById('yearFilter');
     const sortFilter = document.getElementById('sortFilter');
     const clearSearch = document.getElementById('clearSearch');
     const clearAllFilters = document.getElementById('clearAllFilters');
     const resultsCount = document.getElementById('resultsCount');
     const noResults = document.getElementById('noResults');
     const productsContainer = document.getElementById('productsContainer');

     if (!searchInput) return;

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

     function populateYearFilter() {
         const years = new Set();
         allProducts.forEach(card => {
             const productYears = card.dataset.years;
             if (productYears) {
                 if (productYears.includes('-')) {
                     const parts = productYears.split('-');
                     const startYear = parseInt(parts[0]);

                     if (parts[1] === '' || parts[1] === undefined) {
                         const currentYear = new Date().getFullYear();
                         for (let year = startYear; year <= currentYear + 5; year++) {
                             years.add(year);
                         }
                     } else {
                         const endYear = parseInt(parts[1]);
                         for (let year = startYear; year <= endYear; year++) {
                             years.add(year);
                         }
                     }
                 } else if (productYears.includes('+')) {
                     const startYear = parseInt(productYears.replace('+', ''));
                     const currentYear = new Date().getFullYear();
                     for (let year = startYear; year <= currentYear + 5; year++) {
                         years.add(year);
                     }
                 } else {
                     const year = parseInt(productYears);
                     if (!isNaN(year)) {
                         years.add(year);
                     }
                 }
             }
         });
         yearFilter.innerHTML = '<option value="">Todos los años</option>';
         Array.from(years).sort((a, b) => a - b).forEach(year => {
             const option = document.createElement('option');
             option.value = year;
             option.textContent = year;
             yearFilter.appendChild(option);
         });
     }

     function isYearInRange(productYears, searchYear) {
         if (!productYears || !searchYear) return true;

         const searchYearNum = parseInt(searchYear);

         if (productYears.includes('-')) {
             const parts = productYears.split('-');
             const startYear = parseInt(parts[0]);

             if (parts[1] === '' || parts[1] === undefined) {
                 return searchYearNum >= startYear;
             } else {
                 const endYear = parseInt(parts[1]);
                 return searchYearNum >= startYear && searchYearNum <= endYear;
             }
         } else if (productYears.includes('+')) {
             const startYear = parseInt(productYears.replace('+', ''));
             return searchYearNum >= startYear;
         } else {
             const productYear = parseInt(productYears);
             return searchYearNum === productYear;
         }
     }

     function filterProducts() {
         const searchTerm = searchInput.value.toLowerCase().trim();
         const categoryValue = categoryFilter.value.toLowerCase();
         const brandValue = brandFilter.value.toLowerCase();
         const yearValue = yearFilter.value;

         filteredProducts = allProducts.filter(card => {
             const name = (card.dataset.name || '').toLowerCase();
             const cod = (card.dataset.cod || '').toLowerCase();
             const brand = (card.dataset.brand || '').toLowerCase();
             const category = (card.dataset.category || '').toLowerCase();
             const years = card.dataset.years || '';

             const matchesSearch = !searchTerm ||
                 name.includes(searchTerm) ||
                 cod.includes(searchTerm) ||
                 brand.includes(searchTerm);
             const matchesCategory = !categoryValue || category.includes(categoryValue);
             const matchesBrand = !brandValue || brand.includes(brandValue);
             const matchesYear = !yearValue || isYearInRange(years, yearValue);
             return matchesSearch && matchesCategory && matchesBrand && matchesYear;
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

         const hasFilters = searchInput.value || categoryFilter.value || brandFilter.value || yearFilter.value;

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
     searchInput.addEventListener('input', filterProducts);
     categoryFilter.addEventListener('change', filterProducts);
     brandFilter.addEventListener('change', filterProducts);
     yearFilter.addEventListener('change', filterProducts);
     sortFilter.addEventListener('change', sortProducts);

     clearSearch.addEventListener('click', function() {
         searchInput.value = '';
         filterProducts();
         searchInput.focus();
     });

     clearAllFilters.addEventListener('click', function() {
         searchInput.value = '';
         categoryFilter.value = '';
         brandFilter.value = '';
         yearFilter.value = '';
         sortFilter.value = 'name';
         filterProducts();
     });
     populateBrandFilter();
     populateYearFilter();
     updateDisplay();
 });

//
//document.addEventListener('DOMContentLoaded', function() {
//
//    function applyFilters() {
//        const name = document.getElementById('searchInput').value;
//        const category = document.getElementById('categoryFilter').value;
//        const brand = document.getElementById('brandFilter').value;
//        const year = document.getElementById('yearFilter').value;
//        const sort = document.getElementById('sortFilter').value;
//
//        const params = new URLSearchParams({
//            name: name,
//            category: category,
//            brand: brand,
//            year: year,
//            sort: sort
//        });
//
//        fetch(`/products/filter?${params}`)
//            .then(res => res.json())
//            .then(data => {
//                updateProductList(data);
//            });
//    }
//
//    // Vuelve a aplicar filtros cada vez que cambie algo
//    document.getElementById('searchInput').addEventListener('input', applyFilters);
//    document.getElementById('categoryFilter').addEventListener('change', applyFilters);
//    document.getElementById('brandFilter').addEventListener('change', applyFilters);
//    document.getElementById('yearFilter').addEventListener('change', applyFilters);
//    document.getElementById('sortFilter').addEventListener('change', applyFilters);
//
//    function updateProductList(products) {
//        const container = document.getElementById('productListContainer'); // Define este en tu HTML
//        container.innerHTML = '';
//
//        if (products.length === 0) {
//            document.getElementById('noResults').style.display = 'block';
//            return;
//        }
//
//        document.getElementById('noResults').style.display = 'none';
//
//        products.forEach(product => {
//            const card = document.createElement('div');
//            card.className = 'product-card';
//            card.innerHTML = `
//                <h5>${product.name}</h5>
//                <p>${product.category} - ${product.brand} - ${product.year}</p>
//                <p>Precio: S/ ${product.price}</p>
//            `;
//            container.appendChild(card);
//        });
//    }
//});