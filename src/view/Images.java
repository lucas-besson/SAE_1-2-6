package view;

import javafx.scene.image.Image;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Images {
    public static Image loadImage(String relPath) {

        Path path = Paths.get("..");
        path = path.resolve(relPath);

        return new Image(String.valueOf(Images.class.getResource(Paths.get(path.toString()).toString())));
    }
}