package dsa.proyecto.G4.services;
import dsa.proyecto.G4.ProductManagerImpl;
import dsa.proyecto.G4.UserManager;
import dsa.proyecto.G4.UserManagerImpl;
import dsa.proyecto.G4.db.orm.dao.*;
import dsa.proyecto.G4.models.Product;
import dsa.proyecto.G4.models.Purchase;
import dsa.proyecto.G4.models.User;
import dsa.proyecto.G4.util.RandomUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Api(value = "/usuarios", description = "Endpoint to User Service")
@Path("/usuarios")
public class UserService {
    private UserManager userManager;
    private IProductDAO productdbU;
    private IUserDAO userdb;
    private IPurchaseDAO purchasedb;

    public UserService() {
        this.userManager = UserManagerImpl.getInstance();
        this.userdb = UserDAOImpl.getInstance();
        this.purchasedb = PurchaseDAOImpl.getInstance();
        this.productdbU = ProductDAOImpl.getInstance();
        // Datos de ejemplo
        if (userManager.countUsers()==0) {
            this.userManager.addUsuarios(this.userdb.getUsers());
        }
    }


    @GET
    @ApiOperation(value = "Obtener todos los usuarios", notes = "Devuelve una lista de todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = User.class, responseContainer = "List"),
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios() {
        List<User> usuarios = this.userManager.getUsuarios();
        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(usuarios) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "Crear un nuevo usuario", notes = "Crea un nuevo usuario con la información proporcionada")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class),
            @ApiResponse(code=406,message = "Existe un usuario con ese nombre"),
            @ApiResponse(code = 500, message = "Error de validación")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUsuario(User usuario) {
        //cambios 4.7
        if (usuario.getNombre() == null || usuario.getContraseña() == null) {
            return Response.status(500).entity("Error de validación").build();
        } else if (this.userManager.getUsuarioPorNombre(usuario.getNombre())!=null) {
            return Response.status(406).entity("Existe un usuario con ese nombre").build();
        } else {
            usuario.setSaldo(100);
            User repetido = this.userManager.getUsuarioPorId(usuario.getId());
            while(repetido!=null){
                usuario.setId(RandomUtils.getId());
                repetido = this.userManager.getUsuarioPorId(usuario.getId());
            }
            this.userManager.addUsuario(usuario);
            this.userdb.addUser(usuario.getId(),usuario.getNombre(),usuario.getContraseña(),100,usuario.getPerfil());
            return Response.status(201).entity(usuario).build();
        }
    }

    @POST
    @Path("/login")
    @ApiOperation(value = "Iniciar sesión", notes = "Valida las credenciales del usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login exitoso"),
            @ApiResponse(code = 400, message = "Credenciales incompletas"),
            @ApiResponse(code = 401, message = "Credenciales incorrectas"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User credentials) {
        try {
            // Validar que se hayan proporcionado credenciales
            if (credentials == null || credentials.getNombre() == null || credentials.getContraseña() == null) {
                return Response.status(400).entity("Credenciales incompletas").build();
            }

            // Buscar el usuario por nombre
            User user = this.userManager.getUsuarioPorNombre(credentials.getNombre());

            // Verificar si el usuario existe y las contraseñas coinciden
            if (user == null) {
                return Response.status(401).entity("Usuario no encontrado").build();
            }

            if (!user.getContraseña().equals(credentials.getContraseña().trim())) {
                return Response.status(401).entity("Contraseña incorrecta").build();
            }

            // Login exitoso
            return Response.status(200).entity(user).build();
        } catch (Exception e) {
            // Manejar cualquier error inesperado
            e.printStackTrace();
            return Response.status(500).entity("Error interno del servidor").build();
        }
    }


