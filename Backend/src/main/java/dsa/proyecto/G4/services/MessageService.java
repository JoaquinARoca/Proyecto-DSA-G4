package dsa.proyecto.G4.services;

import dsa.proyecto.G4.MessageImpl;
import dsa.proyecto.G4.MessageManager;
import dsa.proyecto.G4.models.Message;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class MessageService {
    private MessageManager messageManager;

    public MessageService(){
        this.messageManager = MessageImpl.getInstance();
        if(messageManager.countmsg()==0){
            this.messageManager.addMsg(new Message("Hola1"));
            this.messageManager.addMsg(new Message("Hola2"));
        }
    }

    @GET
    @ApiOperation(value = "Obtener todos los mensajes", notes = "Devuelve una lista de todos los mensajes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Message.class, responseContainer = "List"),
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        List<Message> mensajes = this.messageManager.getAllMsg();
        GenericEntity<List<Message>> entity = new GenericEntity<List<Message>>(mensajes) {};
        return Response.status(200).entity(entity).build();
    }
}
