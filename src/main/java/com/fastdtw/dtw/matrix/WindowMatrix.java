/*
 * WindowMatrix.java   Jul 14, 2004
 *
 * Copyright (c) 2004 Stan Salvador
 * stansalvador@hotmail.com
 */

package com.fastdtw.dtw.matrix;

import com.fastdtw.dtw.window.SearchWindow;

public final class WindowMatrix implements CostMatrix {
    private CostMatrix windowCells;

    public WindowMatrix(SearchWindow searchWindow) {
        try {
            windowCells = new MemoryResidentMatrix(searchWindow);
        } catch (OutOfMemoryError e) {
            System.err
                    .println("Ran out of memory initializing window matrix, all cells in the window cannot fit into "
                            + "main memory.  Will use a swap file instead (will run ~50% slower)");
            System.gc();
            windowCells = new SwapFileMatrix(searchWindow);
        }
    }

    @Override
    public void put(int col, int row, double value) {
        windowCells.put(col, row, value);
    }

    @Override
    public double get(int col, int row) {
        return windowCells.get(col, row);
    }

    @Override
    public int size() {
        return windowCells.size();
    }

    public void freeMem() {
        // Resources only need to be freed for a SwapFileMatrix.
        if (windowCells instanceof SwapFileMatrix) {
            try {
                ((SwapFileMatrix) windowCells).freeMem();
            } catch (Throwable t) {
                // ignore the exception
            }
        }
    }

}
