package view;

import javafx.scene.image.Image;

import java.nio.file.Paths;

class Images {
    public static Image loadImage(String relPath) {
        String path = "../" + relPath;
        return new Image(String.valueOf(Images.class.getResource(Paths.get(path).toString())));
    }
}