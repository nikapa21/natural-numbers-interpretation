package com.omilia.test;

import java.util.*;
import java.util.stream.Collectors;

/** -- Natural Numbers Interpretation Demo --
 * In an automated dialogue system phone numbers may be uttered in many ways with different digit groupings
 *
 * (e.g. 2106930664 may be uttered as "210 69 30 6 6 4" or "210 69 664" etc).
 *
 * If the caller says "twentyfive" this could be transcribed as "25" or "20 5".
 *
 * The application deals with some of these issues.
 *      The BASE level algorithm states if the output number is a valid Greek telephone number
 *      The ADVANCED level algorithm checks for possible ambiguities in number spelling and prints a list of interpretations (VALID or INVALID according to the base level)
 */

public class NNIDemo {
    public static void main(String[] args) {

        // ask the user for input phone number
        String inputPhoneNumber =  askUserForNumber();

        // interpret number. Two implementations: one BASE one ADVANCED
        List<String> interpretedNumbers = interpretNumberAdvancedLevel(inputPhoneNumber);
        // List<String> interpretedNumbers = interpretNumberBaseLevel(inputPhoneNumber);

        boolean isGreekValid;

        int i = 0;
        for(String interpretedNumber : interpretedNumbers) {
            isGreekValid = checkIfNumberIsValidGreek(interpretedNumber);
            printOutput(interpretedNumber, isGreekValid, ++i);
        }
    }

    private static List<String> interpretNumberBaseLevel(String telephoneNumber) {
        telephoneNumber = telephoneNumber.replaceAll(" ", "");
        // do the interpretation

        List<String> interpretedNumbers = new ArrayList<String>();
        interpretedNumbers.add(telephoneNumber);
        return interpretedNumbers;
    }

    private static List<String> interpretNumberAdvancedLevel(String telephoneNumber) {

        String [] numbers = telephoneNumber.split(" ");
        List<String> listOfNumbers = Arrays.asList(numbers);

        // convert from List of Strings to List of Nodes
        List<Node> listOfNodes = listOfNumbers.stream()
                .map(n -> new Node(n))
                .collect(Collectors.toList());

        Node root = buildTree(listOfNodes);

        // call locateLeaves again to get a list of all the final leaves
        // we will use root a starter node and locate all the leaves (all the steps of our algorithm)
        List<Node> finalLeaves = new ArrayList<>();
        root.locateLeaves(finalLeaves);

        List<String> interpretations = new ArrayList<>(); // here we keep our interpretations, we will return and then print this list

        findPathsInTheTree(finalLeaves, interpretations);

        return interpretations;

    }

    /**
     * This method will find all the paths in the tree(that is all the possible interpretations)
     * and for every path it will add it to the interpretations List
     * */
    private static void findPathsInTheTree(List<Node> finalLeaves, List<String> interpretations) {
        for(Node leaf : finalLeaves) {
            String finalNumber = leaf.getValue();
            Node parent = leaf.getParent();
            while(parent != null) {
                finalNumber = parent.getValue().concat(finalNumber);
                parent = parent.getParent();
            }
            interpretations.add((finalNumber));
        }
    }

    public static Node buildTree(List<Node> listOfNodes) {
        // First, we keep the root node (e.g Node with value "2")
        Node root = listOfNodes.get(0);

        for(Node incomingNode : listOfNodes) {

            // ignore if the first node is the incoming node
            if (root == incomingNode) {
                continue;
            }

            // We want to find the leaves in the existing structure. Wherever a leaf is, a new node can be added based on the tree logic
            List<Node> leaves = new ArrayList<>();
            root.locateLeaves(leaves);

            // insert the new node under all leaves accordingly
            for (Node leaf : leaves) {
                Node incomingClone = new Node(incomingNode.getValue()); // e.g Node with value "10"
                leaf.insertNode(incomingClone);
            }

        }
        return root;
    }

    private static void printOutput(String interpretedNumber, boolean isGreekValid, int i) {
        if (isGreekValid) {
            System.out.println("Interpretation " + i + ": " + interpretedNumber + " [phone number: VALID]");
        }
        else {
            System.out.println("Interpretation " + i + ": " + interpretedNumber + " [phone number: INVALID]");        }
    }

    public static boolean checkIfNumberIsValidGreek(String interpretedNumber) {

        if(interpretedNumber.length() == 10) {
            if (interpretedNumber.startsWith("2") || interpretedNumber.startsWith("69")) return true;
        }
        else if (interpretedNumber.length() == 14) {
            if (interpretedNumber.startsWith("00302") || interpretedNumber.startsWith("003069")) return true;
        }

        return false;
    }

    private static String askUserForNumber() {

        Scanner sc = new Scanner(System.in);

        System.out.println("Please press the number! Every grouping of digits must be separated by a space ' ' char");
        String number = sc.nextLine();

        // Check that the user input was valid. That is, no bigger groupings than 3-digits groupings.
        String [] numbers = number.split(" ");
        for (String grouping : numbers) {
            if (grouping.length() > 3) {
                System.out.println("The phone number you typed contains one or more grouping of digits with a size of: " + grouping.length());
                System.out.println("The maximum size of each grouping is 3 digits. Try again! ");
                askUserForNumber();
            }
        }

        return number;

    }
}
