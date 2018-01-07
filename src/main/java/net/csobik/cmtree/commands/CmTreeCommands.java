package net.csobik.cmtree.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import net.csobik.cmtree.service.NodeColor;
import net.csobik.cmtree.service.NodeId;
import net.csobik.cmtree.service.NodeColorService;
import net.csobik.cmtree.service.types.Color;

/**
 * @author jirisobotik
 * @version 1.0.0
 * @since 06.01.18
 */
@ShellComponent()
public class CmTreeCommands {

  NodeColorService service = null;

  @ShellMethod("Initialize basic tree")
  public void init() {
    Map<NodeId, List<NodeId>> nodes = new HashMap<>();

    NodeId node1 = new NodeId(1L);
    NodeId node2 = new NodeId(2L);
    NodeId node3 = new NodeId(3L);
    NodeId node4 = new NodeId(4L);

    List<NodeId> node1subnodes = Arrays.asList(node2, node3);
    List<NodeId> node3subnodes = Arrays.asList(node4);
    nodes.put(node1, node1subnodes);
    nodes.put(node3, node3subnodes);

    List<NodeColor> initColors = new ArrayList<>();
    NodeColor color1 = new NodeColor(node1, Color.black, 30);
    NodeColor color2 = new NodeColor(node2, Color.red, 50);
    NodeColor color3 = new NodeColor(node3, Color.red, 20);
    NodeColor color4 = new NodeColor(node4, Color.red, 30);
    initColors.addAll(Arrays.asList(color1, color2, color3, color4));

    service = new NodeColorService(nodes, initColors);
    service.print("*** INITIAL STATE ***");

  }


  @ShellMethod("Initialize basic tree")
  public void print() {
    if (service == null) {
      init();

    } else {
      service.print("*** ACTUAL STATE ***");
    }
  }

  @ShellMethod("Initialize basic tree (ie: '{\"nodeId\":{\"id\":4},\"color\":\"black\",\"priority\":100}')")
  public void changeColors(String newNodeColor) throws IOException {
    if (service == null) {
      init();
    }

    ObjectMapper mapper = new ObjectMapper();
    if (!StringUtils.isEmpty(newNodeColor.trim())) {
      NodeColor color = mapper.readValue(newNodeColor, NodeColor.class);
      service.colorChanged(color);
    }

    print();
  }
}