package M6FGR.watut_compat.api.animation;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.StaticAnimationProperty;

public class InteractionAnimationProperty<T> extends StaticAnimationProperty<T> {

    public static InteractionAnimationProperty<Boolean> FOLLOW_ENTITY_LOOK = new InteractionAnimationProperty<>("follow_entity_look", Codec.BOOL);
    public static InteractionAnimationProperty<Float> PLAY_SPEED = new InteractionAnimationProperty<>("play_speed", Codec.FLOAT);

    public InteractionAnimationProperty(String name, @Nullable Codec<T> codecs) {
        super(name, codecs);
    }

    public InteractionAnimationProperty() {
        super();
    }
}
