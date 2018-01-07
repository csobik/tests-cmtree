package net.csobik.cmtree.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;

public class NodeColorService extends Observable implements NodeColorChangeObservable, NodeColorChangeObserver {

    private final List<NodeColor> initColors;
    private final List<NodeColor> treeNodeParent = new LinkedList<>();
    private NodeColor observer;

    private List<NodeColor> observers = new ArrayList<>();

    /**
     * From given map constructor creates a tree with {@link NodeColor} nodes
     * every node is registered as observe and this service extends abstract {@link Observable} class
     * given map of subnodes does not meet 'true' tree structure
     * if you need more deep structures you could define another constructor with tree structure as parameter
     *
     * @param subnodesMap not really tree structure
     * @param initColors
     */
    public NodeColorService (Map<NodeId, List<NodeId>> subnodesMap, List<NodeColor> initColors) {
        this.initColors = initColors;

        // create tree from Map
        for (Map.Entry<NodeId, List<NodeId>> sub : subnodesMap.entrySet()) {
            Optional<NodeColor> nodeColor = getNodeInitColor(sub.getKey());

            if (nodeColor.isPresent()) {
                NodeColor parent = new NodeColor(nodeColor.get().getNodeId(),nodeColor.get().getColor(),nodeColor.get().getPriority());
                // find it in tree if we have it already
                Optional<NodeColor> parentOpt = getObserver(nodeColor.get().getNodeId());
                if (parentOpt.isPresent()) {
                    parent = parentOpt.get();
                } else {
                    treeNodeParent.add(parent);
                }

                // add observer for child in tree
                addNodeColorChangeObserver(parent);
                for (NodeId s : sub.getValue()) {
                    Optional<NodeColor> subChildNodeColor = getNodeInitColor(s);
                    NodeColor finalParent = parent;
                    subChildNodeColor.ifPresent(subChild->{
                        // add observer for child in tree
                        NodeColor child = new NodeColor(subChild.getNodeId(),subChild.getColor(),subChild.getPriority());
                        addNodeColorChangeObserver(finalParent.addChild(child));
                    });
                }
            }
        }
    }

    /**
     * find init data for given nodeId
     *
     * @param sub
     * @return
     */
    private Optional<NodeColor> getNodeInitColor(NodeId sub) {
        return initColors.stream().filter(n -> n.getNodeId().equals(sub))
                   .findFirst();
    }

    /**
     * find Observer by given nodeId
     *
     * @param sub
     * @return
     */
    private Optional<NodeColor> getObserver(NodeId sub) {
        return observers.stream().filter(n -> n.getNodeId().equals(sub))
                   .findFirst();
    }

    /**
     * register observer - actually node in the tree
     *
     * @param observer
     */
    @Override
    public void addNodeColorChangeObserver(NodeColor observer) {
        if (observer == null) {
            throw new NullPointerException();
        }

        addObserver(observer);
        observers.add(observer);
    }

    public NodeColor getObserver() {
        return observer;
    }

    /**
     * notify all observers to handle changed color
     *
     * @param nodeColor
     */
    @Override
    public void colorChanged(NodeColor nodeColor) {
        this.observer = nodeColor;
        setChanged();
        notifyObservers(initColors);
    }

    /**
     * simply print the tree
     *
     * @param preStr
     */
    public void print(String preStr) {
        System.out.println(preStr);
        for (NodeColor color : treeNodeParent) {
            System.out.println(color.printTree(0));
        }
        System.out.println();
    }
}
