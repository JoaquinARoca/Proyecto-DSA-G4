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

    @GET
    @ApiOperation(value = "Obtener un usuario por id", notes = "Devuelve una lista de todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = User.class, responseContainer = "List"),
    })
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("id") String id) {
        User user = this.userManager.getUsuarioPorId(id);
        return Response.status(200).entity(user).build();
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
            @ApiResponse(code = 401, message = "Credenciales incorrectas")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User credentials) {
        // Busca el usuario por nombre
        User user = this.userManager.getUsuarioPorNombre(credentials.getNombre());

        // Verifica si el usuario existe y si la contraseña coincide
        if (user != null && user.getContraseña().equals(credentials.getContraseña())) {
            // Si la autenticación es exitosa
            return Response.status(200).entity(user).build();
        } else {
            // Si las credenciales son incorrectas
            return Response.status(401).entity("Credenciales incorrectas").build();
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
    public Response updateUser(@PathParam("id") String id, User user){
        User u = this.userManager.updateUser(id,user);
        if(u == null) return Response.status(404).build();

        try {
            this.userdb.updateUser(u.getId(),u.getNombre(),u.getContraseña(),u.getSaldo(),u.getPerfil());
        } catch (SQLException e) {
            return Response.status(409).build();
        }
        return Response.status(201).entity(u).build();
    }

    @PUT
    @ApiOperation(value= "update money", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code=201,message = "Successful"),
            @ApiResponse(code=409,message = "Couldn't update user"),
            @ApiResponse(code=404, message = "User not found")
    })
    @Path("/{id}/money")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserMoney(@PathParam("id") String id, User user){
        User antiguoSaldo = this.userManager.getUsuarioPorId(user.getId());
        user.setSaldo(antiguoSaldo.getSaldo()+user.getSaldo());

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
    @ApiOperation(value = "Registrar una nueva compra", notes = "Añade una nueva compra para el usuario con el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Compra registrada exitosamente"),
            @ApiResponse(code = 400, message = "Datos de compra incompletos o inválidos"),
            @ApiResponse(code = 404, message = "Usuario no encontrado"),
            @ApiResponse(code = 500, message = "Error interno del servidor"),
            @ApiResponse(code = 403,message = "Saldo insuficiente para realizar la compra")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPurchase(@PathParam("id") String userId, List<Purchase> purchases) {
        try {
            // Validar si el usuario existe
            User u = this.userManager.getUsuarioPorId(userId);
            if (u == null) {
                return Response.status(404).entity("Usuario no encontrado").build();
            }
            List<Product> products = this.productdbU.getProducts();
            for(Purchase purchase : purchases){
                // Validar que la compra tenga datos válidos
                if (purchase.getIdP() == null || purchase.getCantidad() <= 0) {
                    return Response.status(400).entity("Datos de compra incompletos o inválidos").build();
                }
            }

            int nuevoSaldo = this.userManager.calculaNuevoSaldo(userId,purchases,products);
            if(nuevoSaldo<0)
                return Response.status(403).entity("Saldo insuficiente").build();

            u.setSaldo(nuevoSaldo);
            this.userManager.updateUser(userId,u);
            try {
                this.userdb.updateUser(u.getId(),u.getNombre(),u.getContraseña(),u.getSaldo(),u.getPerfil());
            } catch (SQLException e) {
                return Response.status(409).build();
            }
            // Añadir las compras
            for(Purchase purchase: purchases)
                this.purchasedb.addPurchase(userId, purchase.getIdP(), purchase.getCantidad());

            // Retornar respuesta exitosa
            return Response.status(201).entity(purchases).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error interno del servidor").build();
            //cambio
        }
    }

}
