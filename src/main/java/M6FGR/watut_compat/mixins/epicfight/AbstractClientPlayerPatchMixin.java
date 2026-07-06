package M6FGR.watut_compat.mixins.epicfight;

import M6FGR.watut_compat.api.animation.WatutLivingMotions;
import com.corosus.watut.PlayerStatus;
import com.corosus.watut.PlayerStatus.PlayerChatState;
import com.corosus.watut.PlayerStatus.PlayerGuiState;
import com.corosus.watut.WatutMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = AbstractClientPlayerPatch.class, remap = false)
public abstract class AbstractClientPlayerPatchMixin extends PlayerPatch<AbstractClientPlayer> {
    public AbstractClientPlayerPatchMixin() {
        super();
    }

    @Inject(
            method = "updateMotion",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/api/IEventBus;post(Lnet/minecraftforge/eventbus/api/Event;)Z"),
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
            this.setLiving(WatutLivingMotions.BROWSING);
        }
        // gui motions

        // pressing only runs if there was another player in the server!
        if (status.isPressing()) {
            this.setComposite(WatutLivingMotions.PRESSING);
        } else if (guiState != PlayerStatus.PlayerGuiState.NONE && PlayerStatus.PlayerGuiState.isPointingGui(guiState)) {
            this.setLiving(WatutLivingMotions.BROWSING);
        } else if (status.isIdle()) {
            this.setLiving(WatutLivingMotions.IDLING);
        }
        // in-game debug
        // Minecraft.getInstance().gui.setOverlayMessage(Component.literal("Current motions are: " + this.currentLivingMotion + " and " + this.currentCompositeMotion), false);
    }

    @Unique
    private void setComposite(LivingMotion motion) {
        this.currentCompositeMotion = motion;
    }

    private void setLiving(LivingMotion motion) {
        this.currentLivingMotion = motion;
    }


}
