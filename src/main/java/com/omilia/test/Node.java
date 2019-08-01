package com.omilia.test;

import java.util.List;

public class Node {
    private Node leftChild;
    private Node rightChild;
    private Node middleChild;
    private Node parent;
    private String value;

    public Node(String value) {
        this.value = value;
    }

    /**
     * Creates the child-parent relationships between nodes ( leaf and incoming )
     * Each incoming node from the list of notes may be parent-conflicting, self-conflicting or both
     * Method declarations explain more about when a node is parent-conflicting or self-conflicting
     *
     * */

    public void insertNode(Node incomingNode) {

        // as a convention the incoming node will be the left child of the current leaf node
        this.leftChild = incomingNode;
        // when a child is added, it needs to know its parent
        incomingNode.parent = this;

        if(incomingNode.isParentConflicting()) {
            if(this.parent != null) {

                // first we keep the sum of incoming node's value plus its parent(this) value. (e.g parent: 700, incoming node: 22, sum => 722)
                int addition = Integer.parseInt(this.getValue()) + Integer.parseInt(incomingNode.getValue());
                Node sum = new Node(String.valueOf(addition)); // so a new Node(e.g 722) is created

                if(incomingNode.parent.value.length() <= 2) { // This is for the case when the parent node is 2-digits

                    // (e.g parent(this): 70, incoming node: 6. The parent.leftchild = node(6) as assigned in line 24
                    // But we also need to create a "sibling" of the parent node which will hold the sum value => this.parent.rightChild = node(76)
                    this.parent.rightChild = sum;
                    sum.parent = this.parent;

                } else if (incomingNode.parent.value.length() == 3) { // This is for the case when the parent node is 3-digits

                    this.parent.middleChild = sum;
                    sum.parent = this.parent;

                    Integer hundred = getHundredOfParent(incomingNode);

                    if(incomingNode.getValue().length() == 2) {

                        Integer decade = getDecadeFromTwoDigits(incomingNode);

                        Node sumOfHundredDecade = new Node(String.valueOf(hundred+decade)); // a new Node(700+60 = 760) is created

                        int monada = getMonadaFromTwoDigits(incomingNode);
                        Node monad = new Node(String.valueOf(monada)); //(e,g 69. Here we keep the 9 value to use below)

                        if(monada !=0) {
                            this.parent.rightChild = sumOfHundredDecade; // add the sumOfHundredDecade(760) node to the tree accordingly
                            sumOfHundredDecade.parent = this.parent;

                            sumOfHundredDecade.leftChild = monad; // add the monad(9) node to the tree accordingly
                            monad.parent = sumOfHundredDecade;
                        }
                    }
                }
            }
        }

        if (incomingNode.isSelfConflicting()) {
            if(incomingNode.getValue().length() == 3) {

                Integer hundred = getHundredFromThreeDigits(incomingNode);
                Integer decade = getDecadeFromThreeDigits(incomingNode);
                Integer monad = getMonadFromThreeDigits(incomingNode);

                // create nodes
                Node a = new Node(String.valueOf(hundred));
                Node sumOfHundredDecade = new Node(String.valueOf(hundred+decade));
                Node b = new Node(String.valueOf(decade));
                Node sumOfDecadeMonad = new Node(String.valueOf(decade + monad));
                Node c = new Node(String.valueOf(monad));
                Node d = new Node(String.valueOf(monad));

                this.rightChild = a;
                a.parent = this;

                a.leftChild = b;
                b.parent = a;

                if(monad != 0) { // now we care only for 3-digits
                    a.rightChild = sumOfDecadeMonad;
                    sumOfDecadeMonad.parent = a;

                    b.leftChild = c;
                    c.parent = b;

                    this.middleChild = sumOfHundredDecade;
                    sumOfHundredDecade.parent = this;

                    sumOfHundredDecade.leftChild = d;
                    d.parent = sumOfHundredDecade;
                }
            }
            else { // definitely incoming node.length() here is equal to 2
                Integer decade = getDecadeFromTwoDigits(incomingNode);
                Integer monad = Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(1)));

                // create nodes
                Node a = new Node(String.valueOf(decade));
                Node b = new Node(String.valueOf(monad));

                // assign them accordingly to the tree
                this.rightChild = a;
                a.parent = this;

                a.leftChild = b;
                b.parent = a;
            }
        }
    }

    private Integer getMonadFromThreeDigits(Node incomingNode) {
        return Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(2)));
    }

    private Integer getDecadeFromThreeDigits(Node incomingNode) {
        return Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(1))) * 10;
    }

    private Integer getHundredFromThreeDigits(Node incomingNode) {
        return Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(0))) * 100;
    }

    private int getMonadaFromTwoDigits(Node incomingNode) {
        return Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(1)));
    }

    private Integer getDecadeFromTwoDigits(Node incomingNode) {
        return Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(0))) * 10;
    }

    private Integer getHundredOfParent(Node incomingNode) {
        return Integer.parseInt(String.valueOf(incomingNode.parent.getValue().charAt(0))) * 100;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getMiddleChild() {
        return middleChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public Node getParent() {
        return parent;
    }

    /**
     * There are several cases where an incoming node(with a maximum 3-digits grouping) may have a value that causes ambiguities.
     * Some examples:
     * 1. incoming node:24. This might mean => (i)24, (ii)204
     * 2. incoming node:304. => (i)304, (ii)3004
     * 3. incoming node:324. => (i)324, (ii)3204, (iii)300204, (iv)30024
     * */

    public boolean isSelfConflicting() {
        if (this.value.length() == 2) {
            return (!this.value.endsWith("0"));
        }
        else if (this.value.length() == 3 && !this.value.endsWith("0")) {
            return true;
        }
        else if (this.value.length() == 3 && this.value.endsWith("0") && !this.value.substring(0,2).endsWith("0")) {
            return true;
        }
        return false;

    }
    /**
     * There are several cases where an incoming node may be "conflicting" with the parent value
     * Some examples:
     * 1. parent:700, incoming node:24. This might mean => (i)70024, (ii)700204, (iii)724
     * 2. parent:700, incoming node:4. => (i)7004, (ii)704
     * 3. parent:30, incoming node:6. => (i)36, (ii)306
     * */
    public boolean isParentConflicting() {
        if(this.parent!=null) {
            if (this.parent.value.length() == 2 && this.parent.value.endsWith("0") && this.value.length()==1) { // Example 3
                return true;
            }
            else if (this.parent.value.length() == 3 && this.parent.value.endsWith("0") && this.value.length() <= 2 && this.parent.value.substring(0,2).endsWith("0")) { // Examples 1 or 2
                return true;
            }
        }
        return false; // if parent is null or 1-digit long => no conflict
    }

    public String getValue() {
        return  value;
    }

    /** We want to find the leaves in the existing structure.
     * Recursively, we go down the tree and find the leaves
     */
    public void locateLeaves(List<Node> leaves) {
        // find the leaves of the tree

            if (this.isLeaf()) {
                leaves.add(this);
            }
            else {
                if (this.getLeftChild() != null) {
                    this.getLeftChild().locateLeaves(leaves);
                }
                if (this.getRightChild() != null) {
                    this.getRightChild().locateLeaves(leaves);
                }
                if (this.getMiddleChild() != null) {
                    this.getMiddleChild().locateLeaves(leaves);
                }
            }
        }

    private boolean isLeaf() {
        return (this.getLeftChild() == null && this.getRightChild() == null && this.getMiddleChild() == null);
    }
}