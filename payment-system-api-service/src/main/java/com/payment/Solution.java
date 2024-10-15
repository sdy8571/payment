package com.payment;

/**
 * @author sdy
 * @description
 * @date 2024/9/18
 */
public class Solution {

    public static void main(String[] args) {
//        int target = 7;
//        int[] nums = {2,3,1,2,4,3};
//        System.out.println(minSubArrayLen(target, nums));
//        System.out.println("===>" + minSumArrayLen1(target, nums));
//
//        int target1 = 4;
//        int[] nums1 = {1,4,4};
//        System.out.println(minSubArrayLen(target1, nums1));
//        System.out.println("===>" + minSumArrayLen1(target1, nums1));
//
//        int target2 = 11;
//        int[] nums2 = {1,1,1,1,1,1,1,1};
//        System.out.println(minSubArrayLen(target2, nums2));
//        System.out.println("===>" + minSumArrayLen1(target2, nums2));
//
//        int target3 = 11;
//        int[] nums3 = {1,2,3,4,5};
//        System.out.println(minSubArrayLen(target3, nums3));
//        System.out.println("===>" + minSumArrayLen1(target3, nums3));
//
//        int target4 = 7;
//        int[] nums4 = {1,1,1,1,7};
//        System.out.println(minSubArrayLen(target4, nums4));
//        System.out.println("===>" + minSumArrayLen1(target4, nums4));

        int target5 = 396893380;
        int[] nums5 = {1,1,1,1,7};
        System.out.println(minSubArrayLen(target5, nums5));
        System.out.println("===>" + minSumArrayLen1(target5, nums5));
    }

    public static int minSumArrayLen1(int s, int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += nums[j];
                if (sum >= s) {
                    ans = Math.min(ans, j - i + 1);
                    break;
                }
            }
        }
        return ans == Integer.MAX_VALUE ? 0 : ans;
    }

    public static int minSubArrayLen(int target, int[] nums) {
        int left = 0;
        int right = 0;
        int sum = 0;
        int len = nums.length;
        int subLen = 0;
        while (left < len) {
            int offset = right - left;
            if (offset > 0) {
                sum += nums[right];
            } else {
                sum = nums[left];
            }
            if (sum < target && right < len - 1) {
                right++;
                continue;
            }
            if (sum >= target && (subLen == 0 || subLen > offset + 1)) {
                subLen = offset + 1;
            }
            left++;
            right = left;
            sum = 0;
        }
        return subLen;
    }

}
