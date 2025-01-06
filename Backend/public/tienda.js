document.addEventListener("DOMContentLoaded", async () => {
    const userId = localStorage.getItem("userId"); // Obtener el ID del usuario desde localStorage
    const balanceSpan = document.getElementById("userBalance");
    const cartItems = document.getElementById("cartItems");
    const cartTotal = document.getElementById("cartTotal");

    if (!userId) {
        console.error("No se encontró el ID del usuario. Asegúrate de que el usuario haya iniciado sesión.");
        balanceSpan.textContent = "Error: Usuario no autenticado";
        return;
    }

    let cart = [];
    let userBalance = 0;

    // Función para cargar el saldo del usuario
    async function loadBalance() {
        if (!userId) {
            console.error("No se encontró el ID del usuario.");
            balanceSpan.textContent = "Error al cargar el saldo";
            return;
        }

        console.log("Cargando saldo para userId:", userId);

        try {
            const response = await fetch(`http://localhost:8080/dsaApp/usuarios/${userId}/balance`);
            console.log("Respuesta del servidor:", response);

            if (!response.ok) {
                throw new Error("Error en la solicitud: " + response.statusText);
            }

            const data = await response.json();
            console.log("Saldo recibido:", data);
            userBalance = data.saldo; // Actualizar el saldo
            balanceSpan.textContent = `${data.saldo.toFixed(2)}`;
        } catch (error) {
            console.error("Error al cargar el saldo:", error);
            balanceSpan.textContent = "Error al cargar el saldo";
        }
    }

    // Llamar a la función para cargar el saldo
    await loadBalance();

    // Función para añadir al carrito
    const addToCart = (productName, productPrice) => {
        if (userBalance < productPrice) {
            alert("Saldo insuficiente para comprar este producto.");
            return;
        }

        const existingProduct = cart.find(item => item.name === productName);
        if (existingProduct) {
            existingProduct.quantity += 1;
        } else {
            cart.push({ name: productName, price: productPrice, quantity: 1 });
        }

        updateCart();
    };

    // Actualizar el carrito
    const updateCart = () => {
        cartItems.innerHTML = "";
        let total = 0;

        cart.forEach(item => {
            total += item.price * item.quantity;

            const div = document.createElement("div");
            div.className = "cart-item";
            div.innerHTML = `
                ${item.name} - ${item.price.toFixed(2)} x ${item.quantity}
                <span class="remove-btn" onclick="removeFromCart('${item.name}')">Quitar</span>
            `;
            cartItems.appendChild(div);
        });

        cartTotal.textContent = `${total.toFixed(2)}`;
    };

    // Quitar producto del carrito
    window.removeFromCart = (productName) => {
        const productIndex = cart.findIndex(item => item.name === productName);
        if (productIndex !== -1) {
            const product = cart[productIndex];
            cart.splice(productIndex, 1);
            updateCart();
        }
    };

    // Función para realizar la compra
    async function purchase() {
        if (cart.length === 0) {
            alert("El carrito está vacío.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/dsaApp/usuarios/${userId}/purchase`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(cart), // Enviar el carrito como JSON
            });

            if (!response.ok) {
                throw new Error("Error en la compra: " + response.statusText);
            }

            const data = await response.json();
            alert("Compra realizada con éxito.");
            userBalance = data.newBalance; // Actualiza el saldo del usuario
            cart = []; // Vacía el carrito
            updateCart(); // Actualiza el carrito en la interfaz
            loadBalance(); // Recarga el saldo del usuario
        } catch (error) {
            console.error("Error al realizar la compra:", error);
            alert("Ocurrió un error al procesar la compra.");
        }
    }

    // Asignar eventos a los botones de productos
    document.querySelectorAll(".btn").forEach(button => {
        button.addEventListener("click", function () {
            const productName = this.parentElement.querySelector("h2").textContent.trim();
            const productPrice = parseFloat(
                this.parentElement.querySelector("p").textContent.replace("Precio: $", "").trim()
            );

            addToCart(productName, productPrice);
        });
    });

    // Hacer la función `purchase` disponible globalmente para el botón "Comprar"
    window.purchase = purchase;
});
