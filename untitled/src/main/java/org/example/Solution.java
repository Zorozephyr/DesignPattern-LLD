package org.example;

import java.util.*;

class Solution {
    public int[][] merge(int[][] intervals) {
        List<int[]> res = new ArrayList<>();
        int[] newInterval = intervals[0];

        for (int i = 1; i < intervals.length; i++) {
            int prevStart = newInterval[0];
            int prevEnd = newInterval[1];
            int currStart = intervals[i][0];
            int currEnd = intervals[i][1];

            if (prevEnd < currStart) {
                res.add(newInterval);
                newInterval = new int[]{currStart, currEnd}; // Create a new interval
            } else {
                newInterval[0] = Math.min(currStart, prevStart);
                newInterval[1] = Math.max(currEnd, prevEnd);
            }
        }

        res.add(newInterval);
        return res.toArray(new int[res.size()][]);
    }

    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] intervals = {
                {1, 3},
                {2, 6},
                {8, 10},
                {15, 18}
        };

        int[][] merged = sol.merge(intervals);

        System.out.println("Merged Intervals:");
        for (int[] interval : merged) {
            System.out.println(Arrays.toString(interval));
        }
    }
}
