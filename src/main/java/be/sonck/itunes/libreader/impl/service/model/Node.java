package be.sonck.itunes.libreader.impl.service.model;

/**
 * Created by johansonck on 08/02/16.
 */
public class Node {

    private final String name;
    private final Node parentNode;


    public Node(String name, Node parentNode) {
        this.name = name;
        this.parentNode = parentNode;
    }

    public String getName() {
        return name;
    }

    public Node getParentNode() {
        return parentNode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (parentNode != null) {
            builder.append(parentNode.toString())
                .append(":");
        }

        return builder.append(name).toString();
    }
}
