package assignment1;

public abstract class Node {
    protected Node left;
    protected Node right;
    protected boolean changeable;
    protected String name;

    public Node(){ }
    public void setLeft(Node leftNode){ left = leftNode; }
    public boolean hasLeft(){ return !(left == null); }

    public void setRight(Node rightNode){ right = rightNode; }
    public boolean hasRight(){ return !(right == null); }
    public Node getLeft(){ return left; }
    public Node getRight(){ return right; }
    public String getName(){ return name;}
    public boolean isChangeable(){return changeable;}
    abstract public String toString();


}
