package com.omilia.test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In an automated dialogue system phone numbers may be uttered in many ways with different digit groupings
 *
 * (e.g. 2106930664 may be uttered as "210 69 30 6 6 4" or "210 69 664" etc).
 *
 * If the caller says "twentyfive" this could be transcribed as "25" or "20 5".
 *
 * The application deals with some of these issues.
 */

public class NNIDemo {
    public static void main(String[] args) {

        // ask the user for input phone number
        String inputPhoneNumber =  askUserForNumber();

        // interpret number. edw 2 implemantations ena base ena advanced
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

    // to interpretation tha ginei ws eksis:
    // otan tha pairnw substring me 2 digits tote tha uparxoun multiple interpretations
    // case 1: an to substring einai dekada p.x(30), tote koitaw to epomeno substring kai
    // 1.1 an auto einai monopsifio tote (p.x 30 6)
    //      1.1.1 to prwto interpretation einai to ena substring concat me to allo diladi 30 6 => 306
    //      1.1.2 to deutero interpretation einai to ena substring + to allo(ws numbers) diladi => 36
    // 1.2 an einai dipsifio tote (p.x 30 45)
    //      exoume mono ena interpretation 3045
    // case 2: an to substring den einai dekada (p.x 35)
    // exoume 2 interpretations:
    // 2.1 35
    // 2.2 30 5 => 305

    private static List<String> interpretNumberAdvancedLevel(String telephoneNumber) {

        String [] numbers = telephoneNumber.split(" ");
        List<String> listOfNumbers = Arrays.asList(numbers);

        // convert from List of Strings to List of Nodes
        List<Node> listOfNodes = listOfNumbers.stream()
                .map(n -> new Node(n))
                .collect(Collectors.toList());

        Node root = buildTree(listOfNodes);

        // ksanakalw ti locate leaves gia na parw mia lista me ola ta leaves
        List<Node> finalLeaves = new ArrayList<>();
        root.locateLeaves(finalLeaves);
        List<String> interpretations = new ArrayList<>();

        for(Node leaf : finalLeaves) {
            String finalNumber = leaf.getValue();
            Node parent = leaf.getParent();
            while(parent != null) {
                finalNumber = parent.getValue().concat(finalNumber);
                parent = parent.getParent();
            }
            interpretations.add((finalNumber));
        }

//        int i =0;
//        for(String number : interpretations) {
//            System.out.println("Interpretation " + i + ": " + number);
//            i++;
//        }

        return interpretations;


    }

    public static Node buildTree(List<Node> listOfNodes) {
        Node root = listOfNodes.get(0);

        for(Node incomingNode : listOfNodes) {

            // ignore if the first node is the incoming node
            if (root == incomingNode) {
                continue;
            }

            // find the leaves in the existing structure
            List<Node> leaves = new ArrayList<>();
            root.locateLeaves(leaves);

            // insert the new node under all leaves accordingly
            for (Node leaf : leaves) {
                Node incomingClone = new Node(incomingNode.getValue());
                leaf.addChild(incomingClone);
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

    // ena test oti den einai null, h oti den exei kena
    public static String askUserForNumber() { //TODO

        Scanner sc = new Scanner(System.in);
        System.out.println("Please press the number! Every grouping of digits must be separated by a space ' ' char");
        boolean validInput = true;

        String number = sc.nextLine();

        // Check that the user input was valid. That is, no bigger groupings than 3-digits groupings.
        String [] numbers = number.split(" ");
        for(String grouping : numbers) {
            if(grouping.length()>3) {
                System.out.println("The phone number you typed contains one or more grouping of digits with a size of: " + grouping.length());
                System.out.println("The maximum size of each grouping is 3 digits. Try again! ");
                askUserForNumber();
            }
        }

        return number;

    }

/*prwto vima input, deutero vima interpretation, trito vima validOrNot. mia methodo p legetai interpret. tha epistrefei enan arithmo(epeidi einai base level)
kai meta kanei to greek validation.
sto advanced level tha kanei to idio pragma alla tha epistrefei mia lista apo interpratations kai gia kathe mia apo autes, tha kanei to greek validation*/
}
