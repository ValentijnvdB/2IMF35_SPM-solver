import java.io.File;
import java.io.FileNotFoundException;
import java.util.BitSet;
import java.util.Scanner;

public class Parser {

    public static StateSpace parse(String path) throws FileNotFoundException {

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
        int maxPriority = 0;

        scanner.nextLine();

        int i;
        String[] split, tokens;
        int[] nums;
        while (scanner.hasNext()) {

            line = scanner.nextLine();
            split = line.split("\\s+");

            i = Integer.parseInt(split[0]);
            priority[i] = Integer.parseInt(split[1]);
            maxPriority = Integer.max(priority[i], maxPriority);

            if (Integer.parseInt(split[2]) == 0) ownedByEven.set(i, true);

            // edges

            tokens = split[3].replace(";", "").split(",");
            nums = new int[tokens.length];
            for (int j = 0; j < tokens.length; j++) {
                nums[j] = Integer.parseInt(tokens[j]);
            }
            adj[i] = nums;
        }

        scanner.close();

        return new StateSpace(nrStates, maxPriority, adj, priority, ownedByEven);
    }

}
