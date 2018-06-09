package packing;

import java.util.ArrayList;
import java.util.List;

/**
 * Fit boxes into container, i.e. perform bin packing to a single container. <br>
 * <br>
 * This attempts a brute force approach, which is very demanding in terms of resources. For use in scenarios with 'few'
 * boxes, where the complexity of a 'few' can be measured for a specific set of boxes and containers using
 * {@linkplain PermutationRotationIterator#countPermutations()} *
 * {@linkplain PermutationRotationIterator#countRotations()}. <br>
 * <br>
 * Thread-safe implementation.
 */

public class BruteForcePackager {

    protected final Dimension[] containers;

    protected final boolean rotate3D; // if false, then 2d
    protected final boolean binarySearch;

    /**
     * Logical packager for wrapping preprocessing / optimizations.
     *
     */

    public interface Adapter {
        Container pack(List<BoxItem> boxes, Dimension dimension, long deadline);
    }


    /**
     * Constructor
     * 
     * @param containers
     *            list of containers
     * @param rotate3D
     *            whether boxes can be rotated in all three directions (two directions otherwise)
     * @param binarySearch
     *            if true, the packager attempts to find the best box given a binary search. Upon finding a container that
     *            can hold the boxes, given time, it also tries to find a better match.
     */
    public BruteForcePackager(List<? extends Dimension> containers, boolean rotate3D, boolean binarySearch) {
        this.containers = containers.toArray(new Dimension[containers.size()]);
        this.rotate3D = rotate3D;
        this.binarySearch = binarySearch;
    }

    protected Container pack(List<Placement> placements, Dimension dimension, PermutationRotationIterator.PermutationRotation[] rotations, long deadline) {

        PermutationRotationIterator rotator = new PermutationRotationIterator(dimension, rotations);

        return pack(placements, dimension, rotator, deadline);
    }

    public List<Dimension> filterContainers(List<BoxItem> boxes) {
        long volume = 0;
        for (BoxItem box : boxes) {
            volume += box.getBox().getVolume() * box.getCount();
        }

        List<Dimension> list = new ArrayList<>();
        for (Dimension container : containers) {
            if (container.getVolume() < volume || !canHold(container, boxes)) {
                // discard this container
                continue;
            }

            list.add(container);
        }

        return list;
    }

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

    public Container pack(List<BoxItem> boxes, long deadline) {
        return pack(boxes, filterContainers(boxes), deadline);
    }

    public Container pack(List<Placement> placements, Dimension container, PermutationRotationIterator rotator, long deadline) {

        Container holder = new Container(container);

        // iterator over all permutations
        do {
            if (System.currentTimeMillis() > deadline) {
                break;
            }
            // iterator over all rotations

            fit: do {
                Dimension remainingSpace = container;

                int index = 0;
                while (index < rotator.length()) {
                    if (System.currentTimeMillis() > deadline) {
                        // fit2d below might have returned due to deadline
                        return null;
                    }

                    if (!rotator.isWithinHeight(index, remainingSpace.getHeight())) {
                        // clean up
                        holder.clear();

                        continue fit;
                    }

                    Box box = rotator.get(index);

                    Placement placement = placements.get(index);
                    Space levelSpace = placement.getSpace();
                    levelSpace.width = container.getWidth();
                    levelSpace.depth = container.getDepth();
                    levelSpace.height = box.getHeight();

                    placement.setBox(box);

                    levelSpace.x = 0;
                    levelSpace.y = 0;
                    levelSpace.z = holder.getStackHeight();

                    levelSpace.setParent(null);
                    levelSpace.getRemainder().setParent(null);

                    holder.addLevel();

                    index++;

                    index = fit2D(rotator, index, placements, holder, placement, deadline);

                    // update remaining space
                    remainingSpace = holder.getFreeSpace();
                }

                return holder;
            } while (rotator.nextRotation());
        } while (rotator.nextPermutation());

        return null;
    }

