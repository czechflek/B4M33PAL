package pal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            int from, to;

            //Init counts
            String line = in.readLine();
            String[] tokens = line.split(" ");
            int numTrees = Integer.parseInt(tokens[0]);
            int numVs = Integer.parseInt(tokens[1]);
            int numEdges = Integer.parseInt(tokens[2]);

            Tree[] forest = new Tree[numTrees];
            for(int i=0; i < numTrees; i++){
                Tree tree = new Tree(numVs);
                for (int j = 0; j < numEdges; j++) {
                    line = in.readLine();
                    tokens = line.split(" ");
                    from = Integer.parseInt(tokens[0]) - 1;
                    to = Integer.parseInt(tokens[1]) - 1;
                    tree.addEdge(from, to);
                }
                tree.finishTree();
                forest[i] = tree;
            }

            HashMap<String, Integer> certs = new HashMap<>();
            for(Tree t: forest){
                String cert = t.getCertificate();
                //System.out.println(cert);
                Integer val = certs.get(cert);
                if(val != null){
                    certs.put(cert, val + 1);
                } else {
                    certs.put(cert, 1);
                }
            }

            List<Integer> res = new ArrayList<>(certs.values());
            Collections.sort(res);
            String result = "";
            for (int i = 0; i < res.size() - 1; i++) {
                result += res.get(i) + " ";
            }
            result += res.get(res.size() - 1);
            System.out.println(result);
        } catch (IOException ex) {
            System.err.println("Whopsie");
        }
    }
}
