import com.omilia.test.NNIDemo;
import com.omilia.test.Node;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NNIDemoTest {

    @Test
    public void testCheckIfNumberIsValidGreek() {
        assertFalse(NNIDemo.checkIfNumberIsValidGreek("302558"));
        assertTrue(NNIDemo.checkIfNumberIsValidGreek("2106930664"));
        assertFalse(NNIDemo.checkIfNumberIsValidGreek("21069306604"));
        assertTrue(NNIDemo.checkIfNumberIsValidGreek("00306974092252"));
    }

    @Test
    public void testBuildTree() {
        Node mock = new Node("2");
        Node a = new Node("10");
        Node b = new Node("50");
        Node c = new Node("50");
        Node d = new Node("0");
        Node e = new Node("48");
        List<Node> listOfNodes = Arrays.asList(mock, a, b, c, d, e);

        Node finalRootStructure = NNIDemo.buildTree(listOfNodes);
        assertNull(finalRootStructure.getParent());
        assertTrue(finalRootStructure.getLeftChild().getValue() == "10");
        assertTrue(finalRootStructure.getLeftChild().getLeftChild().getValue() == "50");
        assertNull(finalRootStructure.getRightChild());

    }

    @Test
    public void testLocateLeaves() {
        Node mock = new Node("2");
        Node a = new Node("10");
        Node b = new Node("50");
        Node c = new Node("50");
        Node d = new Node("0");
        Node e = new Node("48");
        List<Node> listOfNodes = Arrays.asList(mock, a, b, c, d, e);

        Node finalRootStructure = NNIDemo.buildTree(listOfNodes);

    }

    @Test
    public void testIsSelfConflicting() {
        Node n = new Node("69");
        assertTrue(n.isSelfConflicting());
        Node n1 = new Node("769");
        assertTrue(n1.isSelfConflicting());
        Node n2 = new Node("709");
        assertTrue(n2.isSelfConflicting());
        Node n3 = new Node("9");
        assertFalse(n3.isSelfConflicting());
        Node n4 = new Node("700");
        assertFalse(n4.isSelfConflicting());
    }

    @Test
    public void testIsParentConflicting() {
        Node n = new Node("69");
        Node n1 = new Node("769");
        n1.setParent(n);
        assertFalse(n1.isParentConflicting());

        Node n2 = new Node("700");
        Node n3 = new Node("69");
        n3.setParent(n2);
        assertTrue(n3.isParentConflicting());

        Node n4 = new Node("30");
        Node n5 = new Node("9");
        n5.setParent(n4);
        assertTrue(n5.isParentConflicting());
    }

}