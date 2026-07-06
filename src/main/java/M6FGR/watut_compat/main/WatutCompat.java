package M6FGR.watut_compat.main;

import M6FGR.watut_compat.api.animation.WatutLivingMotions;
import M6FGR.watut_compat.gameassets.WatutAnimations;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import yesman.epicfight.api.animation.LivingMotion;
import java.util.logging.Logger;

@Mod(WatutCompat.MODID)
public class WatutCompat {
    public static final String MODID = "watut_compat";
    public static final String MOD_NAME = "Watut Compat";
    public static final Logger LOGGER = Logger.getLogger(MOD_NAME);
    public WatutCompat(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(WatutAnimations::registerAnimations);
        LivingMotion.ENUM_MANAGER.registerEnumCls(MODID, WatutLivingMotions.class);
    }

}
