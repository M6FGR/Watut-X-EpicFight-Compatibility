package M6FGR.watut_compat.api.animation.types;

import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationEvent.Side;
import yesman.epicfight.api.animation.property.AnimationEvent.SimpleEvent;
import yesman.epicfight.api.animation.property.AnimationProperty.StaticAnimationProperty;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.Layer.LayerType;
import yesman.epicfight.api.client.animation.Layer.Priority;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.JointMaskEntry;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.gameasset.Animations;

public class InteractionAnimation extends StaticAnimation {
    private static final JointMaskEntry ROOT_UPPER_JOINTS = JointMaskEntry.builder().defaultMask(JointMaskEntry.BIPED_UPPER_JOINTS_WITH_ROOT).create();

    public InteractionAnimation(float transitionTime, AnimationAccessor<? extends InteractionAnimation> animation, AssetAccessor<? extends Armature> armature) {
        this(transitionTime, true, animation, armature);
    }

    public InteractionAnimation(float transitionTime, boolean repeatable, AnimationAccessor<? extends InteractionAnimation> animation, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, repeatable, animation, armature);
        // locking the head from rotating?
        this.addProperty(StaticAnimationProperty.FIXED_HEAD_ROTATION, true);
        // to override all animations
        this.addProperty(ClientAnimationProperties.PRIORITY, Priority.HIGHEST);
        // settings tool on the back
        this.addProperty(StaticAnimationProperty.ON_ITEM_CHANGE_EVENT,
                SimpleEvent.create(Animations.ReusableSources.SET_TOOLS_BACK_WHEN_ITEM_CHANGED, Side.CLIENT));
        this.addEvents(StaticAnimationProperty.ON_BEGIN_EVENTS,
                        SimpleEvent.create(Animations.ReusableSources.SET_TOOLS_BACK, Side.CLIENT),
                        SimpleEvent.create(Animations.ReusableSources.UPDATE_Y_TO_NEARBY_LADDER, Side.CLIENT));
        this.addEvents(StaticAnimationProperty.TICK_EVENTS,
                        SimpleEvent.create(Animations.ReusableSources.UPDATE_Y_TO_NEARBY_LADDER, Side.CLIENT));
        this.addEvents(StaticAnimationProperty.ON_END_EVENTS,
                        SimpleEvent.create(Animations.ReusableSources.REVERT_TO_HANDS, Side.CLIENT))
                .newTimePair(0.0F, Float.MAX_VALUE)
                .addStateRemoveOld(EntityState.COMBO_ATTACKS_DOABLE, false)
                .addStateRemoveOld(EntityState.SKILL_EXECUTABLE, false)
                .addStateRemoveOld(EntityState.TURNING_LOCKED, true)
                .addStateRemoveOld(EntityState.INACTION, true);
    }
}
