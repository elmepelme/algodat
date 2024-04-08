import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

@SuppressWarnings("unchecked")

public class lab4 {

  public static class Point {
    private int x;
    private int y;
    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
    public int getX() {
      return x;
    }
    public int getY() {
      return y;
    }
    @Override
    public String toString() {
      String s = "(" + x + ", " + y + ")";
      return s;
    }
  }

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    ArrayList<Point> Px = readPoints(scan);
    ArrayList<Point> Py = new ArrayList(Px);

    Collections.sort(Px, new Comparator<Point>() {
      public int compare(Point o1, Point o2) {
      return Integer.compare(o1.getX(), o2.getX());
      }
    });

    Collections.sort(Py, new Comparator<Point>() {
      public int compare(Point o1, Point o2) {
      return Integer.compare(o1.getY(), o2.getY());
      }
    });

    double d = closest(Px, Py, Px.size());
    double scale = Math.pow(10, 6);
    d = Math.round(d*scale) / scale;
    System.out.println(d);

  }
  public static double distance(Point p, Point q) {
      return Math.hypot(p.getX()-q.getX(), p.getY()-q.getY());
    }

  public static ArrayList<Point> readPoints(Scanner scan) {
    int nbrPoints = scan.nextInt();
    scan.nextLine();
    ArrayList<Point> points = new ArrayList<>();
    for (int i = 0; i < nbrPoints ; i++) {
      String line = scan.nextLine();
      String[] lineInts = line.split(" ", 0);
      Point p = new Point(Integer.parseInt(lineInts[0]),
      Integer.parseInt(lineInts[1]));
      points.add(p);
    }
    return points;
  }

  public static double naive(ArrayList<Point> points) {
    double dist = Double.MAX_VALUE;
      for (Point p : points) {
        for (Point q : points) {
          if (p != q && distance(p,q) < dist) {
            dist = distance(p,q);
          }
        }
      }
    return dist;
  }

  public static ArrayList<Point> pointsClose(ArrayList<Point> points,
  Point midPoint, double dist) {
    ArrayList<Point> pointsD = new ArrayList<>();
    for (Point p : points) {
      if (Math.abs(p.getX() - midPoint.getX()) < dist) {
        pointsD.add(p);
      }
    }
    return pointsD;
  }

  public static double checkLine(ArrayList<Point> points, double dist) {
    int C = 5;
    double minDist = dist;
    for (int i = 0; i < points.size(); i++) {
      for (int j = i + 1; j < Math.min(i + C, points.size()); j++) {
        if (points.get(i) != points.get(j) &&
        distance(points.get(i), points.get(j)) < dist) {
          minDist = distance(points.get(i), points.get(j));
        }
      }
    }
    return minDist;
  }

  public static double closest(ArrayList<Point> Px, ArrayList<Point> Py,
  int n) {
    double d;

    if (n <= 7) {
      d = naive(Px);
      return d;
    }

    int mid = n/2;
    Point midPoint = Px.get(mid);

    ArrayList<Point> Lx = new ArrayList(Px.subList(0, mid));
    ArrayList<Point> Rx = new ArrayList(Px.subList(mid, Px.size()));

    ArrayList<Point> Ly = new ArrayList<>();
    ArrayList<Point> Ry = new ArrayList<>();

    for (Point p : Py) {
      if (p.getX() <= midPoint.getX()) {
        Ly.add(p);
      } else {
        Ry.add(p);
      }
    }

    double d1 = closest(Lx, Ly, Lx.size());
    double d2 = closest(Rx, Ry, Rx.size());

    d = Math.min(d1, d2);

    ArrayList<Point> pointsD = pointsClose(Py, midPoint, d);
    d = checkLine(pointsD, d);
    return d;
  }
 }
