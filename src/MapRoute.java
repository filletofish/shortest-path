import java.util.*;

public class MapRoute {

    static class Node implements Comparable<Node> {
        private  int data, dist;
        private boolean used;
        private ArrayList<Node> arcs;

        public Node(int x, int y, int data) {
            this.data = data;
            this.used = false;
            this.dist = -1; // stands for eternity
            this.arcs = new ArrayList<Node>();
        }

        @Override
        public String toString() {
            String links = "";
            for (Node v : arcs) links +=  " " + v.data;

            return "{ " + data + "- " + links  +
                    " } ";
        }

        @Override
        public int compareTo(Node o) {
            if (this.dist == -1 && o.dist == -1) return 0;
            if (this.dist == -1) return 1;
            if (o.dist == -1) return -1;
            if (this.dist >= o.dist) return 1;
            else return -1;
        }
    }

    static class OrGraph implements Iterable<Node> {
        private ArrayList<Node> points;
        private int size;
        public Node get(int a) { return this.points.get(a);}

        public void addArc(int a, int b) {
            points.get(a).arcs.add(points.get(b));
            points.get(b).arcs.add(points.get(a));
        }

        public OrGraph() {
            points = new ArrayList<Node>();
        }

        public void initArcs(int n) {
            if (n != 1) {
                for (int i = 0; i < n - 1; i++) {
                    for (int j = 0; j < n - 1; j++) {
                        this.addArc(i*n + j, i*n + j + 1);
                        this.addArc(i*n + j, (i+1)*n + j);
                    }
                    this.addArc(i*n + n - 1, (i+1)*n + n - 1);
                }
                for (int j = 0; j < n - 1; j++) {
                    this.addArc((n-1)* n + j, (n-1)* n + j + 1);
                }
                this.size = this.points.size();
            }
        }

        public Iterator<Node> iterator() {
            return new MyGraphIterator();
        }

        private class MyGraphIterator implements Iterator <Node> {
            private int pos;
            public MyGraphIterator() {
                pos = 0;
            }
            public boolean hasNext () {
                return pos < size;
            }
            public Node next() {
                return points.get(pos++);
            }
        }
    }

    static boolean Relax(Node u, Node v, int w) {
        if (u.dist == -1) return false;
        if (v.dist == -1 || (u.dist + w < v.dist)) {
            v.dist = u.dist + w;
            return true;
        }
        return false;
    }


    static void Dijkstra (OrGraph G, Node s) {
        s.dist = 0;
        NavigableSet<Node> q = new TreeSet<Node>();
        for (Node v : G) q.add(v);

        while (! q.isEmpty()){
            Node v = q.pollFirst();
            v.used = true;
            for (Node u : v.arcs)
                if (! u.used && Relax(v, u, u.data)) {
                    q.remove(u);
                    q.add(u);
                }
        }
    }

    public static void main(String[] args) {
        // write your code here
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        OrGraph G = new OrGraph();

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                G.points.add(new Node(i,j,in.nextInt()));

        G.initArcs(n);
        Dijkstra(G, G.get(0));
        System.out.println(G.get(n*n - 1).dist + G.get(0).data);
    }
}

