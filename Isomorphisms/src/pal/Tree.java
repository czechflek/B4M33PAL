package pal;

import java.util.*;

public class Tree {
    List<Integer> neighbors[];
    int[] incoming;
    int[] incomingAlt;
    int[] outgoing;
    Set<Integer> cycle = new HashSet<>();
    int root;
    int numVs;
    Set<Integer> nonLeaves = new HashSet<>();
    Set<Integer> nonLeavesAlt = new HashSet<>();
    String[] labels;

    Tree(int numVs){
        this.numVs = numVs;
        neighbors = new LinkedList[numVs];
        incoming = new int[numVs];
        incomingAlt = new int[numVs];
        outgoing = new int[numVs];
        for (int i = 0; i < numVs; i++) {
            incoming[i] = Integer.MAX_VALUE;
            neighbors[i] = new LinkedList<>();
        }
    }

    void addEdge(int from, int to){
        neighbors[from].add(to);
        outgoing[from]++;

        if(incoming[to] == Integer.MAX_VALUE){
            incoming[to] = from;
        } else {
            cycle.add(to);
            incomingAlt[to] = from;
        }


    }

    void finishTree(){
        int i=0;

        //remove cycle
        for(Integer n: cycle){
            ListIterator<Integer> iterator = neighbors[n].listIterator();
            while(iterator.hasNext()){
                Integer next = iterator.next();
                if(cycle.contains(next)){
                    iterator.remove();
                    if(incoming[next] == n){
                        incoming[next] = incomingAlt[next];
                    }
                    outgoing[n]--;
                    break;
                }
            }
        }

        //find root
        while(incoming[i] != Integer.MAX_VALUE){
            i++;
        }
        root = i;

        //find leaves
        for (i = 0; i < numVs; i++) {
            if(outgoing[i] == 0){
                nonLeaves.add(incoming[i]); //nebo povolit duplikaty
            }
        }
    }

    String getCertificate(){
        labels = new String[numVs];
        for (int i = 0; i < numVs; i++) {
            labels[i] = "01";
        }

        boolean notLast = true;
        while(notLast) {
            for (Integer n : nonLeaves) {
                if(outgoing[n] == Integer.MAX_VALUE)
                    continue;
                List<String> subcerts = new ArrayList<>();
                for (Integer i : neighbors[n]) {
                    if (outgoing[i] == 0) {
                        subcerts.add(labels[i]);
                        if (outgoing[n] > 0) {
                            outgoing[n]--;
                            if (outgoing[n] == 0) {
                                nonLeavesAlt.add(incoming[n]);
                            }
                        }
                        outgoing[i] = Integer.MAX_VALUE;
                    }
                }

                if(!subcerts.isEmpty()) {
                    String lab = labels[n].substring(1, labels[n].length()-1);
                    subcerts.add(lab);
                    Collections.sort(subcerts);
                    String label = "0";
                    for (String s : subcerts) {
                        label += s;
                    }
                    label += "1";
                    labels[n] = label;
                }
            }

            nonLeaves.clear();
            Set<Integer> tmp = nonLeaves;
            nonLeaves = nonLeavesAlt;
            nonLeavesAlt = tmp;

            if(outgoing[root] == 0){
                notLast = false;
            }
        }
        //System.out.println(labels[root]);
        return labels[root] + getCycleCertificate();
    }

    String getCycleCertificate(){
        List<String> subcerts = new ArrayList<>();
        for(Integer i : cycle) {
            subcerts.add(labels[i]);
        }
        Collections.sort(subcerts);

        String label = "0";
        for (String s : subcerts) {
            label += s;
        }
        label += "1";

        return label;


    }
}
