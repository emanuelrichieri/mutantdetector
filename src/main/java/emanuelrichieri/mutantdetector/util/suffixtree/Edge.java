package emanuelrichieri.mutantdetector.util.suffixtree;

/**
 * Represents an Edge in the Suffix Tree.
 * It has a label and a destination Node
 */
class Edge {
    private String label;
    private Node destination;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Node getDestination() {
        return destination;
    }

    public void setDestination(Node dest) {
        this.destination = dest;
    }

    public Edge(String label, Node dest) {
        this.label = label;
        this.destination = dest;
    }

}
