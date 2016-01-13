import weightedGraphs.WeightedVertex;
import weightedGraphs.WeightedVertexGraph;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Cracking the coding interview: 8.13.
 *
 * You have a stack of n boxes with width w_i, h_i, d_i. The boxes cannot be rotated and can only be stacked on top
 * of one another if each box in the stack is strictly larger than the box above it in width, height and depth.
 * Implement a method to compute the height of the tallest possible stack. The height of a stack is the sum of the
 * heights of each box.
 */
public class CCIBoxTower {
    static class Box {
        private int width;
        private int depth;
        private int height;
        private int id;

        Box(int w, int d, int h) {
            width = w;
            height = h;
            depth = d;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getWidth() {
            return width;
        }

        public int getDepth() {
            return depth;
        }

        public int getHeight() {
            return height;
        }

        public int getId() {
            return id;
        }

        public int getHierarchy(Box box) {
            if (width > box.width && height > box.height && depth > box.depth) {
                return 1;
            } else if (width < box.width && height < box.height && depth < box.depth) {
                return -1;
            }
            return 0;
        }

        @Override
        public String toString() {
            return new StringBuilder(id).append("{")
                    .append(height).append(",")
                    .append(width).append(",")
                    .append(depth).append("}").toString();
        }
    }

    private Map<Integer, Box> boxes;
    private int n;

    public CCIBoxTower(List<Box> boxes) {
        this.boxes = new HashMap<>();
        int i = 0;
        for (Box box : boxes) {
            box.setId(i);
            this.boxes.put(box.getId(), box);
            i ++;
        }
        n = boxes.size();
    }

    public int calculateHeight(List<Box> bestBoxes) {
        int sumHeight = 0;
        for (Box box : bestBoxes) {
            sumHeight += box.getHeight();
        }
        return sumHeight;
    }

    public AbstractMap.SimpleEntry<Integer, List<Integer>> getNextColumn(List<Integer> curColumn) {
        int maxPerColumn = -1;
        int maxId = -1;
        List<Integer> nextColumn = new ArrayList<>(n);
        for (int i = 0; i < curColumn.size(); i ++) {
            Box box = boxes.get(i);
            int maxHeight = -1;
            for (int j = 0; j < curColumn.size(); j ++) {
                if (curColumn.get(j) == -1) {
                    continue;
                }
                Box baseBox = boxes.get(j);
                if (box.getHierarchy(baseBox) == -1) {
                    maxHeight = maxHeight < curColumn.get(j) + box.getHeight() ?
                            curColumn.get(j) + box.getHeight() : maxHeight;
                }
            }
            if (maxPerColumn < maxHeight) {
                maxPerColumn = maxHeight;
                maxId = i;
            }
            nextColumn.add(maxHeight);
        }
        return new AbstractMap.SimpleEntry<>(maxId, nextColumn);
    }

    /**
     * Looks for a best solution by creating a table with box id on rows and stack height on columns.
     * Intersection of i row and j column contains max height for a stack of j boxes and box i on the top,
     * -1 if it is impossible to create a stack with such parameters.
     * O(n^2) by memory (table with n boxes and max n levels)
     */
    public List<Box> getBestStackByDynamic() {
        if (boxes.isEmpty()) {
            return Collections.emptyList();
        }

        List<List<Integer>> partSolutions = new ArrayList<>();
        int maxStackH = 0;
        int maxBoxId = -1;
        int maxH = -1;

        List<Integer> firstColumn = new ArrayList<>(n);
        for (int i = 0; i < n; i ++) {
            firstColumn.add(boxes.get(i).height);
            maxH = maxH > boxes.get(i).height ? maxH : boxes.get(i).height;
            if (maxH < boxes.get(i).height) {
                maxH = boxes.get(i).height;
                maxBoxId = i;
            }
        }
        partSolutions.add(firstColumn);

        int boxesInStack = 1;
        boolean done = false;

        while (!done) {
            AbstractMap.SimpleEntry<Integer, List<Integer>> res = getNextColumn(partSolutions.get(boxesInStack - 1));
            if (res.getKey() != -1) {
                partSolutions.add(res.getValue());
                boxesInStack ++;
                if (maxH < res.getValue().get(res.getKey())) {
                    maxStackH = boxesInStack -1;
                    maxBoxId = res.getKey();
                    maxH = res.getValue().get(res.getKey());
                }
            } else {
                done = true;
            }
        }

        return backTrack(partSolutions, maxBoxId, maxStackH);
    }

    private List<Box> backTrack(List<List<Integer>> partSolutions, int topBoxId, int stackH) {
        List<Box> path = new ArrayList<>();
        Box curBox = boxes.get(topBoxId);
        path.add(curBox);
        if (stackH != 0) {
            int nextBoxId = -1;
            int nextSum = partSolutions.get(stackH).get(topBoxId) - boxes.get(topBoxId).getHeight();

            for (int i = 0; i < n; i++) {
                if (partSolutions.get(stackH - 1).get(i) == nextSum && boxes.get(i).getHierarchy(curBox) == 1) {
                    nextBoxId = i;
                    break;
                }
            }
            path.addAll(backTrack(partSolutions, nextBoxId, stackH - 1));
        }
        return path;
    }

    public List<Box> getBestStackByGraph() {
        WeightedVertexGraph graph = new WeightedVertexGraph();
        Map<Integer, Box> boxVertices = new HashMap<>();
        for (int i = 0; i < n; i ++) {
            boxVertices.put(graph.addVertex(new WeightedVertex(boxes.get(i).height)), boxes.get(i));
        }
        for (int i = 0; i < n; i ++) {
            for (int j = i + 1; j < n; j ++) {
                int r = boxes.get(i).getHierarchy(boxes.get(j));
                if (r == 1) {
                    graph.addEdge(i, j);
                } else if (r == -1) {
                    graph.addEdge(j, i);
                }
            }
        }
        List<WeightedVertex> vertices = graph.getLongestPath();
        List<Box> res = new ArrayList<>();
        vertices.forEach(v -> res.add(boxVertices.get(v.getId())));

        return res;
    }

    public static void main(String[] args) {
        List<Box> boxes = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 10000; i ++) {
            boxes.add(new Box(rand.nextInt(100), rand.nextInt(100), rand.nextInt(100)));
        }
        System.out.println("Boxes: " + boxes);
        CCIBoxTower tower = new CCIBoxTower(boxes);
        long t = System.currentTimeMillis();
        List<Box> bestStack = tower.getBestStackByDynamic();
        System.out.println(System.currentTimeMillis() - t);
        System.out.println("Best stack: " + bestStack);
        System.out.println("Height: " + tower.calculateHeight(bestStack));

        t = System.currentTimeMillis();
        List<Box> bestGraphStack = tower.getBestStackByGraph();
        System.out.println(System.currentTimeMillis() - t);
        System.out.println("Best stack: " + bestGraphStack);
        System.out.println("Height: " + tower.calculateHeight(bestGraphStack));
    }
}