    protected int fit2D(PermutationRotationIterator rotator, int index, List<Placement> placements, Container holder, Placement usedSpace, long deadline) {
        // add used space box now
        // there is up to possible 2 free spaces
        holder.add(usedSpace);

        if (index >= rotator.length()) {
            return index;
        }

        if (System.currentTimeMillis() > deadline) {
            return index;
        }

        Box nextBox = rotator.get(index);
        Placement nextPlacement = placements.get(index);

        nextPlacement.setBox(nextBox);

        if (!isFreespace(usedSpace.getSpace(), usedSpace.getBox(), nextPlacement)) {
            // no additional boxes
            // just make sure the used space fits in the free space
            return index;
        }

        index++;
        // the correct space dimensions is copied into the next placement

        // attempt to fit in the remaining (usually smaller) space first

        // stack in the 'sibling' space - the space left over between the used box and the selected free space
        if (index < rotator.length()) {
            Space remainder = nextPlacement.getSpace().getRemainder();
            if (!remainder.isEmpty()) {
                Box box = rotator.get(index);

                if (box.fitsInside3D(remainder)) {
                    Placement placement = placements.get(index);
                    placement.setBox(box);

                    index++;

                    placement.getSpace().copyFrom(remainder);
                    placement.getSpace().setParent(remainder);
                    placement.getSpace().getRemainder().setParent(remainder);

                    index = fit2D(rotator, index, placements, holder, placement, deadline);
                }
            }
        }

        // fit the next box in the selected free space
        return fit2D(rotator, index, placements, holder, nextPlacement, deadline);
    }

    protected boolean isFreespace(Space freespace, Box used, Placement target) {

        // Two free spaces, on each rotation of the used space.
        // Height is always the same, used box is assumed within free space height.
        // First:
        // ........................ ........................ .............
        // . . . . . .
        // . . . . . .
        // . A . . A . . .
        // . . . . . .
        // . B . . . . B .
        // ............ . ........................ . .
        // . . . . .
        // . . . . .
        // ........................ .............
        //
        // So there is always a 'big' and a 'small' leftover area (the small is not shown).
        if (freespace.getWidth() >= used.getWidth() && freespace.getDepth() >= used.getDepth()) {

            // if B is empty, then it is sufficient to work with A and the other way around
            int b = (freespace.getWidth() - used.getWidth()) * freespace.getDepth();
            int a = freespace.getWidth() * (freespace.getDepth() - used.getDepth());

            // pick the one with largest footprint.
            if (b >= a) {
                if (b > 0 && b(freespace, used, target)) {
                    return true;
                }

                return a > 0 && a(freespace, used, target);
            } else {
                if (a > 0 && a(freespace, used, target)) {
                    return true;
                }

                return b > 0 && b(freespace, used, target);
            }
        }
        return false;
    }

    private boolean a(Space freespace, Box used, Placement target) {
        if (target.getBox().fitsInside3D(freespace.getWidth(), freespace.getDepth() - used.getDepth(), freespace.getHeight())) {
            target.getSpace().copyFrom(freespace.getWidth(), freespace.getDepth() - used.getDepth(), freespace.getHeight(), freespace.getX(),
                    freespace.getY() + used.depth, freespace.getHeight());
            target.getSpace().getRemainder().copyFrom(freespace.getWidth() - used.getWidth(), used.getDepth(), freespace.getHeight(),
                    freespace.getX() + used.getWidth(), freespace.getY(), freespace.getZ());
            target.getSpace().setParent(freespace);
            target.getSpace().getRemainder().setParent(freespace);

            return true;
        }
        return false;
    }

