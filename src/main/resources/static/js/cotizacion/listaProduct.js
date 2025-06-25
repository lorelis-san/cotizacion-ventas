
    // Función para cargar los datos en el modal cuando se hace clic en el botón de eliminar
    function cargarDatosEliminar(button) {
        const id = button.getAttribute('data-id');
        const numero = button.getAttribute('data-numero');

        // Mostrar el código del producto en el modal
        document.getElementById('codEliminar').textContent = numero;

        // Guardar el ID en el botón de confirmación para usarlo después
        document.getElementById('confirmarEliminarBtn').setAttribute('data-id', id);
    }

    // Esperar que el DOM cargue completamente
    document.addEventListener('DOMContentLoaded', () => {
        const confirmarBtn = document.getElementById('confirmarEliminarBtn');

        confirmarBtn.addEventListener('click', () => {
            const id = confirmarBtn.getAttribute('data-id');

            // Confirmar eliminación con llamada fetch
            fetch(` /delete/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.ok) {
                    // Cerrar el modal manualmente
                    const modal = bootstrap.Modal.getInstance(document.getElementById('eliminarProductoModal'));
                    modal.hide();

                    // Recargar la página o quitar el producto del DOM
                    location.reload(); // o actualiza dinámicamente la tabla si lo prefieres
                } else {
                    return response.text().then(text => {
                        throw new Error(text || 'Error al eliminar el producto.');
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('No se pudo eliminar el producto. Intenta nuevamente.');
            });
        });
    });
