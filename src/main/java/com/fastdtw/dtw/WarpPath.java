/*
 * WarpPath.java   Jul 14, 2004
 *
 * Copyright (c) 2004 Stan Salvador
 * stansalvador@hotmail.com
 */

package com.fastdtw.dtw;

import com.fastdtw.matrix.ColMajorCell;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public final class WarpPath {
    private final List<Integer> tsIindexes;
    private final List<Integer> tsJindexes;

    private WarpPath(List<Integer> tsIindexes, List<Integer> tsJindexes) {
        this.tsIindexes = tsIindexes;
        this.tsJindexes = tsJindexes;
    }

    public WarpPath() {
        this(16);
    }

    public WarpPath(int initialCapacity) {
        this(new ArrayList<Integer>(initialCapacity), new ArrayList<Integer>(initialCapacity));
    }

    public int size() {
        return tsIindexes.size();
    }

    public int minI() {
        return tsIindexes.get(tsIindexes.size() - 1);
    }

    public int minJ() {
        return tsJindexes.get(tsJindexes.size() - 1);
    }

    public int maxI() {
        return tsIindexes.get(0);
    }

    public int maxJ() {
        return tsJindexes.get(0);
    }

    public void addFirst(int i, int j) {
        tsIindexes.add(i);
        tsJindexes.add(j);
    }

    public void addLast(int i, int j) {
        tsIindexes.add(0, i);
        tsJindexes.add(0, j);
    }

    public List<Integer> getMatchingIndexesForI(int i) {
        int index = tsIindexes.indexOf(i);
        if (index < 0)
            throw new InternalError("ERROR:  index '" + i + " is not in the " + "warp path.");
        final List<Integer> matchingJs = new ArrayList<Integer>(tsIindexes.size());
        while (index < tsIindexes.size() && tsIindexes.get(index) == i)
            matchingJs.add(tsJindexes.get(index++));

        return matchingJs;
    }

    public List<Integer> getMatchingIndexesForJ(int j) {
        int index = tsJindexes.indexOf(j);
        if (index < 0)
            throw new InternalError("ERROR:  index '" + j + " is not in the " + "warp path.");
        final List<Integer> matchingIs = new ArrayList<Integer>(tsJindexes.size());
        while (index < tsJindexes.size() && tsJindexes.get(index) == j)
            matchingIs.add(tsIindexes.get(index++));

        return matchingIs;
    }

    // Create a new WarpPath that is the same as THIS WarpPath, but J is the reference template, rather than I.
    public WarpPath invertedCopy() {
        final WarpPath newWarpPath = new WarpPath();
        for (int x = 0; x < tsIindexes.size(); x++)
            newWarpPath.addLast(tsJindexes.get(x), tsIindexes.get(x));

        return newWarpPath;
    }

    // Swap I and J so that the warp path now indicates that J is the template rather than I.
    public void invert() {
        for (int x = 0; x < tsIindexes.size(); x++) {
            final int temp = tsIindexes.get(x);
            tsIindexes.set(x, tsJindexes.get(x));
            tsJindexes.set(x, temp);
        }
    }

    public ColMajorCell get(int index) {
        if ((index > this.size()) || (index < 0))
            throw new NoSuchElementException();
        else
            return new ColMajorCell(tsIindexes.get(tsIindexes.size() - index - 1), tsJindexes.get(tsJindexes.size() - index - 1));
    }

    @Override
    public String toString() {
        StringBuilder outStr = new StringBuilder("[");
        for (int x = 0; x < tsIindexes.size(); x++) {
            outStr.append("(").append(tsIindexes.get(x)).append(",").append(tsJindexes.get(x)).append(")");
            if (x < tsIindexes.size() - 1)
                outStr.append(",");
        }
        outStr.append("]");

        return outStr.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof WarpPath))  // trivial false test
        {
            final WarpPath p = (WarpPath) obj;
            if ((p.size() == this.size()) && (p.maxI() == this.maxI()) && (p.maxJ() == this.maxJ())) // less trivial reject
            {
                // Compare each value in the warp path for equality
                for (int x = 0; x < this.size(); x++)
                    if (!(this.get(x).equals(p.get(x))))
                        return false;

                return true;
            } else
                return false;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return tsIindexes.hashCode() * tsJindexes.hashCode();
    }

}
