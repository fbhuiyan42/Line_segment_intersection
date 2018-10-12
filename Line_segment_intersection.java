
package line_segment_intersection;

import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;

class Line
{
    Point p1;
    Point p2;
    boolean st;
    int segno;
}


class Point implements Comparable<Point>
{
    double x;
    double y;
    Line line;
    Line line1;
    int start;
    
    Point()
    {

    }
    
    Point(double x,double y,Line line,Line line1,int st) 
    {
        this.x=x;
        this.y=y;
        this.line=line;
        this.line1=line1;
        this.start=st;
    }
    
    // Given three colinear points p, q, r, the function checks if
    // point q lies on line segment 'pr'
    static boolean onSegment(Point p, Point q, Point r)
    {
        if (q.x <= max(p.x, r.x) && q.x >= min(p.x, r.x) && q.y <= max(p.y, r.y) && q.y >= min(p.y, r.y))
            return true;
        return false;
    }
 
    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    static int orientation(Point p, Point q, Point r)
    {
    
        double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
 
        if (val == 0) return 0;  // colinear
 
        return (val > 0)? 1: 2; // clock or counterclock wise
    }
    
    // The main function that returns true if line segment 'p1q1' and 'p2q2' intersect.
    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2)
    {
        // Find the four orientations needed for general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
 
        // General case
        if (o1 != o2 && o3 != o4) return true;
 
        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        // p1, q1 and p2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;
 
        return false; // Doesn't fall in any of the above cases
    }
    
    public int compareTo(Point P) 
    {
        if(this.x < P.x) return -1;
        else if (this.x > P.x) return +1;
        else if (this.y < P.y) return -1;
        else if (this.y > P.y) return +1;
        else if(this.start > P.start) return -1;
        else return  0; 
    }
  
    static void Generate_Event_Points(Line line[],int N,PriorityQueue<Point> EQ)
    {
        for (int i = 0; i < N; i++) 
        {
            Point P1 = new Point(line[i].p1.x,line[i].p1.y,line[i],null,1);
            Point P2 = new Point(line[i].p2.x,line[i].p2.y,line[i],null,0);
            EQ.add(P1);
            EQ.add(P2);
        }
    }
    
    static Point intersectPoint(Line L1,Line L2)
    {
        double x1=L1.p1.x;
        double y1=L1.p1.y;
        double x2=L1.p2.x;
        double y2=L1.p2.y;
        double x3=L2.p1.x;
        double y3=L2.p1.y;
        double x4=L2.p2.x;
        double y4=L2.p2.y;
        double a1=y1-y2;
        double b1=x2-x1;
        double a2=y3-y4;
        double b2=x4-x3;
        double c1= x1*(y2-y1) + y1*(x1-x2);
        double c2= x3*(y4-y3) + y3*(x3-x4);
        double d=a1*b2 - a2*b1;
        double xi=(b1*c2 - b2*c1)/d;
        double yi=(a2*c1 - a1*c2)/d;
        return new Point(xi,yi,L1,L2,2);

    }
    
    static void sweep(int N,PriorityQueue<Point> EQ,int list[])
    {
        Vector<Line> active = new Vector<>();
        Vector<Point> output=new Vector<>();
        int n=0;
        while (!EQ.isEmpty()) 
        {
            Point E = EQ.remove();
            if (E.start==1) 
            {
                int index=binarySearch(active,0,n,E.y );
                if (index < 0) 
                {
                    index = - index - 1;
                }
                n++;
                active.add(index,E.line);
                for(int i=0;i<active.size();i++)
                {
                    if(E.line.p1!=active.elementAt(i).p1) 
                    {
                        if ( doIntersect(E.line.p1,E.line.p2, active.elementAt(i).p1,active.elementAt(i).p2))
                         EQ.add(intersectPoint(E.line, active.elementAt(i)));
                    }
                }
                /*if(index>0)
                {
                    if ( doIntersect(E.line.p1,E.line.p2, active.elementAt(index-1).p1,active.elementAt(index-1).p2))
                         EQ.add(intersectPoint(E.line, active.elementAt(index-1)));
                }
                if(index+1<active.size())
                {
                    if ( doIntersect(E.line.p1,E.line.p2, active.elementAt(index+1).p1,active.elementAt(index+1).p2))
                        EQ.add(intersectPoint(E.line,active.elementAt(index+1)));
                }*/
            }

            else if (E.start==0) 
            {
                int index=binarySearch(active,0,n,E.y );
                
                if (index < 0) 
                {
                    index = - index - 1;
                }
                for(int i=0;i<active.size();i++)
                {
                    if(E.line.p1!=active.elementAt(i).p1) 
                    {
                         if ( doIntersect(E.line.p1,E.line.p2, active.elementAt(i).p1,active.elementAt(i).p2))
                         {
                             EQ.add(intersectPoint(E.line, active.elementAt(i)));
                         }
                    }
                }
                /*if(index>0 && index+1<active.size())
                 {
                    if ( doIntersect( active.elementAt(index-1).p1,active.elementAt(index-1).p2, active.elementAt(index+1).p1, active.elementAt(index+1).p2))
                    {
                        Point P=intersectPoint(active.elementAt(index-1), active.elementAt(index+1));
                        if(!EQ.contains(P)) EQ.add(P);
                    }
                 }*/
                
                if(index<active.size()) 
                    active.remove(index);
                n--;
            }
            else 
            {
                int flag=1;
                for(int g=0;g<output.size();g++)
                {
                    if(output.elementAt(g).x==E.x && output.elementAt(g).y==E.y ) flag=0;
                }
                if(flag==1) output.add(E);
                int index1=active.indexOf(E.line1);
                if (index1 < 0) 
                {
                    index1 = - index1 - 1;
                }
                int index2=active.indexOf(E.line);
                if (index2 < 0) 
                {
                    index2 = - index2 - 1;
                }
                if(index1+1<active.size())
                {
                    if ( doIntersect( active.elementAt(index1).p1,active.elementAt(index1).p2, active.elementAt(index1+1).p1,active.elementAt(index1+1).p2))
                    {
                        Point P=intersectPoint(active.elementAt(index1), active.elementAt(index1+1));
                        if(!EQ.contains(P)) EQ.add(P);
                    }
                }
                if(index2>0)
                {
                    if ( doIntersect( active.elementAt(index2).p1,active.elementAt(index2).p2, active.elementAt(index2-1).p1,active.elementAt(index2-1).p2))
                    {
                        Point P=intersectPoint(active.elementAt(index2), active.elementAt(index2-1));
                        if(!EQ.contains(P)) EQ.add(P);
                    }
                }
        }       
        EQ.remove(E); 
     }  
     System.out.println("No. of Intersections : "+output.size());
     System.out.println("Intersecting Points : ");
     for(int i=0;i<output.size();i++) System.out.println("( "+output.elementAt(i).x+", "+output.elementAt(i).y+" )");
   }
    
   static int binarySearch(Vector<Line> active,int low,int nElems,double key) 
   {
        if (nElems == 0) return 0;
        int high = nElems - 1;
        int mid = 0;
        while (true)
        {
            mid = (high + low) / 2;
            if(mid<active.size())
            {
                if (active.elementAt(mid).p1.y == key) return mid;
                else if (active.elementAt(mid).p1.y < key)
                {
                    low = mid + 1; // its in the upper
                    if (low > high) return mid + 1;
                }
                else
                {
                    high = mid - 1; // its in the lower
                    if (low > high)return mid;
                }
            }
        }
   }
}


public class Line_segment_intersection {

    public static Scanner in;
    public static void main(String[] args) throws IOException 
    {
        in = new Scanner(new File("input.txt"));
        int T=in.nextInt();
        for(int c=0;c<T;c++)
        {
            int N=in.nextInt();
            Line line[]=new Line[N];
            int seg=0;
            for(int i=0;i<N;i++)
            {
                line[i]=new Line();
                line[i].p1=new Point();
                line[i].p2=new Point();
                line[i].p1.x=in.nextInt();
                line[i].p1.y=in.nextInt();
                line[i].p2.x=in.nextInt();
                line[i].p2.y=in.nextInt();
                line[i].segno=i;
            }
            PriorityQueue<Point> EQ = new PriorityQueue<Point>();
            Point.Generate_Event_Points(line,N,EQ);
            int list[]=new int[N];
            Point.sweep(N,EQ,list);
            System.out.println();
        }
    }
    
}
