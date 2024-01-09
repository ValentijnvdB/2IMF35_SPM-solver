package Solver;

import Strategies.*;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;

import static java.lang.System.exit;

public class SPMSolver {

    public static int lifted;

    private static final String[] FILE_STRINGS = new String[]{"-g", "--game", "-f", "--file"};

    private static final String[] FOLDER_STRINGS = new String[]{"-a", "--all", "-d", "--dir"};

    private static final String[] OUT_STRINGS = new String[]{"-o", "--out"};

    private static final String[] STRATEGY_STRINGS = new String[]{"-s", "--strategy"};

    private static final String[] DETAILED_WINNER_STRINGS = new String[]{"-dw", "--detailed"};

    public static void main(String[] args) {

        if (args.length == 0) {
            SimpleLogger.writeln("Please supply arguments!");
            printHelp();
            exit(1);
        }


        ArrayList<StateSpace> games = new ArrayList<>();

        Path basePath = Paths.get(System.getProperty("user.dir"));

        Strategy strategy = Strategy.REVERSE;

        boolean detailed = false;


        for (int i = 0; i < args.length; i++) {
            String command = args[i].toLowerCase();

            try {

                if (contains(FILE_STRINGS, command)) {
                    String[] files = args[++i].split(",");
                    for (String file : files) {
                        Path path = basePath.resolve(file);
                        if (Files.exists(path)) {
                            games.add(Parser.parse(path));
                        } else {
                            SimpleLogger.warning(file + " was not found!");
                        }
                    }
                }

                else if (contains(FOLDER_STRINGS, command)) {
                    File[] files = basePath.toFile().listFiles((dir, name) -> name.toLowerCase().endsWith(".gm"));
                    for (File file : files) {
                        games.add(Parser.parse(basePath.resolve(file.toPath())));
                    }
                }

                else if (contains(OUT_STRINGS, command)) {
                    String outFile = args[++i];
                    SimpleLogger.outputToFile(new File(outFile));
                }

                else if (contains(STRATEGY_STRINGS, command)) {

                    switch (args[++i]) {
                        case "in" -> strategy = Strategy.INPUT;
                        case "rd" -> strategy = Strategy.RANDOM;
                        case "ls" -> strategy = Strategy.LASSO;
                        case "rv" -> strategy = Strategy.REVERSE;
                        case "all" -> strategy = Strategy.ALL;
                    }
                }

                else if (contains(DETAILED_WINNER_STRINGS, command)) {
                    detailed = true;
                }


            } catch (Exception e) {
                SimpleLogger.exception(e);
            }
        }

        GenericStrategy[] strategies;
        int k = 1;
        for (StateSpace game : games) {

            // resolve strategy
            if (strategy == Strategy.ALL) {
                k = 4;
                strategies = new GenericStrategy[k];
                strategies[0] = new InputOrder(game.getOrder());
                strategies[1] = new RandomOrder(game.NROF_STATES);
                strategies[2] = new LassoStrategy(game);
                strategies[3] = new ReverseEdgeStrategy(game);
            } else {
                strategies = new GenericStrategy[k];
                strategies[0] = resolveStrategy(strategy, game);
            }


            // solve with every strategy
            int[] iterations = new int[k];
            BitSet oddWins = new BitSet();

            for (int i = 0; i < k; i++) {
                lifted = 0;
                oddWins = Solver.solve(game, strategies[i]);
                iterations[i] = lifted;
            }

            // write output
            SimpleLogger.writeln("Game: " + game.NAME);

            if (detailed) {
                printDetailedOutput(oddWins, game);
            } else {
                printSimpleOutput(oddWins, game);
            }

            if (k==4) {
                SimpleLogger.writeln("");
                SimpleLogger.writeln("Lifted:");
                SimpleLogger.writeln("InputOrder          = " + iterations[0]);
                SimpleLogger.writeln("RandomOrder         = " + iterations[1]);
                SimpleLogger.writeln("LassoStrategy       = " + iterations[2]);
                SimpleLogger.writeln("ReverseEdgeStrategy = " + iterations[3]);
            }
            SimpleLogger.writeln("");
            SimpleLogger.writeln("");

        }

        SimpleLogger.close();
    }

    private static void printSimpleOutput(BitSet oddWins, StateSpace game) {
        SimpleLogger.writeln("Initial state is won by " + (oddWins.get(0) ? "ODD" : "EVEN") );
        SimpleLogger.writeln("Odd wins " + oddWins.cardinality() + " vertices.");
        SimpleLogger.writeln("Even wins " + (game.NROF_STATES - oddWins.cardinality()) + " vertices.");
    }

    private static void printDetailedOutput(BitSet oddWins, StateSpace game) {
        SimpleLogger.writeln("Odd wins the following states : " + oddWins);
        BitSet evenWins = new BitSet();
        for (int i = 0; i < game.NROF_STATES; i++) {
            if (!oddWins.get(i)) evenWins.set(i);
        }
        SimpleLogger.writeln("Even wins the following states: " + evenWins);
        SimpleLogger.writeln("");
    }

    private static void printHelp() {

    }

    private static GenericStrategy resolveStrategy(Strategy strategy, StateSpace game) {
        GenericStrategy out;
        switch (strategy) {
            case INPUT      -> out = new InputOrder(game.getOrder());
            case RANDOM     -> out = new RandomOrder(game.NROF_STATES);
            case LASSO      -> out = new LassoStrategy(game);
            case REVERSE    -> out = new ReverseEdgeStrategy(game);
            default         -> throw new IllegalArgumentException("Should not be called with ALL!");
        }
        return out;
    }

    private static boolean contains(String[] arr, String elem) {
        for (String cur : arr) {
            if (elem.equals(cur)) return true;
        }
        return false;
    }


    private enum Strategy {
        INPUT,
        RANDOM,
        LASSO,
        REVERSE,
        ALL
    }


}
