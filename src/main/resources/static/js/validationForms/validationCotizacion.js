 const inputs = document.querySelectorAll('.no-spaces');
    inputs.forEach(input => {
        input.addEventListener('keydown', e => {
            if (e.key === ' ') e.preventDefault();
        });
        input.addEventListener('input', e => {
            e.target.value = e.target.value.replace(/\s/g, '');
        });
    });

    const soloNumerosInputs = document.querySelectorAll('.solo-numeros');
    soloNumerosInputs.forEach(input => {
        input.addEventListener('keydown', function (e) {
            const teclasPermitidas = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
            if (teclasPermitidas.includes(e.key)) return;
            if (!/^\d$/.test(e.key)) {
                e.preventDefault();
            }
        });
        input.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '');
        });
    });


function mostrarError(inputId, errorId, mensaje) {
    const input = document.getElementById(inputId);
    const errorDiv = document.getElementById(errorId);

    if (input && errorDiv) {
        input.classList.add("is-invalid");
        errorDiv.textContent = mensaje;
        errorDiv.style.display = "block";
    }
}

function limpiarError(inputId, errorId) {
    const input = document.getElementById(inputId);
    const errorDiv = document.getElementById(errorId);

    if (input && errorDiv) {
        input.classList.remove("is-invalid");
        errorDiv.textContent = "";
        errorDiv.style.display = "none";
    }
}

function validarDocumentoCliente() {
    const tipo = document.getElementById('tipoDoc').value;
    const numero = document.getElementById('numDoc').value.trim();
    limpiarError("numDoc", "errorNumDoc");

    if (!tipo) {
        mostrarError("numDoc", "errorNumDoc", "Seleccione el tipo de documento.");
        return false;
    }

    if (tipo === "DNI" && !/^\d{8}$/.test(numero)) {
        mostrarError("numDoc", "errorNumDoc", "El DNI debe tener exactamente 8 dígitos.");
        return false;
    }

    if (tipo === "RUC" && !/^\d{11}$/.test(numero)) {
        mostrarError("numDoc", "errorNumDoc", "El RUC debe tener exactamente 11 dígitos.");
        return false;
    }

    return true;
}

function validarAnioNuevo() {
    const anioInput = document.getElementById("anioNew");
    const anio = anioInput.value.trim();
    const currentYear = new Date().getFullYear();
    limpiarError("anioNew", "errorAnio");

    if (!/^\d{4}$/.test(anio) || anio < 1900 || anio > currentYear) {
        mostrarError("anioNew", "errorAnio", `Ingrese un año válido entre 1900 y ${currentYear}.`);
        return false;
    }

    return true;
}


function validarCamposRequeridos(selector) {
    let valid = true;
    const inputs = document.querySelectorAll(selector);

    inputs.forEach(input => {
        limpiarErrores(input);

        if (input.value.trim() === "") {
            mostrarError(input, "Este campo es obligatorio.");
            valid = false;
        }
    });

    return valid;
}
1
