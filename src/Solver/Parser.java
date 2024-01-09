package Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {

    public static StateSpace parse(Path path) throws FileNotFoundException, ParseException {
        return parse(path.toString());
    }

    public static StateSpace parse(String path) throws FileNotFoundException, ParseException {

        Scanner scanner = new Scanner(new File(path));
        String line = scanner.next();

        while ( ! line.equals("parity") ) {
            line = scanner.next();
        }

        line = scanner.next().replace(";","");

        int nrStates = Integer.parseInt(line) + 1;
        int[][] adj = new int[nrStates][];
        int[] priority = new int[nrStates];
        BitSet ownedByEven = new BitSet(nrStates);
        String[] names = new String[nrStates];
        int maxPriority = 0;

        ArrayList<Integer> inputOrder = new ArrayList<>(nrStates);
        HashMap<Integer, ArrayList<Integer>> revAdjMap = new HashMap<>();


        scanner.nextLine();

        int i;
        String[] split, tokens;
        int[] nums;
        while (scanner.hasNext()) {

            line = scanner.nextLine();
            split = line.split("\\s+");

            i = Integer.parseInt(split[0]);
            if (i >= nrStates) throw new ParseException("State identifier " + i + " larger than specified largest state identifier!", i);
            priority[i] = Integer.parseInt(split[1]);
            maxPriority = Integer.max(priority[i], maxPriority);

            if (inputOrder.contains(i)) inputOrder.remove(i);
            inputOrder.add(i);


            if (Integer.parseInt(split[2]) == 0) ownedByEven.set(i, true);

            // edges
            ArrayList<Integer> list;

            tokens = split[3].replace(";", "").split(",");
            nums = new int[tokens.length];
            for (int j = 0; j < tokens.length; j++) {
                nums[j] = Integer.parseInt(tokens[j]);

                // reverse
                list = revAdjMap.getOrDefault(nums[j], new ArrayList<>());
                list.add(i);
                revAdjMap.put(nums[j], list);
            }
            adj[i] = nums;

            if (split.length > 4) {
                names[i] = split[4].replace(";", "");
            }
        }

        scanner.close();

        // convert input order
        int[] order = new int[inputOrder.size()];
        for (int j = 0; j < inputOrder.size(); j++) {
            order[j] = inputOrder.get(j);
        }

        // convert reverse adjacency
        int[][] revAdj = new int[nrStates][];
        ArrayList<Integer> list;
        for (int j = 0; j < nrStates; j++) {
            list = revAdjMap.getOrDefault(j, new ArrayList<>());
            revAdj[j] = new int[list.size()];
            for (int k = 0; k < list.size(); k++) {
                revAdj[j][k] = list.get(k);
            }
        }
        File f = new File(path);


        return new StateSpace(nrStates, maxPriority, adj, revAdj, priority, ownedByEven, order, f.getName(), names);
    }

}