    private boolean b(Space freespace, Box used, Placement target) {
        if (target.getBox().fitsInside3D(freespace.getWidth() - used.getWidth(), freespace.getDepth(), freespace.getHeight())) {
            // we have a winner
            target.getSpace().copyFrom(freespace.getWidth() - used.getWidth(), freespace.getDepth(), freespace.getHeight(), freespace.getX() + used.getWidth(),
                    freespace.getY(), freespace.getZ());

            target.getSpace().getRemainder().copyFrom(used.getWidth(), freespace.getDepth() - used.getDepth(), freespace.getHeight(), freespace.getX(),
                    freespace.getY() + used.getDepth(), freespace.getZ());

            target.getSpace().setParent(freespace);
            target.getSpace().getRemainder().setParent(freespace);
            return true;
        }
        return false;
    }

    protected Adapter adapter(List<BoxItem> boxes) {
        // instead of placing boxes, work with placements
        // this very much reduces the number of objects created
        // performance gain is something like 25% over the box-centric approach

        final PermutationRotationIterator.PermutationRotation[] rotations = PermutationRotationIterator.toRotationMatrix(boxes, rotate3D);

        int count = 0;
        for (PermutationRotationIterator.PermutationRotation permutationRotation : rotations) {
            count += permutationRotation.getCount();
        }

        final List<Placement> placements = getPlacements(count);

        return new Adapter() {
            @Override
            public Container pack(List<BoxItem> boxes, Dimension dimension, long deadline) {
                return BruteForcePackager.this.pack(placements, dimension, rotations, deadline);
            }
        };
    }

    protected boolean canHold(Dimension containerBox, List<BoxItem> boxes) {
        for (BoxItem box : boxes) {
            if (rotate3D) {
                if (!containerBox.canHold3D(box.getBox())) {
                    return false;
                }
            } else {
                if (!containerBox.canHold2D(box.getBox())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Placement> getPlacements(int size) {
        // each box will at most have a single placement with a space (and its remainder).
        List<Placement> placements = new ArrayList<Placement>(size);

        for (int i = 0; i < size; i++) {
            Space a = new Space();
            Space b = new Space();
            a.setRemainder(b);
            b.setRemainder(a);

            placements.add(new Placement(a));
        }
        return placements;
    }

    public Container pack(List<BoxItem> boxes, List<Dimension> dimensions, long deadline) {
        if (dimensions.isEmpty()) {
            return null;
        }

        Adapter pack = adapter(boxes);

        if (!binarySearch || dimensions.size() <= 2 || deadline == Long.MAX_VALUE) {
            for (int i = 0; i < dimensions.size(); i++) {

                if (System.currentTimeMillis() > deadline) {
                    break;
                }

                Container result = pack.pack(boxes, dimensions.get(i), deadline);
                if (result != null) {
                    return result;
                }
            }
        } else {
            Container[] results = new Container[dimensions.size()];
            boolean[] checked = new boolean[results.length];

            ArrayList<Integer> current = new ArrayList<>();
            for (int i = 0; i < dimensions.size(); i++) {
                current.add(i);
            }

            BinarySearchIterator iterator = new BinarySearchIterator();

            search: do {
                iterator.reset(current.size() - 1, 0);

                do {
                    int next = iterator.next();
                    int mid = current.get(next);

                    Container result = pack.pack(boxes, dimensions.get(mid), deadline);

                    checked[mid] = true;
                    if (result != null) {
                        results[mid] = result;

                        iterator.lower();
                    } else {
                        iterator.higher();
                    }
                    if (System.currentTimeMillis() > deadline) {
                        break search;
                    }
                } while (iterator.hasNext());

                // halt when have a result, and checked all containers at the lower indexes
                for (int i = 0; i < current.size(); i++) {
                    Integer integer = current.get(i);
                    if (results[integer] != null) {
                        // remove end items; we already have a better match
                        while (current.size() > i) {
                            current.remove(current.size() - 1);
                        }
                        break;
                    }

                    // remove item
                    if (checked[integer]) {
                        current.remove(i);
                        i--;
                    }
                }
            } while (!current.isEmpty());

            for (int i = 0; i < results.length; i++) {
                if (results[i] != null) {
                    return results[i];
                }
            }
        }
        return null;
    }

}
