    function selectImage() {
        console.log('Función selectImage llamada');
        document.getElementById('fileInput').click();
    }

    function handleFileSelect(event) {
        console.log('Función handleFileSelect llamada');
        const file = event.target.files[0];
        if (file && file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const previewImage = document.getElementById('previewImage');
                const noImagePlaceholder = document.getElementById('noImagePlaceholder');
                const fileInfo = document.getElementById('fileInfo');
                const fileName = document.getElementById('fileName');

                previewImage.src = e.target.result;
                previewImage.style.display = 'block';
                noImagePlaceholder.style.display = 'none';

                fileName.textContent = file.name;
                fileInfo.style.display = 'block';

                document.getElementById('urlInputSection').style.display = 'none';

                console.log('Archivo seleccionado:', file.name, 'Tamaño:', file.size);
            };
            reader.readAsDataURL(file);
        } else {
            alert('Por favor, selecciona un archivo de imagen válido.');
        }
    }

    function toggleUrlInput() {
        console.log('Función toggleUrlInput llamada');
        const urlSection = document.getElementById('urlInputSection');
        const fileInfo = document.getElementById('fileInfo');
        if (urlSection.style.display === 'none') {
            urlSection.style.display = 'flex';
            document.getElementById('fileInput').value = '';
            fileInfo.style.display = 'none';
        } else {
            urlSection.style.display = 'none';
        }
    }

    function changeImageFromUrl() {
        console.log('Función changeImageFromUrl llamada');
        const urlInput = document.getElementById('imageUrlInput');
        const previewImage = document.getElementById('previewImage');
        const noImagePlaceholder = document.getElementById('noImagePlaceholder');
        const currentImageUrl = document.getElementById('currentImageUrl');
        const fileInfo = document.getElementById('fileInfo');
        const newUrl = urlInput.value.trim();

        if (newUrl) {
            const testImage = new Image();
            testImage.onload = function() {
                previewImage.src = newUrl;
                previewImage.style.display = 'block';
                noImagePlaceholder.style.display = 'none';

                currentImageUrl.value = newUrl;

                document.getElementById('fileInput').value = '';
                fileInfo.style.display = 'none';

                console.log('URL de imagen cambiada a:', newUrl);
            };
            testImage.onerror = function() {
                alert('Error: No se pudo cargar la imagen. Verifica que la URL sea correcta.');
            };
            testImage.src = newUrl;
        } else {
            alert('Por favor, ingresa una URL válida.');
        }
    }

    function clearImage() {
        console.log('Función clearImage llamada');
        const previewImage = document.getElementById('previewImage');
        const noImagePlaceholder = document.getElementById('noImagePlaceholder');
        const fileInput = document.getElementById('fileInput');
        const fileInfo = document.getElementById('fileInfo');
        const urlInput = document.getElementById('imageUrlInput');
        const currentImageUrl = document.getElementById('currentImageUrl');

        fileInput.value = '';
        urlInput.value = '';
        currentImageUrl.value = '';
        fileInfo.style.display = 'none';

        previewImage.style.display = 'none';
        noImagePlaceholder.style.display = 'flex';

        console.log('Imagen limpiada');
    }

    document.addEventListener('DOMContentLoaded', function() {
        console.log('DOM cargado completamente');
        const previewImage = document.getElementById('previewImage');
        const noImagePlaceholder = document.getElementById('noImagePlaceholder');

        if (!previewImage.src || previewImage.src.includes('null') || previewImage.src === window.location.href) {
            previewImage.style.display = 'none';
            noImagePlaceholder.style.display = 'flex';
        }
    });