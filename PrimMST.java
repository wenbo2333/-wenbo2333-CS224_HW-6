import java.util.*;
import java.io.*;

class PrimMST {
    public static void main(String[] args) throws Exception{
        Graph<Character> g = new Graph<Character>();
        
        g.addEdge('a', 'b', 9);
        g.addEdge('a', 'f', 14);
        g.addEdge('a', 'g', 15);
        g.addEdge('b', 'c', 24);
        g.addEdge('f', 'c', 18);
        g.addEdge('f', 'g', 30);
        g.addEdge('g', 'e', 20);
        g.addEdge('c', 'e', 2);
        g.addEdge('c', 'd', 6);
        g.addEdge('c', 'h', 19);
        g.addEdge('d', 'e', 11);
        g.addEdge('d', 'h', 6);
        g.addEdge('e', 'f', 30);
        g.addEdge('e', 'h', 16);
        g.addEdge('h', 'g', 44);  
        
        System.out.println("Graph represented by adjacency list:\n" + g.toString());
        
        Scanner sc = new Scanner(System.in);   
        System.out.print("Enter the start node: ");
        char c = sc.next().charAt(0); 
       
        
        PrimMST(g, new Vertex<Character>(c));
    }
    
    public static void PrimMST(Graph<Character> gragh, Vertex<Character> startVertex) {
        ArrayList<Character> S = new ArrayList<Character>();
        ArrayList<TreeEdge> T = new ArrayList<TreeEdge>();
        gragh.changeKey(startVertex, 0);
        PriorityQueue<Vertex<Character>> q = BuildPQ(gragh);
               
        while (q.peek() != null) {
            Vertex<Character> v = q.poll();
            S.add(v.getId());
            if (v.getId() != startVertex.getId()) {
                T.add(new TreeEdge(v.getPredecessor(), v.getId()));
                // add the edge (pred(v), v) to T where pred is predecessor
            }
            LinkedList<Edge<Character>> edges = v.getEdges();
            for (int i = 0; i < edges.size(); i++) {
                Edge<Character> edge = edges.get(i);
                if (edge.weight < gragh.getVertices().get(gragh.getVertexById(edge.getTo())).getKeyVal()) {
                     gragh.changeKey(gragh.getVertices().get(gragh.getVertexById(edge.getTo())), edge.weight);
                     gragh.changePred(gragh.getVertices().get(gragh.getVertexById(edge.getTo())), v);
                }
            }
                      
            PriorityQueue<Vertex<Character>> copyQ = new PriorityQueue<Vertex<Character>>(q); 
            System.out.print("S = {");
            for (int i = 0; i < S.size(); i++) {
                 System.out.print(S.get(i) + " ");
            }
            System.out.print("}\n");
    
            System.out.print("T = {");
            for (int i = 0; i < T.size(); i++) {
                System.out.print(T.get(i) + " ");
            }
            System.out.print("}\n");
           
            System.out.print("Q = {");
            while (copyQ.peek() != null) {
                System.out.print(copyQ.poll().printShort());
            }
            
            System.out.print("}");
            System.out.println("\n");
        }
    }

    public static PriorityQueue<Vertex<Character>> BuildPQ(Graph<Character> g) {
        PriorityQueue<Vertex<Character>> q = new PriorityQueue<Vertex<Character>>();
        for (int i = 0; i < g.getVertices().size(); i++) {
            q.add(g.getVertices().get(i));
        }
        return q;
    }

}


class Graph<T> {
    LinkedList<Vertex<T>> vertices;


    public Graph() {
        this.vertices = new LinkedList<Vertex<T>>();
    }

    public void changeKey(Vertex<T> vertex, int newKey) {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).getId() == vertex.getId()){
                this.vertices.get(i).changeKey(newKey);;
            }
            
        }
    }

    public void changePred(Vertex<T> vertex, Vertex<T> newPred) {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).getId() == vertex.getId()){
                this.vertices.get(i).setPredecessor(newPred);;
            }
        }
    }

    public void addEdge(T from, T to, int weight) {
        Edge<T> fromE = new Edge<T>(from, weight);
        Edge<T> toE = new Edge<T>(to, weight);
        
         
        if (vertexExists(from)) {
            this.vertices.get(getVertexById(from)).addEdge(toE);
        } else {
            Vertex<T> tempVertex = new Vertex<T>(from);
            tempVertex.addEdge(toE);
            this.vertices.add(tempVertex);
        }
        if (vertexExists(to)) {
            this.vertices.get(getVertexById(to)).addEdge(fromE);
        } else {
            Vertex<T> tempVertex = new Vertex<T>(to);
            tempVertex.addEdge(fromE);
            this.vertices.add(tempVertex);
        }
    }

    public boolean vertexExists(T id) {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).getId() == id) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<Vertex<T>> getVertices() {
        return this.vertices;
    }

    public int getVertexById(T id) {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String outputString = "";
        for (int i = 0; i < this.vertices.size(); i++) {
            outputString += this.vertices.get(i).toString() + "\n";
        }
        return outputString;
    }

}

class Vertex<T> implements Comparable<Vertex<T>>{
    T id;
    int keyVal;
    LinkedList<Edge<T>> edges;
    T pred;

    public Vertex(T id) {
        this.id = id;
        this.edges = new LinkedList<Edge<T>>();
        this.keyVal = Integer.MAX_VALUE;
    }

    public boolean addEdge(Edge<T> edge) {
        if (this.edges.contains(edge)) {
            return false;
        } else {
            this.edges.add(edge);
            this.pred = edge.getTo();
            return true;
        }
    }

    public void setPredecessor(Vertex<T> pred) {
        this.pred = pred.id;
    }

    public T getPredecessor() {
        return this.pred;
    }

    public T getId() {
        return this.id;
    }

    public int getKeyVal() {
        return this.keyVal;
    }

    public LinkedList<Edge<T>> getEdges() { 
        return this.edges;
    }

    public void changeKey(int newKey) {
        this.keyVal = newKey;
    }

    @Override
    public String toString() {
        String outputString =  "";
        for (int i = 0; i < this.edges.size(); i++) {
            outputString += "("+this.id+"," + this.edges.get(i).toString()+") "; 
        }
        return outputString;
    }

    public String printShort() {
        if(this.getKeyVal() == Integer.MAX_VALUE) {
            return "(" + this.getId() + ", ?) ";
        }
        return "(" + this.getId() + ", " + this.getKeyVal() + ") ";
    }

    @Override
    public int compareTo(Vertex<T> otherVertex) {
        return this.getKeyVal() - otherVertex.getKeyVal();
    }

}

class TreeEdge {
    Character from;
    Character to;
    
    public TreeEdge(Character from, Character to) {
        this.from = from;
        this.to = to;
    }

    public Character getFrom() {
        return this.from;
    }

    public Character getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "(" + getFrom() + ", " + getTo() + ")";
    }

}

class Edge<T> {
    T to;
    int weight;

    /**
     * Constructor taking the vertex the edge goes to and the weight of the edge
     */
    public Edge(T to, int weight) {
        this.to = to;
        this.weight = weight;
    }

    public T getTo() {
        return this.to;
    }

    public int getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return "(" + getTo() + "," + getWeight() + ")";
    }

}