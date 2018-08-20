package com.keyeswest.mcts;

import sun.jvm.hotspot.utilities.Assert;



public class UCB1 {
   // private static double C = 2.0;

    public static double ucb1Value(int totalVisit, double nodeValue, int nodeVisit, double cValue){
        if (nodeVisit == 0){
            return Integer.MAX_VALUE;
        }

        return (nodeValue / (double)nodeVisit) + cValue * Math.sqrt(2 * Math.log(totalVisit)/ (double) nodeVisit);
    }

    public static Node findChildNodeWithBestUCBValue(Node node, double cValue){
        int parentVisit = node.getVisitCount();
        int childCount = node.getChildNodes().size();
        Assert.that(childCount > 0,"Must have child nodes");
        double nodeValue = node.getChildNodes().get(0).getValue();
        int visits = node.getChildNodes().get(0).getVisitCount();
        double maxValue = UCB1.ucb1Value(parentVisit, nodeValue, visits, cValue);
        Node returnNode = node.getChildNodes().get(0);
        for (int i=1;i< childCount;i++){
            Node childNode = node.getChildNodes().get(i);
            nodeValue = childNode.getValue();
            visits = childNode.getVisitCount();
            double ucbValue = UCB1.ucb1Value(parentVisit, nodeValue, visits, cValue);
            if (ucbValue > maxValue){
                maxValue = ucbValue;
                returnNode = childNode;
            }
        }

        return returnNode;


    }
}
