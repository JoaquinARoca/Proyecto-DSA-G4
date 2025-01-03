package dsa.proyecto.G4;

import dsa.proyecto.G4.models.Question;

public interface QuestionManager {
    Question addQuestion(Question q);

    Question addQuestion(String title, String date, String message, String sender);

    int countQuestions();
}
