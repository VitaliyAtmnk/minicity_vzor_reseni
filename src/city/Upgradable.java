package city;

/**
 * Schopnost objektu, který lze za určitou cenu vylepšit.
 */
public interface Upgradable {
    /**
     * @return cena následujícího vylepšení
     */
    int getUpgradePrice();

    /**
     * Provede jedno vylepšení objektu.
     */
    void upgrade();
}
