package pathfinding;

import utils.GraphNode;

import java.util.HashMap;
import java.util.Map;

public class HeuristicNode extends GraphNode implements Comparable<HeuristicNode> {
    public int distanceFrom, distanceTo;

    public HeuristicNode(int id) {
        super(id);
    }

    public HeuristicNode(GraphNode gn) {
        super(gn.id);
        for (Map.Entry<Integer, Integer> e: gn.connections.entrySet()) {
            connections.put(e.getKey(), e.getValue());
        }
    }

    public GraphNode copy() {
        HeuristicNode n = new HeuristicNode(this.id);
        n.connections = new HashMap<>(connections);
        n.distanceFrom = distanceFrom;
        n.distanceTo = distanceTo;
        return n;
    }

    @Override
    public int compareTo(HeuristicNode o) {
        return Integer.compare(distanceFrom + distanceTo, o.distanceFrom + o.distanceTo);
    }
}
