package com.keyeswest.mcts;

import sun.jvm.hotspot.utilities.Assert;

import java.util.ArrayList;
import java.util.List;


public class UCB1 {

    private static double EPSILON = 1.0E-06;

    public static double ucb1Value(int totalVisit, double nodeValue, int nodeVisit, double cValue){
        if (nodeVisit == 0){
            return Integer.MAX_VALUE;
        }

        return (nodeValue / (double)nodeVisit) + cValue * Math.sqrt(2 * Math.log(totalVisit)/ (double) nodeVisit);
    }

    public static Node findChildNodeWithBestUCBValue(Node node, double cValue){
        List<Node> candidates = new ArrayList<>();
        int parentVisit = node.getVisitCount();
        int childCount = node.getChildNodes().size();
        Assert.that(childCount > 0,"Must have child nodes");
        Node candidateNode = node.getChildNodes().get(0);
        candidates.add(candidateNode);
        double nodeValue = candidateNode.getValue();

        int visits = candidateNode.getVisitCount();
        double maxValue = UCB1.ucb1Value(parentVisit, nodeValue, visits, cValue);

        for (int i=1;i< childCount;i++){
            Node childNode = node.getChildNodes().get(i);
            nodeValue = childNode.getValue();
            visits = childNode.getVisitCount();
            double ucbValue = UCB1.ucb1Value(parentVisit, nodeValue, visits, cValue);
            if (Math.abs(ucbValue-maxValue) < EPSILON){
                // approximately equivalent nodes
                candidates.add(childNode);
            }else if (ucbValue > maxValue){
                maxValue = ucbValue;
                candidateNode = childNode;
                candidates.clear();
                candidates.add(candidateNode);

            }
        }

        if (candidates.size()> 1){
            int randomSelection = (int)(Math.random() * candidates.size());
            candidateNode = candidates.get(randomSelection);
        }

        return candidateNode;


    }
}