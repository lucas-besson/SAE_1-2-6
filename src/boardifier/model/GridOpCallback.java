package boardifier.model;

@FunctionalInterface
public interface GridOpCallback {
    void execute(GameElement element, GridElement gridDest, int rowDest, int colDest);
}
