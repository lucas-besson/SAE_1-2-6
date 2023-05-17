package boardifier.view;

import boardifier.model.GameElement;
import boardifier.model.SpriteElement;

public abstract class SpriteLook extends ElementLook {

    public SpriteLook(GameElement element) {
        super(element);
    }

    @Override
    public void onChange() {
        updateFace();
    }

    public abstract void updateFace();
}
