package city;

/**
 * Schopnost objektu produkovat energii v jednom tahu.
 */
public interface Producing {
    /**
     * Vyrobí energii pro aktuální tah.
     *
     * @return množství vyrobené energie
     */
    int produce();
}
