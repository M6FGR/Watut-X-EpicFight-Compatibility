package M6FGR.watut_compat.main;

import M6FGR.watut_compat.gameassets.WatutAnimations;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WatutCompat.MODID)
public class WatutCompat {
    public static final String MODID = "watut_compat";
    public WatutCompat(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(WatutAnimations::registerAnimations);

    }

}
