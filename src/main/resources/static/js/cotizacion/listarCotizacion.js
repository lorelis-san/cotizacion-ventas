$.fn.dataTable.ext.errMode = 'none';
$(document).ready(function () {
                    $('.table').DataTable({
                        "language": {
                            "url": "//cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json"
                        }
                    });
                });

                function cargarDatosEliminar(button) {
                    const id = button.getAttribute("data-id");
                    const numero = button.getAttribute("data-numero");
                    document.getElementById("numeroCotizacionEliminar").textContent = numero;

                    document.getElementById("confirmarEliminarBtn").onclick = function () {
                        eliminarCotizacion(id);
                    };
                }

                function eliminarCotizacion(id) {
                    fetch(`/cotizaciones/${id}`, {
                        method: 'DELETE'
                    }).then(response => {
                        if (response.ok) {

                            location.reload();
                        } else {
                            alert('Error al eliminar la cotización');
                        }
                    });
                }