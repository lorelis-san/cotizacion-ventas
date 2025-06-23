 // Efecto de cursor personalizado


    // Animación de entrada retardada para elementos
    window.addEventListener('load', function() {
        const elements = document.querySelectorAll('.error-code, .error-title, .error-message, .home-button');
        elements.forEach((el, index) => {
            el.style.opacity = '0';
            el.style.transform = 'translateY(20px)';
            setTimeout(() => {
                el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                el.style.opacity = '1';
                el.style.transform = 'translateY(0)';
            }, index * 200 + 500);
        });
    });