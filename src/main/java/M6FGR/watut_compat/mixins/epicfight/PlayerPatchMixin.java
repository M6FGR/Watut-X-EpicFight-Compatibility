package M6FGR.watut_compat.mixins.epicfight;

import M6FGR.watut_compat.api.animation.WatutLivingMotions;
import M6FGR.watut_compat.gameassets.WatutAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = PlayerPatch.class, remap = false)

public class PlayerPatchMixin {

    @Inject(
            at = @At(value = "HEAD"),
            method = "initAnimator",
            remap = false
    )

    public void injectInteractionAnimations(Animator animator, CallbackInfo ci) {
        animator.addLivingAnimation(WatutLivingMotions.CHAT_TYPING, WatutAnimations.CHAT_TYPING);
        animator.addLivingAnimation(WatutLivingMotions.FOCUSED_GENERAL, WatutAnimations.FOCUSED_GENERAL);
    }
}
