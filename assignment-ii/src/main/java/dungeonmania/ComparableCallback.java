package dungeonmania;

/**
 * ComparableCallback is a wrapper around a Runnable
 * For every runnable r, we associate a value v to it
 * higher v means lower priority
 * the callback is performed based on one entity,
 * whose entityId is attached to the callback
 */
public class ComparableCallback implements Comparable<ComparableCallback>, Runnable {

    private Runnable r;
    private int v;
    private String entityId; // entityId related to the current runnable
    private boolean isValid = true; // used to invalidate the current callback
    private boolean once = false; // some callback can only be used once

    public ComparableCallback(Runnable r, int v, String entityId, boolean once) {
        this.r = r;
        this.v = v;
        this.entityId = entityId;
        this.once = once;
    }

    public ComparableCallback(Runnable r, int v, String entityId) {
        this(r, v, entityId, false);
    }

    // run the callback
    @Override
    public void run() {
        if (isValid) r.run();
        if (once) invalidate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() != this.getClass()) return false;
        ComparableCallback other = (ComparableCallback) o;

        return this.entityId.equals(other.entityId);
    }
    @Override
    public int compareTo(ComparableCallback arg0) {
        return Integer.compare(v, arg0.v);
    }

    public String getId() {
        return this.entityId;
    }

    public void invalidate() {
        this.isValid = false;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isOnce() {
        return once;
    }
}
