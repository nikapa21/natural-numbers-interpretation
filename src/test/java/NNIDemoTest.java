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
    public void testInsertLeftChild() {

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


}