
     function toggleBusinessNameField() {
     const docType = document.getElementById("typeDocument").value;
     const businessNameField = document.getElementById("businessNameField");
     const firstNameField = document.getElementById("firstNameField").closest(".col-md-6");
     const lastNameField = document.getElementById("lastNameField").closest(".col-md-6");

     const businessNameInput = document.getElementById("businessName");
     const firstNameInput = document.getElementById("firstName");
     const lastNameInput = document.getElementById("lastName");

     if (docType === "RUC") {
         businessNameField.style.display = "block";
         businessNameInput.setAttribute("required", "required");

         firstNameField.style.display = "none";
         lastNameField.style.display = "none";
         firstNameInput.removeAttribute("required");
         lastNameInput.removeAttribute("required");
         clearError("firstNameError");
         clearError("lastNameError");
     } else {
         businessNameField.style.display = "none";
         businessNameInput.removeAttribute("required");
         clearError("businessNameError");

         firstNameField.style.display = "block";
         lastNameField.style.display = "block";
         firstNameInput.setAttribute("required", "required");
         lastNameInput.setAttribute("required", "required");
     }

     clearError("documentNumberError");
 }


         function clearError(id) {
             const errorElem = document.getElementById(id);
             if (errorElem) {
                 errorElem.textContent = "";
             }
         }

         function showError(id, msg) {
             const errorElem = document.getElementById(id);
             if (errorElem) {
                 errorElem.textContent = msg;
             }
         }

         function validateForm(event) {
             let valid = true;
             const docType = document.getElementById("typeDocument").value;
             const documentNumber = document.getElementById("documentNumber").value.trim();
             const firstName = document.getElementById("firstName").value.trim();
             const lastName = document.getElementById("lastName").value.trim();
             const businessName = document.getElementById("businessName").value.trim();
             const phoneNumber = document.getElementById("phoneNumber").value.trim();

             clearError("documentNumberError");
             clearError("businessNameError");
             clearError("firstNameError");
             clearError("lastNameError");
             clearError("phoneNumberError");

             if (docType === "DNI") {
                 if (!/^\d{8}$/.test(documentNumber)) {
                     showError("documentNumberError", "El DNI debe tener exactamente 8 dígitos numéricos.");
                     valid = false;
                 }
                 if (firstName.length === 0) {
                     showError("firstNameError", "El nombre es obligatorio.");
                     valid = false;
                 }
                 if (lastName.length === 0) {
                     showError("lastNameError", "El apellido es obligatorio.");
                     valid = false;
                 }
             } else if (docType === "RUC") {
                 if (!/^\d{11}$/.test(documentNumber)) {
                     showError("documentNumberError", "El RUC debe tener exactamente 11 dígitos numéricos.");
                     valid = false;
                 }
                 if (businessName.length === 0) {
                     showError("businessNameError", "La razón social es obligatoria para RUC.");
                     valid = false;
                 }
             }

             if (!/^\d{9,}$/.test(phoneNumber)) {
                 showError("phoneNumberError", "El número de teléfono debe tener 9 dígitos.");
                 valid = false;
             }

             if (!valid) {
                 event.preventDefault();
             }
         }




        document.addEventListener("DOMContentLoaded", function () {
        toggleBusinessNameField();
        document.getElementById("clientForm").addEventListener("submit", validateForm);
        document.getElementById("typeDocument").addEventListener("change", toggleBusinessNameField);
});
