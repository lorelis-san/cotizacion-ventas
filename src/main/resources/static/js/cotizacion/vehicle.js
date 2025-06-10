async function buscarVehiculo() {
    const placa = document.getElementById('placaInput').value.trim();
    if (!placa) return alert("Ingrese una placa");

    // Oculta todo al comenzar
    ocultarSecciones();

    try {
        const response = await fetch(`/api/vehiculos/placa/${placa}`);

        if (!response.ok) {
            if (response.status === 404) {
                document.getElementById('noEncontrado').classList.remove('hidden');
                return;
            }
            const texto = await response.text();
            throw new Error(`Error HTTP ${response.status}: ${texto}`);
        }

        const vehiculo = await response.json();

        // Rellenar campos
        document.getElementById('marca').value = vehiculo.marca;
        document.getElementById('modelo').value = vehiculo.modelo;
        document.getElementById('anio').value = vehiculo.year;
        document.getElementById('vehiculoId').value = vehiculo.id;
        // Mostrar sección de datos
        document.getElementById('datosVehiculo').classList.remove('hidden');
    } catch (error) {
        console.error("Error inesperado al buscar vehículo:", error);
        alert("Error inesperado al buscar vehículo");
    }
}

function ocultarSecciones() {
    document.getElementById('datosVehiculo').classList.add('hidden');
    document.getElementById('noEncontrado').classList.add('hidden');
}

async function agregarVehiculo() {
    const placa = document.getElementById('placaInput').value.trim();
    const marca = document.getElementById('nuevaMarca').value.trim();
    const modelo = document.getElementById('nuevoModelo').value.trim();
    const year = document.getElementById('anioNew').value.trim();

    if (!placa || !marca || !modelo || !year) {
        return alert("Complete todos los campos");
    }

    const nuevoVehiculo = { placa, marca, modelo, year };

    try {
        const response = await fetch('/api/vehiculos/guardarVehicle', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nuevoVehiculo),
        });

        if (response.ok) {
            alert("Vehículo agregado correctamente");
            await buscarVehiculo(); // Recargar y mostrar datos del nuevo vehículo
        } else {
            const texto = await response.text();
            throw new Error(`Error al agregar: ${texto}`);
        }
    } catch (error) {
        console.error("Error al agregar vehículo:", error);
        alert("Ocurrió un error al agregar el vehículo");
    }
}