    @PUT
    @ApiOperation(value= "update a User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code=201,message = "Successful"),
            @ApiResponse(code=409,message = "Couldn't update user"),
            @ApiResponse(code=404, message = "User not found")
    })
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") String id, User user){//cambios 4.7
        User u = this.userManager.updateUser(id,user);
        if(u == null) return Response.status(404).build();

        try {
            this.userdb.updateUser(u.getId(),u.getNombre(),u.getContraseña(),u.getSaldo(),u.getPerfil());
        } catch (SQLException e) {
            return Response.status(409).build();
        }
        return Response.status(201).entity(u).build();
    }

    @GET
    @Path("/{id}/purchase")
    @ApiOperation(value = "Obtener productos comprados por un usuario", notes = "Devuelve una lista de productos comprados por el usuario con el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Purchase.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Usuario no encontrado o sin compras"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPurchasesByUserId(@PathParam("id") String userId) {
        try {
            // Obtener las compras realizadas por el usuario
            List<Purchase> purchases = this.purchasedb.getCompras(userId);

            if (purchases == null || purchases.isEmpty()) {
                return Response.status(404).entity("El usuario no tiene compras registradas o no existe").build();
            }
            purchases = this.userManager.ordenaInventario(purchases);
            // Convertir a una entidad genérica para devolver como JSON
            GenericEntity<List<Purchase>> entity = new GenericEntity<List<Purchase>>(purchases) {};
            return Response.status(200).entity(entity).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error interno del servidor").build();
        }
    }

    @POST
    @Path("/{id}/purchase")
    @ApiOperation(value = "Registrar nuevas compras", notes = "Deduce saldo y registra múltiples compras para el usuario con el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Compras registradas exitosamente"),
            @ApiResponse(code = 400, message = "Datos de compra incompletos o inválidos"),
            @ApiResponse(code = 404, message = "Usuario no encontrado"),
            @ApiResponse(code = 403, message = "Saldo insuficiente para realizar las compras"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPurchase(@PathParam("id") String userId, HashMap<String, Integer> cart) {
        try {
            // Log para inspeccionar el tipo de contenido recibido
            System.out.println("Recibiendo solicitud para agregar compras.");
            System.out.println("Carrito recibido: " + cart);

            if (cart == null || cart.isEmpty()) {
                System.out.println("El carrito está vacío o no fue recibido correctamente.");
                return Response.status(400).entity("El carrito está vacío.").build();
            }

            // Validar si el usuario existe
            User user = this.userManager.getUsuarioPorId(userId);
            if (user == null) {
                System.out.println("Usuario no encontrado para ID: " + userId);
                return Response.status(404).entity("Usuario no encontrado.").build();
            }

            int totalCost = 0;

            // Calcular el costo total del carrito
            for (String productId : cart.keySet()) {
                int cantidad = cart.get(productId);

                // Validar cantidad
                if (cantidad <= 0) {
                    System.out.println("Cantidad inválida para el producto: " + productId);
                    return Response.status(400).entity("Cantidad inválida para el producto: " + productId).build();
                }

                // Validar producto existente
                Product product = this.productdbU.getProduct(productId);
                if (product == null) {
                    System.out.println("Producto inválido en el carrito: " + productId);
                    return Response.status(400).entity("Producto inválido en el carrito: " + productId).build();
                }

                totalCost += product.getPrecio() * cantidad;
            }

            // Validar saldo suficiente
            if (user.getSaldo() < totalCost) {
                System.out.println("Saldo insuficiente para realizar la compra. Saldo disponible: "
                        + user.getSaldo() + ", Costo total: " + totalCost);
                return Response.status(403).entity("Saldo insuficiente.").build();
            }

            // Deduce el saldo del usuario
            user.setSaldo(user.getSaldo() - totalCost);
            userManager.updateUser(userId, user);

            // Registrar las compras en la base de datos
            for (String productId : cart.keySet()) {
                int cantidad = cart.get(productId);
                purchasedb.addPurchase(userId, productId, cantidad);
            }

            // Retornar el nuevo saldo
            System.out.println("Compras registradas exitosamente. Nuevo saldo: " + user.getSaldo());
            return Response.status(201).entity("{\"newBalance\": " + user.getSaldo() + "}").build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error interno del servidor: " + e.getMessage());
            return Response.status(500).entity("Error interno del servidor").build();
        }
    }



    @GET
    @Path("/{id}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Obtener el saldo de un usuario", notes = "Devuelve el saldo actual del usuario con el ID proporcionado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Saldo obtenido exitosamente"),
            @ApiResponse(code = 404, message = "Usuario no encontrado"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    public Response getUserBalance(@PathParam("id") String userId) {
        try {
            User user = this.userManager.getUsuarioPorId(userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
            }

            // Devolver el saldo como un objeto JSON básico
            String jsonResponse = "{\"saldo\": " + user.getSaldo() + "}";
            return Response.ok(jsonResponse).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();
        }
    }



}
