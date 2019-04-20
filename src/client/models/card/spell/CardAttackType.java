package client.models.card.spell;

public class CardAttackType {
    private boolean melee;
    private boolean ranged;
    private boolean hybrid;

    public CardAttackType(boolean melee, boolean ranged, boolean hybrid) {
        this.melee = melee;
        this.ranged = ranged;
        this.hybrid = hybrid;
    }

    public boolean isMelee() {
        return melee;
    }

    public boolean isRanged() {
        return ranged;
    }

    public boolean isHybrid() {
        return hybrid;
    }
}
