package org.progetto.Controller.Command;

public interface Command {
    boolean esegui();
    boolean undo();
    boolean redo();
}

