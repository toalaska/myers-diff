package com.ziheliu;

import java.util.Date;
import java.util.List;

public class Difference {
  private String[] oldLines;
  private String[] newLines;

  public Difference(String[] oldLines, String[] newLines) {
    this.oldLines = oldLines;
    this.newLines = newLines;
  }

  public Difference(String oldString, String newString) {
    this.oldLines = oldString.split("\n");
    this.newLines = newString.split("\n");
  }

  public String[] getOldLines() {
    return oldLines;
  }

  public String[] getNewLines() {
    return newLines;
  }

  /**
   * Show diff in the console between oldString and new String.
   */
  public String getDiffString() {
    Myers myers = new Myers(oldLines, newLines);
    myers.diff();

    List<Node> path = myers.getPath();

    return this.getDiffString(oldLines, newLines, path);
  }

  private String getDiffString(String[] oldLines, String[] newLines, List<Node> path) {
    Node prevNode = path.get(0);
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 1; i < path.size(); i++) {
      Node node = path.get(i);
      int gapDis = (node.getCoordinateX() - node.getCoordinateY())
          - (prevNode.getCoordinateX() - prevNode.getCoordinateY());

      int posX = prevNode.getCoordinateX();
      int posY = prevNode.getCoordinateY();

      if (gapDis == 1) {
        // right
        stringBuilder
            .append(this.getStringWithRed("-\t" + oldLines[prevNode.getCoordinateX()]))
            .append("\n");
        posX++;
      } else if (gapDis == -1) {
        // down
        stringBuilder
            .append(this.getStringWithGreen("+\t" + newLines[prevNode.getCoordinateY()]))
            .append("\n");
        posY++;
      }

      for (; posX < node.getCoordinateX() && posY < node.getCoordinateY()
          && posX < oldLines.length && posY < newLines.length; posX++, posY++) {
        stringBuilder
            .append("\t")
            .append(oldLines[posX])
            .append("\n");
      }

      prevNode = node;
    }
    return stringBuilder.toString();
  }

  private String getStringWithRed(String str) {
    return "\033[31m" + str + "\033[0m";
  }


  private String getStringWithGreen(String str) {
    return "\033[32m" + str + "\033[0m";
  }

  public String merge() {
    Myers myers = new Myers(oldLines, newLines);
    myers.diff();

    List<Node> path = myers.getPath();

    return this.mergeString(oldLines, newLines, path);
  }

  private String mergeString(String[] oldLines, String[] newLines, List<Node> path) {
    Node prevNode = path.get(0);
    StringBuilder stringBuilder = new StringBuilder();
    boolean prevAddFlag=false;
    for (int i = 1; i < path.size(); i++) {
      Node node = path.get(i);
      int gapDis = (node.getCoordinateX() - node.getCoordinateY())
              - (prevNode.getCoordinateX() - prevNode.getCoordinateY());

      int posX = prevNode.getCoordinateX();
      int posY = prevNode.getCoordinateY();

      if (gapDis == 1) {
        prevAddFlag=false;
        // right
        stringBuilder
//                .append( "// " + oldLines[prevNode.getCoordinateX()])
                .append(oldLines[prevNode.getCoordinateX()])
                .append("\n");
        posX++;
      } else if (gapDis == -1) {
        if(!prevAddFlag){
          stringBuilder .append("// "+new Date()+" 添加\n");
          prevAddFlag=true;
        }
        // down
        stringBuilder
//                .append("// "+new Date()+" 添加\n")
                .append(newLines[prevNode.getCoordinateY()])
                .append("\n");
        posY++;
      }

      for (; posX < node.getCoordinateX() && posY < node.getCoordinateY()
              && posX < oldLines.length && posY < newLines.length; posX++, posY++) {
        stringBuilder
                .append("")
                .append(oldLines[posX])
                .append("\n");
        prevAddFlag=false;
      }

      prevNode = node;
    }
    return stringBuilder.toString();
  }
}
