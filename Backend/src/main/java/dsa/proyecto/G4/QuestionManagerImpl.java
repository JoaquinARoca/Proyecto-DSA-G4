package dsa.proyecto.G4;

import dsa.proyecto.G4.models.Question;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class QuestionManagerImpl implements QuestionManager{
    private static QuestionManagerImpl instance;
    private List<Question> questions;
    final static Logger logger = Logger.getLogger(QuestionManagerImpl.class);

    private QuestionManagerImpl(){this.questions = new LinkedList<>();
    }
    public static QuestionManagerImpl getInstance(){
        if(instance==null) {
            instance = new QuestionManagerImpl();
        }
        return instance;
    }
    @Override
    public Question addQuestion(Question q){
        questions.add(q);
        return q;
    }

    @Override
    public Question addQuestion(String title, String date, String message, String sender){
        logger.info("Titulo: " + title+
                "Fecha: " + date+
                "Mensaje: "+ message+
                "Remitente: "+sender);
        return this.addQuestion(new Question(title,date,message,sender));
    }

    @Override
    public int countQuestions(){
        return questions.size();
    }
//
//    @Override
//    public List<Question> getAllQuestions(){
//
//    }
}
