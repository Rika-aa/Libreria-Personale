package org.progetto.Model.Memento;

import org.progetto.Model.GestoreLibreria;
import org.progetto.Model.Singleton.LibreriaSingleton;

import java.util.LinkedList;

public class GestoreStatiLibreria { //corrisponde al caretaker del pattern memento

    private final GestoreLibreria originator;
    private final LinkedList<Memento> undoList = new LinkedList<>();
    private final LinkedList<Memento> redoList = new LinkedList<>();

    private final int max = 20; //numero massimo di operazioni che possono essere salvate

    public GestoreStatiLibreria(GestoreLibreria originator) {
        this.originator = originator;
        saveState(originator.save()); //salvo lo stato iniziale dell'originator
    }

    //salva un nuovo stato
    public void saveState(Memento memento) {
        undoList.add(memento);//nuovo stato in coda
        redoList.clear();//per ogni nuova operazione cancello redo
        //check del limite
        if(undoList.size() > max) {
            undoList.removeFirst();
        }
    }

    public boolean undo(){
        if(undoList.size() > 1){
            Memento memento = undoList.removeLast();
            redoList.addFirst(memento);

            Memento precedente = undoList.getLast();
            originator.restore(precedente);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if(!redoList.isEmpty()){
            Memento memento = redoList.removeFirst();
            undoList.add(memento);

            originator.restore(memento);

            //check limite
            if(undoList.size() > max){
                undoList.removeFirst();
            }
            return true;
        }
        return false;
    }

    public void reset(){
        undoList.clear();
        redoList.clear();
        saveState(originator.save());
    }

}
