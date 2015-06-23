package etf.crossword.tt960164d;


public class SlotsLimitation {
    private final Space slot;
    private final int slotIndex;
    private final int index;

    public SlotsLimitation(Space slot, int slotIndex, int index) {
        this.slot = slot;
        this.slotIndex = slotIndex;
        this.index = index;
    }

    public Space getSlot() {
        return slot;
    }
    public int getIndex() {
        return index;
    }
    public int getSlotIndex() {
        return slotIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SlotsLimitation)) return false;

        SlotsLimitation that = (SlotsLimitation) o;

        if (getSlotIndex() != that.getSlotIndex()) return false;
        return getIndex() == that.getIndex() && getSlot().equals(that.getSlot());

    }

    @Override
    public int hashCode() {
        int result = getSlot().hashCode();
        result = 31 * result + getSlotIndex();
        result = 31 * result + getIndex();
        return result;
    }
}
