package org.progetto.Model.Observer;

public interface LibreriaSubject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
}
