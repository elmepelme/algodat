import java.util.*;
//
public class lab6 {
  static ArrayList<Edge> edges; // så vi kan plocka ut vilka som ska tas bort
  static ArrayList<ArrayList<Edge>> graph;
  static int N, M, C, P;
  static int[] edgeToRemove; //i ordning index för vilken edge som ska bort

  public static class Edge {
    public int from, to, resCap;
    public final int CAPACITY;
    public Edge residual;
    public boolean activated;

    public Edge(int from, int to, int c) {
      this.from = from;
      this.to = to;
      this.CAPACITY = c;
      this.resCap = c;
      this.activated = true;
    }
    public int getResCap() {
      if(activated) {
        return resCap;
      }
      return 0;
    }
  }

  public static void main(String[] args) {
    readInput();
    int nbrAdded = 0;
    int ed;

// stänger av alla kanter i listan
    for (int i = 0; i < edgeToRemove.length; i++) {
      ed = edgeToRemove[i];
      edges.get(ed).activated = false;
      edges.get(ed).residual.activated = false;
    }

    int f = fordFulkerson(graph, 0, N-1);
    Edge e;

// lägger tillbaka kanterna en och en
    for (int i = edgeToRemove.length-1; i >= 0 && f < C; i--) {
      e = edges.get(edgeToRemove[i]);
      e.activated = true;
      e.residual.activated = true;
      f += fordFulkerson(graph, 0, N-1);
      nbrAdded += 1;
    }

    System.out.println(edgeToRemove.length - nbrAdded + " " + f);
    }

  public static int augumentPath(HashMap<Integer, Edge> pred, int t) {
    int pathFlow = Integer.MAX_VALUE;
    int n = t;
    Edge e;

    while(!Objects.isNull(pred.get(n))) {
      e = pred.get(n);
      pathFlow = Math.min(pathFlow, e.getResCap());
      n = e.from;
    }

    n = t;
    while(!Objects.isNull(pred.get(n))) {
      e = pred.get(n);
      e.resCap -= pathFlow;
      e.residual.resCap += pathFlow;
      n = e.from;
    }

    return pathFlow;
  }

  public static int fordFulkerson(ArrayList<ArrayList<Edge>> graph, int s,
  int t) {
// (0,1), (1,2) ,(2,3)
    HashMap<Integer, Edge> pred = BFS(graph, s, t);
    int maxFlow = 0;
    while(!pred.isEmpty()) {
      maxFlow += augumentPath(pred, t);
      pred = BFS(graph, s, t);
    }
    return maxFlow;
  }

  public static HashMap<Integer, Edge> BFS(ArrayList<ArrayList<Edge>> graph,
  int s, int t) {
    int V = graph.size();
    boolean[] visited = new boolean[V];
    LinkedList<Integer> queue = new LinkedList<Integer>();
    queue.add(s);
    visited[s] = true;
    HashMap<Integer, Edge> pred = new HashMap<Integer, Edge>();
    pred.put(s,null);
    int m, n;

    while(queue.size() != 0) {
      m = queue.poll();
      for (Edge e : graph.get(m)) {
        n = e.to;
        if (!visited[n] && e.getResCap() > 0) {
          if (n == t) {
            pred.put(n,e);
            return pred;
          }
          queue.add(n);
          pred.put(n,e);
          visited[n] = true;
        }
      }
    }
    // tom lista om ingen path hittades
    return new HashMap<Integer, Edge>();
  }

  public static void readInput() {
  Scanner scan = new Scanner(System.in);
  String[] firstLine = scan.nextLine().split(" ");
  N = Integer.parseInt(firstLine[0]);
  M = Integer.parseInt(firstLine[1]);
  C = Integer.parseInt(firstLine[2]);
  P = Integer.parseInt(firstLine[3]);
  int from, to, c;
  edges = new ArrayList<>();
  graph = new ArrayList<>();
  for (int i = 0; i < N; i++) {
    graph.add(new ArrayList<Edge>());
  }
  for (int i = 0; i < M; i++) {
    String[] line = scan.nextLine().split(" ");
    from = Integer.parseInt(line[0]);
    to = Integer.parseInt(line[1]);
    c = Integer.parseInt(line[2]);
    Edge e1 = new Edge(from, to, c);
    Edge e2 = new Edge(to, from, c);
    e1.residual = e2;
    e2.residual = e1;
    edges.add(e1);
    graph.get(from).add(e1);
    graph.get(to).add(e2);
  }

  edgeToRemove = new int[P];
  for (int i = 0; i < P; i++) {
    edgeToRemove[i] = Integer.parseInt(scan.nextLine());
    }
  }

}
