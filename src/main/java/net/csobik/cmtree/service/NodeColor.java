package net.csobik.cmtree.service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

import net.csobik.cmtree.service.types.Color;

/**
 * @author jirisobotik
 * @version 1.0.0
 * @since 06.01.18
 */
public class NodeColor implements Observer {

  private final NodeId nodeId;
  private Color color;

  private long priority;

  private List<NodeColor> children = new LinkedList<>();
  private NodeColor parent = null;

  //Introducing the dummy constructor
  public NodeColor() {
    nodeId = null;
  }

  public NodeColor(NodeId nodeId, Color color, long priority) {
    this.nodeId = nodeId;
    this.color = color;
    this.priority = priority;
    this.children = new LinkedList<>();
  }

  public NodeColor addChild(NodeColor child) {
    NodeColor childNode = new NodeColor(child.getNodeId(), child.getColor(), child.getPriority());
    childNode.parent = this;
    this.children.add(childNode);
    return childNode;
  }

  public NodeId getNodeId() {
    return nodeId;
  }

  public Color getColor() {
    return color;
  }

  public long getPriority() {
    return priority;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public void setPriority(long priority) {
    this.priority = priority;
  }

  public List<NodeColor> getChildren() {
    return children;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NodeColor nodeColor = (NodeColor) o;

    if (priority != nodeColor.priority) return false;
    if (nodeId != null ? !nodeId.equals(nodeColor.nodeId) : nodeColor.nodeId != null) return false;
    return color == nodeColor.color;

  }

  @Override
  public int hashCode() {
    int result = nodeId != null ? nodeId.hashCode() : 0;
    result = 31 * result + (color != null ? color.hashCode() : 0);
    result = 31 * result + (int) (priority ^ (priority >>> 32));
    return result;
  }

  @Override
  public void update(Observable o, Object arg) {

    List<NodeColor> initColors = (List<NodeColor>) arg;
    NodeColorService observable = (NodeColorService) o;
    NodeColor changedColor = observable.getObserver();

    if (changedColor != null && nodeId.equals(changedColor.getNodeId())) {

      // set node color
      this.setColor(changedColor.getColor());
      this.setPriority(changedColor.getPriority());

      // reset parent to default value if there is no higher values on this siblings
      setParentPriority(initColors, observable);
    }

  }

  private void setParentPriority(List<NodeColor> initColors, NodeColorService observable) {
    if (parent != null && parent.getChildren() != null) {

      // does any child have higher value than parent?
      NodeColor newValue = parent.getChildren().stream()
                                          .max(Comparator.comparing(NodeColor::getPriority))
                                          .orElseThrow(NoSuchElementException::new);


      if (newValue.getPriority() < parent.getPriority()) {
        // find init value
        newValue = initColors.stream().filter(n -> n.getNodeId().equals(parent.getNodeId()))
                       .findFirst().orElseThrow(NoSuchElementException::new);
      }

      parent.setPriority(newValue.getPriority());
      parent.setColor(newValue.getColor());

      // traverse tree up to root
      observable.colorChanged(parent);
    }
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("NodeColor{");
    sb.append("nodeId=").append(nodeId);
    sb.append(", color=").append(color);
    sb.append(", priority=").append(priority);
    sb.append('}');
    return sb.toString();
  }

  public String printTree(int increment) {
    String s = "";
    String inc = "";
    for (int i = 0; i < increment; ++i) {
      inc = inc + " ";
    }
    s = inc + "--- " + toString();
    for (NodeColor child : children) {
      s += "\n" + child.printTree(increment + 2);
    }
    return s;
  }
}
