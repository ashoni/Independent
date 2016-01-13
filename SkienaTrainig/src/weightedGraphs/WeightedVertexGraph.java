package weightedGraphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WeightedVertexGraph {
    private Map<Integer, WeightedVertex> weightVertices;
    private int i = 0;

    public WeightedVertexGraph() {
        weightVertices = new HashMap<>();
    }

    public int addVertex(WeightedVertex v) {
        v.setId(i);
        weightVertices.put(i, v);
        i ++;
        return i - 1;
    }

    public void addEdge(int from, int to) {
        weightVertices.get(from).addOutEdge(weightVertices.get(to));
        weightVertices.get(to).addInEdge(weightVertices.get(from));
    }

    public List<WeightedVertex> sort() {
        int t = 0;
        Set<Integer> processed = new HashSet<>();
        Set<Integer> discovered = new HashSet<>();
        Map<Integer, Integer> entryTime = new HashMap<>();
        Map<Integer, Integer> exitTime = new HashMap<>();

        List<WeightedVertex> sorted = new ArrayList<>(weightVertices.values());

        for (WeightedVertex v : weightVertices.values()) {
            if (processed.contains(v.getId())) {
                continue;
            }
            t = dfs(entryTime, exitTime, v, discovered, processed, t);
        }
        sorted.sort((v1, v2) -> exitTime.get(v2.getId()).compareTo(exitTime.get(v1.getId())));
        return sorted;
    }

    public List<WeightedVertex> getLongestPath() {
        List<WeightedVertex> sorted = sort();

        int absMax = -1;
        int maxVertexId = -1;

        Map<Integer, Integer> maxSums = new HashMap<>();
        for (WeightedVertex v : sorted) {
            int maxIn = 0;
            for (WeightedVertex vIn : v.getIn()) {
                if (maxIn < maxSums.get(vIn.getId())) {
                    maxIn = maxSums.get(vIn.getId());
                }
            }
            maxSums.put(v.getId(), maxIn + v.getWeight());
            if (maxSums.get(v.getId()) > absMax) {
                absMax = maxSums.get(v.getId());
                maxVertexId = v.getId();
            }
        }

        List<WeightedVertex> bestBranch = new ArrayList<>();
        WeightedVertex cur = weightVertices.get(maxVertexId);
        int curSum = absMax;
        while (!cur.getIn().isEmpty()) {
            bestBranch.add(cur);
            for (WeightedVertex prev : cur.getIn()) {
                if (maxSums.get(prev.getId()) == curSum - cur.getWeight()) {
                    curSum -= cur.getWeight();
                    cur = prev;
                    break;
                }
            }
        }
        bestBranch.add(cur);
        return bestBranch;
    }

    public int dfs(Map<Integer, Integer> entryTime, Map<Integer, Integer> exitTime, WeightedVertex v,
                     Set<Integer> discovered, Set<Integer> processed, int t) {
        discovered.add(v.getId());
        t ++;
        entryTime.put(v.getId(), t);

        for (WeightedVertex vNext : v.getOut()) {
            if (!discovered.contains(vNext.getId())) {
                t = dfs(entryTime, exitTime, vNext, discovered, processed, t);
            }
        }

        processed.add(v.getId());
        t ++;
        exitTime.put(v.getId(), t);
        return t;
    }
}
