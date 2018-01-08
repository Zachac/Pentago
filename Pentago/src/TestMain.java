
public class TestMain {

	public static void main(String[] args) {
//		int positions = 36;
//		int max_depth = 5;
//		
//		long[][] treeSize = new long[positions + 1][max_depth + 1];
//		
//		for (int i = 0; i <= positions; i++) {
//			for (int j = 0; j <= max_depth; j++) {
//				treeSize[i][j] = calc(i, j);
//			}
//		}
//		
//		for (int i = 0; i <= positions; i++) {
//			System.out.print(i + ",");
//			for (int j = 0; j <= max_depth; j++) {
//				System.out.print(getLongString(treeSize[i][j]) + ",");
//			}
//			System.out.println();
//		}

		System.out.println((36 * 35 * 34 * 8 * 8 * 8) + (36 * 35 * 8 * 8) + (36 * 8));
//		System.out.println(factorial(36 * 8) / factorial((36 - 3) * 8));
	}
	
	public static String getLongString(long val) {
		if (val < 1000) {
			return "" + val;
		} else if (val < 1000000) {
			return (val / 1000) + "thousand";
		} else if (val < 1000000000) {
			return (val / 1000000) + "million";
		} else {
			return (val / 1000000000L) + "billion";
		}
	}
	
	public static long calc(int branches, int depth) {
		long total = 0;
		long curFringe = 1;
		
		for (int i = 0; i < depth; i++) {
			curFringe *= branches * 8;
			total += curFringe;
			branches--;
		}
		
		return total;
	}
	
	public static long factorial(int n) {
		long result = 1;
		
		for (int i = 1; i <= n; i++) {
			result *= i;
		}
		
		return result;
	}

}
