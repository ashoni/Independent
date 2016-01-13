package weightedGraphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class Queens {
    private static Set<Integer> getFreeCells(List<Integer> upperQueens, int n) {
        Set<Integer> res = new HashSet<>();
        int cRow = upperQueens.size();
        for (int i = 0; i < n; i ++) {
            res.add(i);
        }
        res.removeAll(upperQueens);
        for (int i = 0; i < cRow; i ++) {
            res.remove(upperQueens.get(i) + (cRow - i));
            res.remove(upperQueens.get(i) - (cRow - i));
            if (res.isEmpty()) {
                break;
            }
        }
        return res;
    }

    public static int placeQueens(List<Integer> upperQueens, int n) {
        Set<Integer> freeCells = getFreeCells(upperQueens, n);
        if (freeCells.isEmpty()) {
            return 0;
        } else {
            if (upperQueens.size() + 1 == n) {
                for (Integer q : freeCells) {
                    System.out.print(upperQueens);
                    System.out.println(", " + q);
                }
                return freeCells.size();
            }
            int sum = 0;
            for (Integer c : freeCells) {
                List<Integer> newQueens = new ArrayList<>(upperQueens);
                newQueens.add(c);
                sum += placeQueens(newQueens, n);
            }
            return sum;
        }
    }

    public static void main(String[] args) {
        System.out.println(Queens.placeQueens(new ArrayList<>(), 8));
    }
}
