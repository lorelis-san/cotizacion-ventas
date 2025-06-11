async function guardarCotizacion() {
  const clienteId = document.getElementById('clienteId').value;
  const vehiculoId = document.getElementById('vehiculoId').value;
  const observaciones = document.getElementById('observaciones').value;

  const detalles = carrito.map(item => ({
    productoId: item.id,
    cantidad: item.cantidad,
    precioUnitario: item.precio
  }));

  const cotizacionDTO = {
    clienteId,
    vehiculoId,
    observaciones,
    detalles
  };
console.log("Cotización a enviar:", cotizacionDTO);

  const response = await fetch('/api/cotizaciones', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(cotizacionDTO)
  });

  if (response.ok) {
  const data = await response.json(); // Asegúrate que tu backend devuelva el DTO con el ID
      const id = data.id; // ← este valor lo usas para abrir el PDF

    alert("Cotización guardada con éxito");
    window.open(`/api/pdf/cotizacion/${id}`, '_blank');

    // Redirigir o limpiar
  } else {
    alert("Error al guardar la cotización");
  }


}
