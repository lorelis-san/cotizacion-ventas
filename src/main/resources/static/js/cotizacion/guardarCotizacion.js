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
    alert("Cotización guardada con éxito");
    // Redirigir o limpiar
  } else {
    alert("Error al guardar la cotización");
  }
}
