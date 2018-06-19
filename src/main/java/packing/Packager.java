package packing;

import java.util.*;

/**
 * Fit boxes into container, i.e. perform bin packing to a single container. <br>
 * <br>
 * This attempts a brute force approach, which is very demanding in terms of resources. For use in scenarios with 'few'
 * boxes, where the complexity of a 'few' can be measured for a specific set of boxes and containers using
 * <br>
 * Thread-safe implementation.
 */

public class Packager {

    private final Dimension container;
    public Packager(Dimension container ) {
        this.container = container;
    }

    public Dimension filterContainer(List<Box> boxes) {
        long volume = 0;
        for (Box box : boxes) {
            volume += box.getVolume();
        }

        if (container.getVolume() < volume) {
            throw new ContainerTooSmallException("Container (volume: "+ container.getVolume()+ " is te klein voor al deze dozen (totaal volume: " + volume + ")");
        }

        if(!canHold(container, boxes)) {
            throw new ContainerTooSmallException("Container is te klein voor ingevoerde (enkele) dozen.");
        }

        return container;
    }

    /**
     *
     * Return a container which holds all the boxes in the argument
     *
     * @param boxes
     *            list of boxes to fit in a container
     * @return index of container if match, -1 if not
     */

    public Container pack(List<Box> boxes) {
        return pack(boxes, filterContainer(boxes));
    }

    public Container pack(List<Placement> placements, Dimension container, PermutationBoxIterator rotator) {

        Container holder = new Container(container);

        // iterator over all permutations
        do {
            // iterator over all rotations
            for (Box box : rotator.next()) {
                Dimension remainingSpace = container;

                int index = 0;
                while (index < rotator.getLength()) {
                    if (!rotator.isWithinHeight(index, remainingSpace.getHeight())) {
                        // clean up
                        holder.clear();
                        continue;
                    }

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

                    index = fit2D(rotator, index, placements, holder, placement);

                    // update remaining space
                    remainingSpace = holder.getFreeSpace();
                }

                return holder;
            }
        } while (rotator.hasNext());

        return null;
    }

    protected int fit2D(PermutationBoxIterator rotator, int index, List<Placement> placements, Container holder, Placement usedSpace) {
        // add used space box now
        // there is up to possible 2 free spaces
        holder.add(usedSpace);

        if (index >= rotator.getLength()) {
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
        if (index < rotator.getLength()) {
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

                    index = fit2D(rotator, index, placements, holder, placement);
                }
            }
        }

        // fit the next box in the selected free space
        return fit2D(rotator, index, placements, holder, nextPlacement);
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

    private boolean canHold(Dimension containerBox, List<Box> boxes) {
        for (Box box : boxes) {
            if (!containerBox.canHold2D(box)) {
                return false;
            }
        }
        return true;
    }

    private static List<Placement> getPlacements(int size) {
        // each box will at most have a single placement with a space (and its remainder).
        List<Placement> placements = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            Space a = new Space();
            Space b = new Space();
            a.setRemainder(b);
            b.setRemainder(a);

            placements.add(new Placement(a));
        }
        return placements;
    }

    private Container pack(List<Box> boxes, Dimension dimension) {
        if (dimension == null) {
            return null;
        }

        PermutationBoxIterator permutationIterator = new PermutationBoxIterator(boxes);
        final List<Placement> placements = getPlacements(permutationIterator.getLength());

        Container result = this.pack(placements, dimension, permutationIterator);
        if (result != null) {
            return result;
        }

        return null;
    }

    private class PermutationBoxIterator implements Iterator<List<Box>>  {
        private int[] keys;
        private Map<Integer, Box> objectMap;
        private boolean[] direction;
        private List<Box> nextPermutation;
        int permutationLength = 1;

        private int getLength() {
           return permutationLength -1;
        }

        private boolean isWithinHeight(int fromIndex, int height) {
            for (int i = (fromIndex + 1); i < getLength(); i++) {
                if (objectMap.get(i).getHeight() > height) {
                    return false;
                }
            }
            return true;
        }

        protected Box get(Integer index) {
            return this.objectMap.get(index);
        }

        public PermutationBoxIterator(Collection<Box> coll) {
            if (coll == null) {
                throw new NullPointerException("The collection must not be null");
            } else {
                this.keys = new int[coll.size()];
                this.direction = new boolean[coll.size()];
                Arrays.fill(this.direction, false);

                this.objectMap = new HashMap<>();

                for(Iterator it = coll.iterator(); it.hasNext(); this.keys[permutationLength - 1] = permutationLength++) {
                    Box e = (Box) it.next();
                    this.objectMap.put(permutationLength, e);
                }

                this.nextPermutation = new ArrayList<>(coll);
            }
        }

        public boolean hasNext() {
            return this.nextPermutation != null;
        }

        public List<Box> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                int indexOfLargestMobileInteger = -1;
                int largestKey = -1;

                int offset;
                for(offset = 0; offset < this.keys.length; ++offset) {
                    if ((this.direction[offset] && offset < this.keys.length - 1 && this.keys[offset] > this.keys[offset + 1] || !this.direction[offset] && offset > 0 && this.keys[offset] > this.keys[offset - 1]) && this.keys[offset] > largestKey) {
                        largestKey = this.keys[offset];
                        indexOfLargestMobileInteger = offset;
                    }
                }

                if (largestKey == -1) {
                    List<Box> toReturn = this.nextPermutation;
                    this.nextPermutation = null;
                    return toReturn;
                } else {
                    offset = this.direction[indexOfLargestMobileInteger] ? 1 : -1;
                    int tmpKey = this.keys[indexOfLargestMobileInteger];
                    this.keys[indexOfLargestMobileInteger] = this.keys[indexOfLargestMobileInteger + offset];
                    this.keys[indexOfLargestMobileInteger + offset] = tmpKey;
                    boolean tmpDirection = this.direction[indexOfLargestMobileInteger];
                    this.direction[indexOfLargestMobileInteger] = this.direction[indexOfLargestMobileInteger + offset];
                    this.direction[indexOfLargestMobileInteger + offset] = tmpDirection;
                    List<Box> nextP = new ArrayList();

                    for(int i = 0; i < this.keys.length; ++i) {
                        if (this.keys[i] > largestKey) {
                            this.direction[i] = !this.direction[i];
                        }

                        nextP.add(this.objectMap.get(this.keys[i]));
                    }

                    List<Box> result = this.nextPermutation;
                    this.nextPermutation = nextP;
                    return result;
                }
            }
        }
    }
}
