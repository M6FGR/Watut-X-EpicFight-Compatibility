package M6FGR.watut_compat.mixins.epicfight;

import M6FGR.watut_compat.api.animation.WatutLivingMotions;
import com.corosus.watut.PlayerStatus;
import com.corosus.watut.PlayerStatus.PlayerChatState;
import com.corosus.watut.PlayerStatus.PlayerGuiState;
import com.corosus.watut.WatutMod;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = AbstractClientPlayerPatch.class, remap = false)
public abstract class AbstractClientPlayerPatchMixin extends PlayerPatch<AbstractClientPlayer> {
    public AbstractClientPlayerPatchMixin(AbstractClientPlayer entity) {
        super(entity);
    }

    @Inject(
            method = "updateMotion",
            at = @At("TAIL"),
            remap = false
    )

    private void injectActions(boolean considerInaction, CallbackInfo ci) {
        PlayerStatus status = WatutMod.getPlayerStatusManagerServer().getStatus(this.original);
        PlayerChatState chatState = status.getPlayerChatState();
        PlayerGuiState guiState = status.getPlayerGuiState();
        if (chatState == PlayerChatState.CHAT_TYPING) {
            this.currentLivingMotion = WatutLivingMotions.CHAT_TYPING;
        } else if (chatState == PlayerChatState.CHAT_FOCUSED) {
            this.currentLivingMotion = WatutLivingMotions.FOCUSED_GENERAL;
        }

        if (this.watutCompat$isAnyGuiMatching(guiState,
                PlayerGuiState.INVENTORY,
                PlayerGuiState.ANVIL,
                PlayerGuiState.CRAFTING,
                PlayerGuiState.ESCAPE,
                PlayerGuiState.EDIT_SIGN,
                PlayerGuiState.EDIT_BOOK,
                PlayerGuiState.CHEST,
                PlayerGuiState.ENCHANTING_TABLE,
                PlayerGuiState.ANVIL,
                PlayerGuiState.BEACON,
                PlayerGuiState.BREWING_STAND,
                PlayerGuiState.DISPENSER,
                PlayerGuiState.FURNACE,
                PlayerGuiState.GRINDSTONE,
                PlayerGuiState.HOPPER,
                PlayerGuiState.HORSE,
                PlayerGuiState.LOOM,
                PlayerGuiState.VILLAGER,
                PlayerGuiState.COMMAND_BLOCK,
                PlayerGuiState.MISC
        )) {
            this.currentLivingMotion = WatutLivingMotions.FOCUSED_GENERAL;
        }


    }


    @Unique
    private boolean watutCompat$isAnyGuiMatching(PlayerGuiState originalState, PlayerGuiState... states) {
        for (PlayerGuiState playerGuiState : states) {
            if (originalState == playerGuiState) {
                return true;
            }
        }
        return false;
    }


}
