const form = document.getElementById('registerForm');
    const messageDiv = document.getElementById('message');
    const passwordInput = document.getElementById('password');
    const strengthDiv = document.getElementById('passwordStrength');

    // Password strength checker
    passwordInput.addEventListener('input', function() {
        const password = this.value;
        let strength = 0;
        let text = '';
        let className = '';

        if (password.length >= 8) strength++;
        if (/[a-z]/.test(password)) strength++;
        if (/[A-Z]/.test(password)) strength++;
        if (/[0-9]/.test(password)) strength++;
        if (/[^A-Za-z0-9]/.test(password)) strength++;

        switch (strength) {
            case 0:
            case 1:
            case 2:
                text = 'Contraseña débil';
                className = 'strength-weak';
                break;
            case 3:
            case 4:
                text = 'Contraseña media';
                className = 'strength-medium';
                break;
            case 5:
                text = 'Contraseña fuerte';
                className = 'strength-strong';
                break;
        }

        strengthDiv.textContent = password ? text : '';
        strengthDiv.className = `password-strength ${className}`;
    });

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const submitBtn = document.getElementById('registerBtn');
        const btnText = submitBtn.querySelector('.btn-text');
        const originalText = btnText.textContent;

        // Add loading state
        submitBtn.disabled = true;
        btnText.innerHTML = '<span class="loading-spinner"></span>Registrando...';

        const data = {
            nombre: document.getElementById('nombre').value.trim(),
            apellido: document.getElementById('apellido').value.trim(),
            username: document.getElementById('username').value.trim(),
            email: document.getElementById('email').value.trim(),
            password: document.getElementById('password').value.trim()
        };

        try {
            const response = await fetch('/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                messageDiv.className = 'alert alert-success';
                messageDiv.textContent = '¡Registro exitoso! Puedes iniciar sesión.';
                messageDiv.style.display = 'block';
                form.reset();
                strengthDiv.textContent = '';

                // Optional: Redirect to login after 2 seconds
                setTimeout(() => {
                    // window.location.href = 'login.html';
                }, 2000);
            } else {
                const errorData = await response.json();
                messageDiv.className = 'alert alert-danger';
                messageDiv.textContent = errorData.message || 'Error en el registro';
                messageDiv.style.display = 'block';
            }

            resetButton();
        } catch (error) {
            messageDiv.className = 'alert alert-danger';
            messageDiv.textContent = 'Error de conexión al servidor.';
            messageDiv.style.display = 'block';
            resetButton();
        }

        function resetButton() {
            submitBtn.disabled = false;
            btnText.textContent = originalText;
        }

        // Hide message after 5 seconds
        setTimeout(() => {
            messageDiv.style.display = 'none';
        }, 5000);
    });

    // Input focus effects
    const inputs = document.querySelectorAll('.form-control');
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.style.transform = 'translateY(-1px)';
        });

        input.addEventListener('blur', function() {
            this.parentElement.style.transform = 'translateY(0)';
        });
    });