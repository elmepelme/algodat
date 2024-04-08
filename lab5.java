import java.util.*;

public class lab5 {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int delta = -4;
		String[] stringSymbols = scan.nextLine().split(" ");
    char[] symbols = new char[stringSymbols.length];

    for (int i = 0; i < stringSymbols.length; i++) {
      symbols[i] = stringSymbols[i].charAt(0);
    }

    Map<Character, Integer> index = new HashMap<>();

    for (int i = 0; i < symbols.length; i++) {
      index.put(symbols[i], i);
    }

		int n = symbols.length;
		int[][] costMatrix = getCostMatrix(scan, n);
		int nbrQueries = scan.nextInt();
		scan.nextLine(); // behöver göra detta för nextInt tar inte bort raden
		String[][] allQueries = getAllQueries(scan, nbrQueries);

		for (int i = 0; i < nbrQueries; i++) {
			String[] queries = allQueries[i];
			int[][] A = calculateMatrixA(queries, index, costMatrix, delta);
			String[] alignments = findAlignment(A, index, costMatrix, delta, queries);
			System.out.println(alignments[0] + " " + alignments[1]);
		}
	}

	public static int[][] calculateMatrixA(String[] queries,
  Map<Character, Integer> index, int[][] costMatrix, int delta) {
		String s1 = queries[0];
		String s2 = queries[1];
		int m = s1.length() + 1;
		int n = s2.length() + 1;
		int[][] A = new int[m][n];

		for (int i = 0 ; i < m ; i++) {
			A[i][0] = i*delta;
		}
		for (int j = 0 ; j < n ; j++) {
			A[0][j] = j*delta;
		}

		for (int i = 1; i < m ; i++) {
			for(int j = 1; j < n ; j++) {
				int ind1 = index.get(s1.charAt(i-1));
				int ind2 = index.get(s2.charAt(j-1));
				int val = costMatrix[ind1][ind2];
				int m1 = Math.max(A[i-1][j-1] + val, A[i-1][j] + delta);
				A[i][j] = Math.max(m1, A[i][j-1] + delta);
			}
		}
		return A;
	}

	public static String[] findAlignment(int[][] A,
	Map<Character, Integer> index, int[][] costMatrix, int delta,
	String[] queries) {
		String[] alignments = new String[2];
		String s1 = queries[0];
		String s2 = queries[1];
		String align1 = "";
		String align2 = "";
		int i = s1.length();
		int j = s2.length();
		while (i > 0 || j > 0) {
			if (i > 0 && j > 0 && A[i][j] == A[i-1][j-1] +
			costMatrix[index.get(s1.charAt(i-1))][index.get(s2.charAt(j-1))]) {
				align1 += s1.charAt(i-1);
				align2 += s2.charAt(j-1);
				i -= 1;
				j -= 1;
			}
			else if(i > 0 && A[i][j] == A[i-1][j] + delta) {
				align1 += s1.charAt(i-1);
				align2 += '*';
				i -= 1;
			} else {
				align1 += '*';
				align2 += s2.charAt(j-1);
				j -= 1;
			}
		}

		align1 = new StringBuilder(align1).reverse().toString();
		align2 = new StringBuilder(align2).reverse().toString();
		alignments[0] = align1;
		alignments[1] = align2;
		return alignments;
	}

	public static int[][] getCostMatrix(Scanner scan, int n) {
		int[][] costMatrix = new int[n][n];
		for (int i = 0; i < n ; i++) {
			String line = scan.nextLine();
			String[] lineInts = line.split(" ", 0);
			for(int j = 0; j < lineInts.length; j++) {
				costMatrix[i][j] = Integer.parseInt(lineInts[j]);
			}
		}
		return costMatrix;
	}

	public static String[][] getAllQueries(Scanner scan, int nbrQueries) {
		String[][] queries = new String[nbrQueries][2];
		for (int i = 0; i < nbrQueries ; i++) {
			String line = scan.nextLine();
			String[] words = line.split(" ");
			queries[i][0] = words[0];
			queries[i][1] = words[1];
		}
		return queries;
	}
}
