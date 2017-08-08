package org.walkerljl.toolkit.db.api.hash;

/**
 * Hashing
 *
 * @author lijunlin
 */
public interface Hashing {

    /**
     *
     * @param object
     * @param length
     * @return
     */
    int indexFor(Object object, int length);
}
