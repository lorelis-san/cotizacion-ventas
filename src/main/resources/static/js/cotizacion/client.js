document.addEventListener('DOMContentLoaded', () => {
    const tipoDoc = document.getElementById('tipoDoc');
    const camposDni = document.getElementById('camposDni');
    const camposRuc = document.getElementById('camposRuc');
    const clienteDatos = document.getElementById('clienteDatos');
    const clienteNuevo = document.getElementById('clienteNuevo');
    const grupoNombreApellido = document.getElementById('grupoNombreApellido');
    const grupoRazonSocial = document.getElementById('grupoRazonSocial');

    function toggleCamposPorTipo() {
        const tipo = tipoDoc.value;

        if (tipo === 'DNI') {
            camposDni.classList.remove('hidden');
            camposRuc.classList.add('hidden');
        } else if (tipo === 'RUC') {
            camposDni.classList.add('hidden');
            camposRuc.classList.remove('hidden');
        } else {
            camposDni.classList.add('hidden');
            camposRuc.classList.add('hidden');
        }
    }

    tipoDoc.addEventListener('change', toggleCamposPorTipo);
    toggleCamposPorTipo();

    function ocultarCamposCliente() {
        clienteDatos.classList.add("hidden");
        clienteNuevo.classList.add("hidden");
        camposDni.classList.add("hidden");
        camposRuc.classList.add("hidden");
        grupoNombreApellido.classList.add("hidden");
        grupoRazonSocial.classList.add("hidden");
    }

    document.getElementById("tipoDoc").addEventListener("change", function () {
        document.getElementById("numDoc").value = "";
        ocultarCamposCliente();
    });

    window.buscarCliente = async function () {
        const tipo = tipoDoc.value;
        const numero = document.getElementById("numDoc").value.trim();

        if (!tipo || !numero) {
            alert("Debe seleccionar tipo y número de documento.");
            return;
        }

        try {
            const response = await fetch(`api/client/documento/${numero}`);

            if (!response.ok) {
                throw new Error("Cliente no encontrado");
            }

            const data = await response.json();

            clienteDatos.classList.remove("hidden");
            clienteNuevo.classList.add("hidden");

            // Mostrar datos según tipo de documento
            document.getElementById("clienteId").value = data.id || "";
            if (tipo === "RUC") {
                grupoNombreApellido.classList.add("hidden");
                grupoRazonSocial.classList.remove("hidden");

                document.getElementById("clienteRazonSocial").value = data.businessName || "";
                document.getElementById("clienteNombre").value = "";
                document.getElementById("clienteApellido").value = "";
            } else {
                grupoNombreApellido.classList.remove("hidden");
                grupoRazonSocial.classList.add("hidden");

                document.getElementById("clienteNombre").value = data.firstName || "";
                document.getElementById("clienteApellido").value = data.lastName || "";
                document.getElementById("clienteRazonSocial").value = "";
            }

            document.getElementById("clienteCorreo").value = data.email || "";
            document.getElementById("clienteCelular").value = data.phoneNumber || "";

        } catch (err) {
            clienteDatos.classList.add("hidden");
            clienteNuevo.classList.remove("hidden");

            if (tipo === "DNI") {
                camposDni.classList.remove("hidden");
                camposRuc.classList.add("hidden");
            } else if (tipo === "RUC") {
                camposRuc.classList.remove("hidden");
                camposDni.classList.add("hidden");
            }
        }
    };

    window.agregarCliente = async function () {
        const tipo = tipoDoc.value;
        const numero = document.getElementById("numDoc").value.trim();
        const correo = document.getElementById("nuevoCorreo").value.trim();
        const celular = document.getElementById("nuevoCelular").value.trim();

        let cliente = {
            typeDocument: tipo.toUpperCase(),
            documentNumber: numero,
            email: correo,
            phoneNumber: celular
        };

        if (tipo === "DNI") {
            cliente.firstName = document.getElementById("nuevoNombre").value.trim();
            cliente.lastName = document.getElementById("nuevoApellido").value.trim();
        } else if (tipo === "RUC") {
            cliente.businessName = document.getElementById("nuevaRazonSocial").value.trim();
        }

        console.log("Enviando cliente:", cliente);

        try {
            const response = await fetch("api/client/guardarClient", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(cliente)
            });

            if (!response.ok) {
                throw new Error(await response.text());
            }

            const data = await response.json();
            alert("Cliente registrado correctamente.");

            clienteDatos.classList.remove("hidden");
            clienteNuevo.classList.add("hidden");

            if (tipo === "RUC") {
                grupoNombreApellido.classList.add("hidden");
                grupoRazonSocial.classList.remove("hidden");

                document.getElementById("clienteRazonSocial").value = data.businessName || "";
                document.getElementById("clienteNombre").value = "";
                document.getElementById("clienteApellido").value = "";
            } else {
                grupoNombreApellido.classList.remove("hidden");
                grupoRazonSocial.classList.add("hidden");

                document.getElementById("clienteNombre").value = data.firstName || "";
                document.getElementById("clienteApellido").value = data.lastName || "";
                document.getElementById("clienteRazonSocial").value = "";
            }

            document.getElementById("clienteCorreo").value = data.email || "";
            document.getElementById("clienteCelular").value = data.phoneNumber || "";

        } catch (error) {
            console.error("Error al guardar cliente:", error);
            alert("Error al guardar cliente:\n" + error.message);
        }
    };
});
