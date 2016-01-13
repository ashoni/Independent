package weightedGraphs;

import java.util.ArrayList;
import java.util.List;

public class WeightedVertex {
    private List<WeightedVertex> inEdges;
    private List<WeightedVertex> outEdges;
    private int id;
    private int weight;

    public WeightedVertex(int weight) {
        this.weight = weight;
        inEdges = new ArrayList<>();
        outEdges = new ArrayList<>();
    }

    public void addInEdge(WeightedVertex v) {
        inEdges.add(v);
    }

    public void removeInEdge(WeightedVertex v) {
        inEdges.remove(v);
    }

    public void addOutEdge(WeightedVertex v) {
        outEdges.add(v);
    }

    public void removeOutEdgeFrom(WeightedVertex v) {
        outEdges.remove(v);
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<WeightedVertex> getIn() {
        return inEdges;
    }

    public List<WeightedVertex> getOut() {
        return outEdges;
    }

    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }
}
