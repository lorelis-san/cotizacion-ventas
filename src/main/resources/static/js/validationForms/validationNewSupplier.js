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