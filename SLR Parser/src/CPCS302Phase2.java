/*Project: Part2-Non-Recursive Predictive Parser
 * Course: CPCS 302
 * Section: CA
 * Leader: Ali Habibullah - 1945958
 * Member 1: Khalid Al-Ghamdi - 1936811
 * Member 2: Feras Al-Hilabi - 1945814
 * Completion date: 3/26/2022 7:35PM
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class CPCS302Phase2 {
    public static void main(String[] args) throws FileNotFoundException {
        Stack<String> stack = new Stack<>();
        File file = new File("input.txt");
        if (!file.exists()) {
            System.out.println("File doesn't exit");
            System.exit(0);
        }
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        System.out.println("Left most derivation for the arithmetic expression " + line + ":");
        String[] input = line.split(" ");
        String[][] table = MakeTable();
        ArrayList<String> terminals = getTerminals();
        HashMap<String, Integer> noneTerminalMap = getNoneTerminalMap();
        HashMap<String, Integer> terminalMap = getTerminalMap();
        int ip = 0;
        String currentCharInput = input[ip];
        stack.push("$");
        stack.push("E");
        while (true) {
            String stackTop = stack.peek();
            if (stackTop.equals("$") || terminals.contains(stackTop)) {
                if (stackTop.equals(currentCharInput)) {
                    if (stackTop.equals("$")) {
                        System.out.println("Parsing successfully halts\n");
                        if (!scanner.hasNext()) {
                            System.exit(0);
                        }
                        line = scanner.nextLine();
                        System.out.println("Left most derivation for the arithmetic expression " + line + ":");
                        input = line.split(" ");
                        ip = -1;
                        stack.pop();
                        currentCharInput = input[++ip];
                        System.out.println();
                        stack.push("$");
                        stack.push("E");
                        continue;
                    }
                    stack.pop();
                    currentCharInput = input[++ip];
                    System.out.println();
                } else {
                    errorMethod(stackTop, currentCharInput);
                    System.out.println("ERROR");
                }
            } else {
                try {
                    if (table[noneTerminalMap.get(stackTop)][terminalMap.get(currentCharInput)].equals("ERROR")) {
                        errorMethod(stackTop, currentCharInput);
                    } else {
                        stack.pop();
                        String temp = table[noneTerminalMap.get(stackTop)][terminalMap.get(currentCharInput)];
                        pushReverse(stack, temp);
                        System.out.println("Used rule is: " + temp);
                    }
                } catch (Exception e) {
                    errorMethod(stackTop, currentCharInput);
                }
            }
        }
    }

    private static void pushReverse(Stack<String> stack, String temp) {
        String[] production = temp.split(" ");
        for (int i = production.length - 1; i > 1; i--) {
            if (production[i].equals("^")) {
                //skip
                continue;
            }
            stack.push(production[i]);
        }
    }

    private static HashMap<String, Integer> getTerminalMap() {
        HashMap<String, Integer> terminalMap = new HashMap<>();
        terminalMap.put("id", 0);
        terminalMap.put("+", 1);
        terminalMap.put("*", 2);
        terminalMap.put("(", 3);
        terminalMap.put(")", 4);
        terminalMap.put("$", 5);
        return terminalMap;
    }

    private static HashMap<String, Integer> getNoneTerminalMap() {
        HashMap<String, Integer> noneTerminalMap = new HashMap<>();
        noneTerminalMap.put("E", 0);
        noneTerminalMap.put("Ep", 1);
        noneTerminalMap.put("T", 2);
        noneTerminalMap.put("Tp", 3);
        noneTerminalMap.put("F", 4);
        return noneTerminalMap;
    }

    private static ArrayList<String> getTerminals() {
        ArrayList<String> terminals = new ArrayList<>();
        terminals.add("id");
        terminals.add("+");
        terminals.add("*");
        terminals.add("(");
        terminals.add(")");
        return terminals;
    }

    private static void errorMethod(String stackTop, String currentCharInput) {
        System.out.println("Syntax error: ");
        System.out.print("Error at stack point: " + stackTop);
        System.out.println(" , Does not match input at: " + currentCharInput);
        System.out.println("Closing program");
        System.exit(0);
    }

    public static String[][] MakeTable() {
        String[][] table = new String[5][6];
        table[0][0] = "E -> T Ep";
        table[0][1] = "ERROR";
        table[0][2] = "ERROR";
        table[0][3] = "E -> T Ep";
        table[0][4] = "ERROR";
        table[0][5] = "ERROR";
        table[1][0] = "ERROR";
        table[1][1] = "Ep -> + T Ep";
        table[1][2] = "ERROR";
        table[1][3] = "ERROR";
        table[1][4] = "Ep -> ^";
        table[1][5] = "Ep -> ^";
        table[2][0] = "T -> F Tp";
        table[2][1] = "ERROR";
        table[2][2] = "ERROR";
        table[2][3] = "T -> F Tp";
        table[2][4] = "ERROR";
        table[2][5] = "ERROR";
        table[3][0] = "ERROR";
        table[3][1] = "Tp -> ^";
        table[3][2] = "Tp -> * F Tp";
        table[3][3] = "ERROR";
        table[3][4] = "Tp -> ^";
        table[3][5] = "Tp -> ^";
        table[4][0] = "F -> id";
        table[4][1] = "ERROR";
        table[4][2] = "ERROR";
        table[4][3] = "F -> ( E )";
        table[4][4] = "ERROR";
        table[4][5] = "ERROR";
        return table;
    }
}
