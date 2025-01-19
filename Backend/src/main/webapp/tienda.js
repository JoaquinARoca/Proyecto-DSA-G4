document.addEventListener("DOMContentLoaded", async () => {
    const userId = localStorage.getItem("userId"); // Obtener el ID del usuario desde localStorage
    console.log("userId obtenido desde localStorage:", userId); // Log para verificar el userId

    const balanceSpan = document.getElementById("userBalance");
    const cartItems = document.getElementById("cartItems");
    const cartTotal = document.getElementById("cartTotal");
    console.log("Archivo tienda.js cargado correctamente");

    let cart = [];
    let userBalance = 0;

    // Verificar si el usuario está autenticado
    const isAuthenticated = !!userId;

    if (!isAuthenticated) {
        console.log("Usuario no autenticado: bloqueando funcionalidades."); // Log para usuarios no autenticados
        balanceSpan.textContent = "Usuario no autenticado";

        document.querySelectorAll(".btn").forEach(button => {
            button.disabled = true;
            button.style.cursor = "not-allowed";
            button.style.backgroundColor = "#d3d3d3";
        });

        const buyButton = document.querySelector(".buy-btn");
        if (buyButton) {
            buyButton.disabled = true;
            buyButton.style.cursor = "not-allowed";
            buyButton.style.backgroundColor = "#d3d3d3";
        }

        return;
    }

    async function loadBalance() {
        try {
            console.log(`Intentando cargar el saldo para userId: ${userId}`); // Log antes de la solicitud
            const response = await fetch(`http://localhost:8080/dsaApp/usuarios/${userId}/balance`);
            console.log("Respuesta del servidor:", response); // Log para verificar la respuesta del backend

            if (!response.ok) {
                throw new Error("Error en la solicitud: " + response.statusText);
            }

            const data = await response.json();
            console.log("Datos recibidos del servidor:", data); // Log para verificar los datos del backend

            userBalance = data.saldo;
            balanceSpan.textContent = `${data.saldo.toFixed(2)}`;
        } catch (error) {
            console.error("Error al cargar el saldo:", error); // Log para errores al cargar el saldo
            balanceSpan.textContent = "Error al cargar el saldo";
        }
    }

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

        userBalance -= productPrice;
        balanceSpan.textContent = `${userBalance.toFixed(2)}`;
        updateCart();
    };

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

    window.removeFromCart = (productName) => {
        const productIndex = cart.findIndex(item => item.name === productName);
        if (productIndex !== -1) {
            const product = cart[productIndex];
            userBalance += product.price * product.quantity;
            cart.splice(productIndex, 1);
            balanceSpan.textContent = `${userBalance.toFixed(2)}`;
            updateCart();
        }
    };

    async function purchase() {
        if (cart.length === 0) {
            alert("El carrito está vacío.");
            return;
        }

        // Formatear el carrito para enviarlo al backend
        const formattedCart = {};
        cart.forEach(item => {
            formattedCart[item.name] = item.quantity; // Usa el ID real si está disponible
        });

        console.log("Carrito enviado al backend:", formattedCart);

        try {
            const response = await fetch(`http://localhost:8080/dsaApp/usuarios/${userId}/purchase`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formattedCart),
            });

            if (!response.ok) {
                throw new Error("Error en la compra: " + response.statusText);
            }

            const data = await response.json();
            alert("Compra realizada con éxito.");
            userBalance = data.newBalance;
            cart = [];
            updateCart();
            await loadBalance();
        } catch (error) {
            console.error("Error al realizar la compra:", error);
            alert("Ocurrió un error al procesar la compra.");
        }
    }


    document.querySelectorAll(".btn").forEach(button => {
        button.addEventListener("click", function () {
            const productName = this.parentElement.querySelector("h2").textContent.trim();
            const productPrice = parseFloat(
                this.parentElement.querySelector("p").textContent.replace("Precio: $", "").trim()
            );

            addToCart(productName, productPrice);
        });
    });

    window.purchase = purchase;
});
