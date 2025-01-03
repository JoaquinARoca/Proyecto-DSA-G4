package dsa.proyecto.G4.services;

import dsa.proyecto.G4.QuestionManager;
import dsa.proyecto.G4.QuestionManagerImpl;
import dsa.proyecto.G4.models.Question;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/question", description = "Endpoint to User Service")
@Path("/question")
public class QuestionService {
    private QuestionManager questionManager;

    public QuestionService(){
        this.questionManager = QuestionManagerImpl.getInstance();
        if(questionManager.countQuestions()==0){
            this.questionManager.addQuestion(new Question("Minimo2Saco10?","2024-12-13 08:00:00","Hola Toni,Juan","Joaquin"));
        }
    }

    @POST
    @ApiOperation(value = "Crear un nuevo question", notes = "Crea un nuevo usuario con la información proporcionada")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Question.class),
            @ApiResponse(code = 500, message = "Error de validación")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newQuestion(Question question) {
        //cambios 4.7
        if (question == null ) {
            return Response.status(500).entity("Error de validación").build();
        }else {
            this.questionManager.addQuestion(question);
            return Response.status(201).entity(question).build();
        }
    }

}
