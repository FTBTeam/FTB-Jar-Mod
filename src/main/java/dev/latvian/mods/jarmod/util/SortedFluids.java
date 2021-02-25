package dev.latvian.mods.jarmod.util;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

/**
 * @author LatvianModder
 */
public class SortedFluids {
	public final int height;
	public final int capacity;
	public final int[] amounts;
	public final int totalHeight;
	public final int[] heights;
	public final int[] offsets;

	public SortedFluids(int h, int t, int s, Int2IntFunction function) {
		height = h;
		capacity = t;
		amounts = new int[s];
		heights = new int[s];
		offsets = new int[s];

		for (int i = 0; i < s; i++) {
			amounts[i] = function.get(i);
			heights[i] = Math.max(3, amounts[i] / (capacity / height));
		}

		int th = 0;

		for (int i = 0; i < s; i++) {
			offsets[i] = th;
			th += heights[i];
		}

		totalHeight = th;

		for (int i = 0; i < s; i++) {
			offsets[i] += (height - totalHeight);
		}
	}
}