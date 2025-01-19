package dsa.proyecto.G4;
import dsa.proyecto.G4.models.Message;

import java.util.LinkedList;
import java.util.List;

public class MessageImpl implements MessageManager{
    private static MessageImpl instance;
    private List<Message> messageList;

    public static MessageImpl getInstance(){
        if(instance==null)
            instance=new MessageImpl();
        return instance;
    }

    @Override
    public void addMsg(Message msg){
        messageList.add(msg);
    }


    @Override
    public List<Message> getAllMsg(){
        return new LinkedList<>(messageList);
    }

    @Override
    public Integer countmsg(){
        return messageList.size();
    }
}
