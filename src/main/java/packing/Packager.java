package packing;

import java.util.ArrayList;
import java.util.List;

/**
 * Fit boxes into container, i.e. perform bin packing to a single container.
 * 
 * Thread-safe implementation.
 */

public abstract class Packager {




    /**
     * Constructor
     * 
     * @param containers
     *            list of containers
     * @param rotate3D
     *            whether boxes can be rotated in all three directions (two directions otherwise)
     * @param binarySearch
     *            if true, the packager attempts to find the best box given a binary search. Upon finding a match, it
     *            searches the preceding boxes as well, until the deadline is passed.
     */



    /**
     * 
     * Return a container which holds all the boxes in the argument.
     * 
     * @param boxes
     *            list of boxes to fit in a container
     * @return null if no match
     */



    /**
     * Return a list of containers which can potentially hold the boxes.
     * 
     * @param boxes
     *            list of boxes
     * @return list of containers
     */



    /**
     *
     * Return a container which holds all the boxes in the argument
     *
     * @param boxes
     *            list of boxes to fit in a container
     * @param deadline
     *            the system time in millis at which the search should be aborted
     * @return index of container if match, -1 if not
     */




}
