package M6FGR.watut_compat.mixins.epicfight;

import M6FGR.watut_compat.api.animation.WatutLivingMotions;
import M6FGR.watut_compat.gameassets.WatutAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = PlayerPatch.class, remap = false)

public class PlayerPatchMixin {

    @Inject(
            at = @At(value = "TAIL"),
            method = "initAnimator",
            remap = false
    )

    public void injectInteractionAnimations(Animator animator, CallbackInfo ci) {
        animator.addLivingAnimation(WatutLivingMotions.IDLING, WatutAnimations.IDLE_STATIC);
        animator.addLivingAnimation(WatutLivingMotions.PRESSING, WatutAnimations.PRESS);
        this.addAnimForMotions(animator, WatutAnimations.FOCUSED_GENERAL,
                WatutLivingMotions.CHAT_FOCUSED, WatutLivingMotions.BROWSING
        );
        animator.addLivingAnimation(WatutLivingMotions.CHAT_TYPING, WatutAnimations.CHAT_TYPING);
    }

    @Unique
    private void addAnimForMotions(Animator animator, AnimationAccessor<? extends StaticAnimation> animationAccessor, LivingMotion... motions) {
        for (LivingMotion motion : motions) {
            animator.addLivingAnimation(motion, animationAccessor);
        }
    }
}
