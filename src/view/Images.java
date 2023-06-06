package view;

import javafx.scene.image.Image;

class Images {
    public static Image loadImage(String relPath) {
        return new Image(String.valueOf(Images.class.getResource("../" + relPath)));
    }
}