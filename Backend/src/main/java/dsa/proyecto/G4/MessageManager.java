package dsa.proyecto.G4;

import dsa.proyecto.G4.models.Message;

import java.util.List;

public interface MessageManager {
    void addMsg(Message msg);

    List<Message> getAllMsg();

    Integer countmsg();
}
