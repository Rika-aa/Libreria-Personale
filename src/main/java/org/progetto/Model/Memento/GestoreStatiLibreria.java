package org.progetto.Model.Memento;

import java.util.LinkedList;

public class GestoreStatiLibreria { //corrisponde al caretaker del pattern memento

    private final StatoLibreria statoCorrente;
    private final LinkedList<Memento> undoList = new LinkedList<>();
    private final LinkedList<Memento> redoList = new LinkedList<>();

    private final int max = 20; //numero massimo di operazioni che possono essere salvate

    public GestoreStatiLibreria(StatoLibreria statoCorrente) {
        this.statoCorrente = statoCorrente;
        saveState(statoCorrente.save());
    }

    //salva un nuovo stato
    public void saveState(Memento memento) {
        undoList.add(memento);
        redoList.clear();
        //check del limite
        if(undoList.size() > max) {
            undoList.removeLast();
        }
    }

    public StatoLibreria getStatoCorrente() {
        return statoCorrente;
    }

    public boolean undo(){
        if(undoList.size() > 1){
            Memento memento = undoList.removeLast();
            redoList.addFirst(memento);

            Memento precedente = undoList.getLast();
            statoCorrente.restore(precedente);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if(!redoList.isEmpty()){
            Memento memento = redoList.removeLast();
            undoList.addFirst(memento);

            statoCorrente.restore(memento);

            //check limite
            if(undoList.size() > max){
                undoList.removeLast();
            }

            return true;
        }
        return false;
    }

    public void reset(){
        undoList.clear();
        redoList.clear();
    }

}
