package M6FGR.watut_compat.main;

import M6FGR.watut_compat.api.animation.WatutLivingMotions;
import M6FGR.watut_compat.gameassets.WatutAnimations;
import com.corosus.watut.WatutMod;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.LivingMotion;

@Mod(WatutCompat.MODID)
public class WatutCompat {
    public static final String MODID = "watut_compat";
    public WatutCompat(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(WatutAnimations::registerAnimations);
        LivingMotion.ENUM_MANAGER.registerEnumCls(MODID, WatutLivingMotions.class);

    }

}
