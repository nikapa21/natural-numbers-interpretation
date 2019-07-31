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
     *
     * */

    public void addChild(Node incomingNode) {

        this.leftChild = incomingNode;
        incomingNode.parent = this;

        if(incomingNode.isParentConflicting()) {

            Node athroisma = new Node(String.valueOf(Integer.parseInt(this.getValue()) + Integer.parseInt(incomingNode.getValue())));
            this.parent.rightChild = athroisma;
            athroisma.parent = this.parent;
        }

        if (incomingNode.isSelfConflicting()) {
            if(incomingNode.getValue().length() == 3) {
                Integer ekatontada = (Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(0))) * 100);
                Integer dekada = (Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(1))) * 10);
                Integer monada = Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(2)));

                Node a = new Node(String.valueOf(ekatontada));
                Node athroismaEkatontadasDekadas = new Node(String.valueOf(ekatontada+dekada));
                Node b = new Node(String.valueOf(dekada));
                Node athroismaDekadasMonadas = new Node(String.valueOf(dekada + monada));
                Node c = new Node(String.valueOf(monada));
                Node d = new Node(String.valueOf(monada));

                this.rightChild = a;
                a.parent = this;

                a.leftChild = b;
                b.parent = a;

                a.rightChild = athroismaDekadasMonadas;
                athroismaDekadasMonadas.parent = a;

                b.leftChild = c;
                c.parent = b;

                this.middleChild = athroismaEkatontadasDekadas;
                athroismaEkatontadasDekadas.parent = this;

                athroismaEkatontadasDekadas.leftChild = d;
                d.parent = athroismaEkatontadasDekadas;

            }
            else {
                Integer dekada = (Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(0))) * 10);
                Integer deuteropsifio = Integer.parseInt(String.valueOf(incomingNode.getValue().charAt(1)));

                Node a = new Node(String.valueOf(dekada));
                Node b = new Node(String.valueOf(deuteropsifio));

                this.rightChild = a;
                a.parent = this;
                a.leftChild = b;
                b.parent = a;
            }
        }
    }

    public void insertRightChild (Node node) {
        rightChild = node;
    }

    public void insertParent (Node node) {
        parent = node;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getMiddleChild() {
        return middleChild;
    }

    public void setMiddleChild(Node middleChild) {
        this.middleChild = middleChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isSelfConflicting() {
        return (this.value.length() == 2 && !this.value.endsWith("0")
                || this.value.length() == 3 && !this.value.endsWith("0"));
    }

    public boolean isParentConflicting() {
        if(this.parent!=null) {
            return (this.parent.value.length() == 2 && this.parent.value.endsWith("0") && this.value.length()==1
                    || this.parent.value.length() == 3 && this.parent.value.endsWith("0") && this.value.length()<=2);
//            parent is 2 digits long and ends with 0 (e.g 30)
//            if (this.parent.value.length() == 3 && this.parent.value.endsWith("0")) return true; // parent is 3 digits long and ends with 0 (e.g 700)
//            if parent is 1 digit long. no conflict
        } else {
            return false;
        }
    }

    public boolean hasParent(List<Node> listOfNodes) {
        if (this.parent == null) {
            return false;
        }
        else {
            return true;
        }
//        return this.parent != null;
    }

    public void setValue(String number) {
        this.value = value;
    }

    public String getValue() {
        return  value;
    }

    public void locateLeaves(List<Node> leaves) {

        // find the leaves of the tree and insert the node under each leaf accordingly
//        List<com.omilia.test.Node> leaves = findLeaves(this);

        boolean stillTraversing = true;
        
//        while(stillTraversing) {
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
//    }

    private boolean isLeaf() {
        return (this.getLeftChild() == null && this.getRightChild() == null && this.getMiddleChild() == null);
    }

//    private List<com.omilia.test.Node> findLeaves(com.omilia.test.Node node) {
//        if(node.getLeftChild() != null || node.getRightChild() != null) {
//
//        }
//    }
}
