package M6FGR.watut_compat.api.animation;

import yesman.epicfight.api.animation.LivingMotion;

public enum WatutLivingMotions implements LivingMotion {
    CHAT_TYPING,
    CHAT_FOCUSED,
    TYPING,
    IDLING,
    PRESSING,
    BROWSING;

    private final int ordinalID;
    WatutLivingMotions() {
        this.ordinalID = LivingMotion.ENUM_MANAGER.assign(this);
    }

    @Override
    public int universalOrdinal() {
        return this.ordinalID;
    }

    @Override
    public boolean isSame(LivingMotion livingMotion) {
        return this == livingMotion;
    }
}
