package M6FGR.watut_compat.main;

import M6FGR.epic_api.events.epic_api.EpicAPIEventHooks;
import M6FGR.epic_api.events.epic_api.EpicAPIEventHooks.Player;
import M6FGR.watut_compat.api.animation.WatutLivingMotions;
import M6FGR.watut_compat.gameassets.WatutAnimations;
import com.corosus.watut.PlayerStatus;
import com.corosus.watut.PlayerStatus.PlayerChatState;
import com.corosus.watut.PlayerStatus.PlayerGuiState;
import com.corosus.watut.WatutMod;
import com.corosus.watut.config.ConfigCommon;
import com.corosus.watut.loader.neoforge.WatutModNeoForge;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.EmoteAnimation;
import yesman.epicfight.api.client.event.EpicFightClientEventHooks;
import yesman.epicfight.client.gui.screen.EmoteWheelScreen;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;

@Mod(WatutCompat.MOD_ID)
public class WatutCompat {
    public static final String MOD_ID = "watut_compat";
    public static final String MOD_NAME = "Watut Compat";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public WatutCompat(IEventBus modEventBus) {
        LivingMotion.ENUM_MANAGER.registerEnumCls(MOD_ID, WatutLivingMotions.class);
        modEventBus.addListener(this::onCommonStuff);
    }

    private void onCommonStuff(FMLCommonSetupEvent event) {
        event.enqueueWork(WatutCompat::onModifyCompositeMotions);
        event.enqueueWork(WatutCompat::onAddPlayerMotions);
    }


    private static void onModifyCompositeMotions() {
        EpicFightClientEventHooks.Entity.MODIFY_PLAYER_LIVING_MOTION_COMPOSITE.registerEvent(motionEvent -> {
            AbstractClientPlayerPatch<?> acpp = motionEvent.getPlayerPatch();
            PlayerStatus status = WatutMod.getPlayerStatusManagerClient().getStatus(acpp.getOriginal());
            PlayerChatState chatState = status.getPlayerChatState();
            PlayerGuiState guiState = status.getPlayerGuiState();

            // Stop if the motion was SLEEP, so it doesn't play any of these while the player is in bed
            if (motionEvent.getMotion() == LivingMotions.SLEEP) return;

            var currentAnim = acpp.getAnimator().getPlayerFor(null).getRealAnimation();
            boolean isPlayingEmote = currentAnim instanceof EmoteAnimation;
            boolean isEmoteWheelScreen = Minecraft.getInstance().screen instanceof EmoteWheelScreen;

            if (chatState == PlayerChatState.CHAT_TYPING) {
                motionEvent.setMotion(WatutLivingMotions.CHAT_TYPING);
                return; // We are typing, keep the typing composite locked
            }

            if (chatState == PlayerChatState.CHAT_FOCUSED) {
                motionEvent.setMotion(WatutLivingMotions.BROWSING);
                return;
            }

            // Pointing states (Anvil, Crafting, Inventory, etc.)
            if (guiState != PlayerStatus.PlayerGuiState.NONE && PlayerStatus.PlayerGuiState.isPointingGui(guiState) && !isPlayingEmote && !isEmoteWheelScreen) {
                motionEvent.setMotion(WatutLivingMotions.BROWSING);
                return;
            }

            if (PlayerGuiState.isTypingGui(guiState) && chatState != PlayerChatState.CHAT_TYPING && chatState != PlayerChatState.CHAT_FOCUSED) {
                motionEvent.setMotion(WatutLivingMotions.TYPING);
                return;
            }

            boolean isMoving = Math.abs(acpp.dx) > 0.009999999776482582 || Math.abs(acpp.dz) > 0.009999999776482582;

            if (isMoving) {
                status.setTicksSinceLastAction(0);
                status.setTicksToMarkPlayerIdleSyncedForClient(ConfigCommon.ticksToMarkPlayerIdle);
            }

            if (status.isPressing()) {
                motionEvent.setMotion(WatutLivingMotions.PRESSING);
            } else if (status.isIdle()) {
                if (!isMoving) {
                    motionEvent.setMotion(WatutLivingMotions.IDLING);
                }
            }
        });
    }


    private static void onAddPlayerMotions() {
        EpicAPIEventHooks.Player.ADD_MOTIONS.registerEvent(event -> {
            Animator animator = event.getAnimator();
            animator.addLivingAnimation(WatutLivingMotions.BROWSING, WatutAnimations.FOCUSED_GENERAL);
            animator.addLivingAnimation(WatutLivingMotions.TYPING, WatutAnimations.CHAT_TYPING);
            animator.addLivingAnimation(WatutLivingMotions.CHAT_TYPING, WatutAnimations.CHAT_TYPING);
            animator.addLivingAnimation(WatutLivingMotions.IDLING, WatutAnimations.IDLE_STATIC);
            animator.addLivingAnimation(WatutLivingMotions.PRESSING, WatutAnimations.PRESS);
        });
    }

}
