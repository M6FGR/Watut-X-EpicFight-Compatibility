package M6FGR.watut_compat.gameassets;

import M6FGR.watut_compat.api.animation.InteractionAnimationProperty;
import M6FGR.watut_compat.api.animation.types.InteractionAnimation;
import M6FGR.watut_compat.main.WatutCompat;
import net.minecraft.util.Mth;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.AnimationManager.AnimationBuilder;
import yesman.epicfight.api.animation.AnimationManager.AnimationRegistryEvent;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.Pose.LoadOperation;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.StaticAnimationProperty;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.Layer.Priority;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.Armatures.ArmatureAccessor;
import yesman.epicfight.model.armature.HumanoidArmature;

public class WatutAnimations {

    public static AnimationAccessor<InteractionAnimation> FOCUSED_GENERAL;
    public static AnimationAccessor<InteractionAnimation> PRESS;
    public static AnimationAccessor<InteractionAnimation> IDLE_STATIC;
    public static AnimationAccessor<InteractionAnimation> CHAT_TYPING;


    public static void registerAnimations(AnimationRegistryEvent event) {
        event.newBuilder(WatutCompat.MODID, WatutAnimations::build);
    }

    private static void build(AnimationBuilder builder) {
        ArmatureAccessor<HumanoidArmature> biped = Armatures.BIPED;
        FOCUSED_GENERAL = builder.nextAccessor(livingAnimation("focused_general"), accessor ->
                new InteractionAnimation(0.15F, true, accessor, biped)
                        .addProperty(StaticAnimationProperty.FIXED_HEAD_ROTATION, false)
                        .addProperty(ClientAnimationProperties.PRIORITY, Priority.MIDDLE)
        );
        CHAT_TYPING = builder.nextAccessor(livingAnimation("chat_typing"), accessor ->
                new InteractionAnimation(0.05F, accessor, biped)
        );
        PRESS = builder.nextAccessor(livingAnimation("press"), accessor ->
                new InteractionAnimation(0.05F, false, accessor, biped)
                        .addProperty(InteractionAnimationProperty.PLAY_SPEED, 1.2F)
                        .addProperty(StaticAnimationProperty.PLAY_SPEED_MODIFIER, AnimationModifiers.FADE_ANIMATION_END)
        );
        IDLE_STATIC = builder.nextAccessor(livingAnimation("idling"), accessor ->
                new InteractionAnimation(0.4F, accessor, biped)
        );
    }

    public static class ReusableEvents {
        public static AnimationEvent.E1<AnimationAccessor<? extends StaticAnimation>> PLAY_ANIMATION = (entityPatch, accessor, params) -> {
            entityPatch.playAnimationSynchronized(params.first(), 0.0F);
        };


    }

    public static class AnimationModifiers {
        public static final AnimationProperty.PlaybackSpeedModifier FADE_ANIMATION_END = (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> {
            if (self.isLinkAnimation()) {
                return 1.0F;
            }
            float totalTime = self.getTotalTime();
            if (totalTime <= 0.0F) return 0.0F;
            float progress = elapsedTime / totalTime;
            float clampedProgress = Math.min(progress, 1.0F);
            return 1.0F - (clampedProgress * clampedProgress);
        };

        // copied from yesman.epicfight.gameasset.Animations#SPYGLASS_USE PoseModifier

        public static final AnimationProperty.PoseModifier LOOK_AT_CAMERA_DIRECTION = (self, pose, entitypatch, time, ticks) -> {
            if (entitypatch.isFirstPerson()) {
                pose.disableAllJoints();
            } else if (!self.isLinkAnimation()) {
                LivingMotion livingMotion = entitypatch.getCurrentLivingMotion();
                Pose rawPose;
                if (livingMotion != LivingMotions.SWIM && livingMotion != LivingMotions.FLY && livingMotion != LivingMotions.CREATIVE_FLY) {
                    float xRot = Mth.clamp((entitypatch.getOriginal().getXRot() + 90.0F) * 0.016666668F, 0.0F, 3.0F);
                    rawPose = self.getRawPose(xRot);
                    float f = 90.0F;
                    float ratio = (f - Math.abs(entitypatch.getOriginal().getXRot())) / f;
                    float yawOffset = entitypatch.getOriginal().getVehicle() != null ? entitypatch.getOriginal().getYHeadRot() : entitypatch.getOriginal().yBodyRot;
                    rawPose.get("Chest").frontResult(JointTransform.rotation(QuaternionUtils.YP.rotationDegrees(Mth.wrapDegrees(entitypatch.getOriginal().getYHeadRot() - yawOffset) * ratio)), OpenMatrix4f::mulAsOriginInverse);
                } else {
                    rawPose = self.getRawPose(3.3333F);
                }

                pose.load(rawPose, LoadOperation.OVERWRITE);
            }

        };
    }


    private static String livingAnimation(String name) {
        return "biped/living/" + name;
    }

}
