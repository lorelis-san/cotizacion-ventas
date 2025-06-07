function validateForm() {
    let isValid = true;
    clearAllErrors();

    const cod = document.getElementById('cod').value.trim();
    if (!cod) {
        showError('cod', 'El código del producto es obligatorio');
        isValid = false;
    }

    const name = document.getElementById('name').value.trim();
    if (!name) {
        showError('name', 'El nombre del producto es obligatorio');
        isValid = false;
    }

    const brand = document.getElementById('brand').value.trim();
    if (!brand) {
        showError('brand', 'La marca del producto es obligatoria');
        isValid = false;
    }

    const categoryProductId = document.getElementById('categoryProductId').value;
    if (!categoryProductId || categoryProductId === '') {
        showError('category', 'La categoría del producto es obligatoria');
        isValid = false;
    }

    const supplierProductId = document.getElementById('supplierProductId').value;
    if (!supplierProductId || supplierProductId === '') {
        showError('supplier', 'El proveedor del producto es obligatorio');
        isValid = false;
    }

    const costPrice = parseFloat(document.getElementById('costPrice').value);
    if (!costPrice || costPrice <= 0) {
        showError('costPrice', 'El precio de costo es obligatorio y debe ser mayor a 0');
        isValid = false;
    }

    const dealerPrice = parseFloat(document.getElementById('dealerPrice').value);
    if (!dealerPrice || dealerPrice <= 0) {
        showError('dealerPrice', 'El precio de distribuidor es obligatorio y debe ser mayor a 0');
        isValid = false;
    }

    const salePrice = parseFloat(document.getElementById('salePrice').value);
    if (salePrice && costPrice && salePrice < costPrice) {
        showError('salePrice', 'El precio de venta no puede ser menor que el costo');
        isValid = false;
    }

    const year = document.getElementById('year').value;
    if (year && year.trim() !== '') {
        const currentYear = new Date().getFullYear();
        const yearNum = parseInt(year);
        if (yearNum < 1900 || yearNum > currentYear) {
            showError('year', `El año debe estar entre 1900 y ${currentYear}`);
            isValid = false;
        }
    }

    return isValid;
}

function showError(fieldId, message) {
    const field = document.getElementById(fieldId);
    const errorDiv = document.getElementById(fieldId + '-error');

    if (field) {
        field.classList.add('is-invalid');
    }
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    }
}

function clearAllErrors() {
    const errorDivs = document.querySelectorAll('.error-text');
    const invalidFields = document.querySelectorAll('.is-invalid');

    errorDivs.forEach(div => {
        div.style.display = 'none';
        div.textContent = '';
    });

    invalidFields.forEach(field => {
        field.classList.remove('is-invalid');
    });
}

// Limpiar errores cuando el usuario empiece a escribir
document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('input, select, textarea');
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            this.classList.remove('is-invalid');
            const errorDiv = document.getElementById(this.id + '-error');
            if (errorDiv) {
                errorDiv.style.display = 'none';
            }
        });
    });
});