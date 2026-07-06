package M6FGR.watut_compat.api.animation.types;

import M6FGR.watut_compat.api.animation.InteractionAnimationProperty;
import M6FGR.watut_compat.gameassets.WatutAnimations.AnimationModifiers;
import M6FGR.watut_compat.gameassets.WatutAnimations.ReusableEvents;
import com.corosus.watut.PlayerStatus;
import com.corosus.watut.PlayerStatusManager;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationEvent.Side;
import yesman.epicfight.api.animation.property.AnimationEvent.SimpleEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.StaticAnimationProperty;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.Layer.LayerType;
import yesman.epicfight.api.client.animation.Layer.Priority;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.JointMaskEntry;
import yesman.epicfight.api.client.input.PlayerInputState;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class InteractionAnimation extends StaticAnimation {
    public InteractionAnimation(float transitionTime, AnimationAccessor<? extends InteractionAnimation> animation, AssetAccessor<? extends Armature> armature) {
        this(transitionTime, true, false, animation, armature);
    }

    public InteractionAnimation(float transitionTime, boolean repeatable, AnimationAccessor<? extends InteractionAnimation> animation, AssetAccessor<? extends Armature> armature) {
        this(transitionTime, repeatable, false, animation, armature);
    }


    public InteractionAnimation(float transitionTime, boolean repeatable, boolean rightHandModify, AnimationAccessor<? extends InteractionAnimation> animation, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, repeatable, animation, armature);
        if (rightHandModify) this.addProperty(StaticAnimationProperty.POSE_MODIFIER, AnimationModifiers.LOOK_AT_CAMERA_DIRECTION);
        this.addProperty(InteractionAnimationProperty.FOLLOW_ENTITY_LOOK, false);
        this.addProperty(ClientAnimationProperties.PRIORITY, Priority.MIDDLE);
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


    @Override
    public float getPlaySpeed(LivingEntityPatch<?> entitypatch, DynamicAnimation animation) {
        if (this.properties.containsKey(InteractionAnimationProperty.PLAY_SPEED)) {
            return this.getProperty(InteractionAnimationProperty.PLAY_SPEED).orElse(1.0F);
        }
        return super.getPlaySpeed(entitypatch, animation);
    }

    @Override
    public boolean doesHeadRotFollowEntityHead() {
        return this.getProperty(InteractionAnimationProperty.FOLLOW_ENTITY_LOOK).orElse(false);
    }

}
