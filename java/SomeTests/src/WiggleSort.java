import java.util.*;

public class WiggleSort {
	public void wiggleSort(int[] nums) {
		int[]nums1 = new int[nums.length]; 
		int firstSymbol = -1;
		int preSymbol = -1;
		for(int i = 0; i < nums.length; i++){
			if(i == 0) {
				nums1[i] = nums[i];
			}
			else if(firstSymbol == -1) {
				if(nums[i] > nums[i - 1]) {
					firstSymbol = 1;
					preSymbol = 1;
				}
				else if(nums[i] < nums[i - 1]) {
					firstSymbol = 0;
					preSymbol = 0;
				}
				else {
					firstSymbol = -2;
					preSymbol = -2;
				}
			}
			else if(firstSymbol == -2) {
				if(nums[i] > nums[i - 1]) {
					firstSymbol = 1;
					preSymbol = 1;
				}
				else if(nums[i] < nums[i - 1]) {
					firstSymbol = 0;
					preSymbol = 0;
				}
				else {
					firstSymbol = -2;
					preSymbol = -2;
				}
			}
		}
	}

	public int[] quickSort(int[] nums, int Lindex, int Hindex) {
		if (Lindex < Hindex) {
			int Mindex = Lindex;
			for (int i = Lindex; i < Hindex + 1; i++) {
				if (nums[i] < nums[Mindex]) {
					int tmp1 = nums[Mindex];
					int tmp2 = nums[Mindex + 1];
					nums[Mindex] = nums[i];
					nums[i] = tmp2;
					nums[Mindex + 1] = tmp1;
					Mindex++;
				}
			}
			nums = quickSort(nums, Lindex, Mindex - 1);
			nums = quickSort(nums, Mindex + 1, Hindex);
		}
		return nums;
	}

	public static void main(String[] args) {
		/*
		 * Enter your code here. Read input from STDIN. Print output to STDOUT.
		 * Your class should be named Solution.
		 */
		WiggleSort test = new WiggleSort();

		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int[] nums = new int[n];
		int i = 0;
		while (i < n) {
			nums[i] = scanner.nextInt();
			i++;
		}
		test.wiggleSort(nums);
		for (i = 0; i < n; i++) {
			System.out.print(String.valueOf(nums[i]) + ' ');
		}
	}

}