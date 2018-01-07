package net.csobik.cmtree.service;

/**
 * @author jirisobotik
 * @version 1.0.0
 * @since 06.01.18
 */
public class NodeId {

  private final Long id;

  public NodeId(Long id) {
    this.id = id;
  }

  public NodeId() {
    this.id = null;
  }

  public Long getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NodeId nodeId = (NodeId) o;
    return id != null ? id.equals(nodeId.id) : nodeId.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
