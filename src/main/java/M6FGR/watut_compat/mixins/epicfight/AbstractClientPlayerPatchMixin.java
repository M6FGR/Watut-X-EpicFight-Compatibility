package M6FGR.watut_compat.mixins.epicfight;

import M6FGR.watut_compat.api.animation.WatutLivingMotions;
import com.corosus.watut.PlayerStatus;
import com.corosus.watut.PlayerStatus.PlayerChatState;
import com.corosus.watut.PlayerStatus.PlayerGuiState;
import com.corosus.watut.WatutMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = AbstractClientPlayerPatch.class, remap = false, priority = 1001)
// TO-DO: Fix the issue players can't attack after the composite motions are updated
public abstract class AbstractClientPlayerPatchMixin<T extends AbstractClientPlayer> extends PlayerPatch<T> {
    public AbstractClientPlayerPatchMixin(T entity) {
        super(entity);
    }

    @Inject(
            method = "updateMotion",
            at = @At(value = "INVOKE", target = "Lyesman/epicfight/api/event/EventHook;postWithListener(Lyesman/epicfight/api/event/Event;Lyesman/epicfight/api/event/EntityEventListener;)Lyesman/epicfight/api/event/Event;"),
            remap = false
    )
    private void injectActions(boolean considerInaction, CallbackInfo ci) {
        PlayerStatus status = WatutMod.getPlayerStatusManagerClient().getStatus(this.original);
        PlayerChatState chatState = status.getPlayerChatState();
        PlayerGuiState guiState = status.getPlayerGuiState();
        // we stop if the motion was SLEEP, so it doesn't play any of these while the player is in bed
        if (this.currentLivingMotion == LivingMotions.SLEEP) return;
        // chat motions
        if (chatState == PlayerChatState.CHAT_TYPING) {
            this.setLiving(WatutLivingMotions.CHAT_TYPING);
        } else if (chatState == PlayerChatState.CHAT_FOCUSED) {
            this.setLiving(WatutLivingMotions.CHAT_FOCUSED);
        }
        // pressing only runs if there was another player in the server!
        if (status.isPressing()) {
            this.setLiving(WatutLivingMotions.PRESSING);
            // any type of pointing states (Anvil, Crafting, etc.)
        } else if (guiState != PlayerStatus.PlayerGuiState.NONE && PlayerStatus.PlayerGuiState.isPointingGui(guiState)) {
            this.setLiving(WatutLivingMotions.BROWSING);
            // any type of typing states, such as typing on a sign, chat
        } else if (PlayerGuiState.isTypingGui(guiState) && chatState != PlayerChatState.CHAT_TYPING && chatState != PlayerChatState.CHAT_FOCUSED) {
            this.setLiving(WatutLivingMotions.TYPING);
        } else if (status.isIdle()) {
            this.setLiving(WatutLivingMotions.IDLING);
        }


        // in-game debug
        // Minecraft.getInstance().gui.setOverlayMessage(Component.literal("Current motions are: " + this.currentLivingMotion + " and " + this.currentCompositeMotion), false);
    }

    // helping methods
    @Unique
    private void setComposite(LivingMotion motion) {
        this.currentCompositeMotion = motion;
    }

    @Unique
    private void setLiving(LivingMotion motion) {
        this.currentLivingMotion = motion;
    }


}
