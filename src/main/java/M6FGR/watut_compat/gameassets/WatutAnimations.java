package M6FGR.watut_compat.gameassets;

import M6FGR.watut_compat.api.animation.types.InteractionAnimation;
import M6FGR.watut_compat.main.WatutCompat;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.AnimationManager.AnimationBuilder;
import yesman.epicfight.api.animation.AnimationManager.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.Armatures.ArmatureAccessor;
import yesman.epicfight.model.armature.HumanoidArmature;

public class WatutAnimations {

    public static AnimationAccessor<InteractionAnimation> FOCUSED_GENERAL;
    public static AnimationAccessor<InteractionAnimation> SNORE_GENERAL;
    public static AnimationAccessor<InteractionAnimation> CHAT_TYPING;


    public static void registerAnimations(AnimationRegistryEvent event) {
        event.newBuilder(WatutCompat.MODID, WatutAnimations::build);
    }

    private static void build(AnimationBuilder builder) {
        ArmatureAccessor<HumanoidArmature> biped = Armatures.BIPED;
        FOCUSED_GENERAL = builder.nextAccessor(livingAnimation("focused_general"), accessor ->
                new InteractionAnimation(0.15F, accessor, biped)
        );
        CHAT_TYPING = builder.nextAccessor(livingAnimation("chat_typing"), accessor ->
                new InteractionAnimation(0.05F, accessor, biped)
        );

    }


    private static String livingAnimation(String name) {
        return "biped/living/" + name;
    }

}
