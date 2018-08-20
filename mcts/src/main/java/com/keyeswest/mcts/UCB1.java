package com.keyeswest.mcts;

import sun.jvm.hotspot.utilities.Assert;

import java.util.Collections;
import java.util.Comparator;

public class UCB1 {
    private static double C = 2.0;

    public static double ucb1Value(int totalVisit, double nodeValue, int nodeVisit){
        if (nodeVisit == 0){
            return Integer.MAX_VALUE;
        }

        return (nodeValue / (double)nodeVisit) + C * Math.sqrt(Math.log(totalVisit)/ (double) nodeVisit);
    }

    public static Node findNodeWithBestUCBValue(Node node){
        int parentVisit = node.getVisitCount();
        int childCount = node.getChildNodes().size();
        Assert.that(childCount > 0,"Must have child nodes");
        double nodeValue = node.getChildNodes().get(0).getValue();
        int visits = node.getChildNodes().get(0).getVisitCount();
        double maxValue = UCB1.ucb1Value(parentVisit, nodeValue, visits);
        Node returnNode = node.getChildNodes().get(0);
        for (int i=1;i< childCount;i++){
            Node childNode = node.getChildNodes().get(i);
            nodeValue = childNode.getValue();
            visits = childNode.getVisitCount();
            double ucbValue = UCB1.ucb1Value(parentVisit, nodeValue, visits);
            if (ucbValue > maxValue){
                maxValue = ucbValue;
                returnNode = childNode;
            }
        }

        return returnNode;


       // return Collections.max(node.getChildNodes(),
       //         Comparator.comparing(n -> ucb1Value(parentVisit,n.getValue(),n.getVisitCount())));
    }
}
