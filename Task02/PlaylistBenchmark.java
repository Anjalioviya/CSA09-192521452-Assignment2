import java.util.*;
import java.nio.file.*;

public class PlaylistBenchmark {
    static volatile long sink;
    static long measure(Runnable r, int reps) {
        long s=System.nanoTime(); for(int i=0;i<reps;i++) r.run(); return (System.nanoTime()-s)/reps;
    }
    public static void main(String[] args) throws Exception {
        int[] sizes={1000,10000,50000};
        List<String> rows=new ArrayList<>();
        System.out.println("size,array_append_ns,linked_append_ns,array_get_ns,linked_get_ns,array_front_insert_ns,linked_front_insert_ns,deque_ends_ns,array_iter_ns,linked_iter_ns");
        for(int n:sizes) {
            ArrayList<Integer> a=new ArrayList<>(n+1); LinkedList<Integer> l=new LinkedList<>();
            for(int i=0;i<n;i++){a.add(i);l.add(i);}
            int reps=1000;
            long aa=measure(()->{a.add(-1);a.remove(a.size()-1);},reps);
            long la=measure(()->{l.add(-1);l.remove(l.size()-1);},reps);
            long ag=measure(()->sink+=a.get(n/2),reps);
            long lg=measure(()->sink+=l.get(n/2),reps);
            long ai=measure(()->{a.add(0,-1);a.remove(0);},reps);
            long li=measure(()->{l.add(0,-1);l.remove(0);},reps);
            ArrayDeque<Integer> d=new ArrayDeque<>(a);
            long de=measure(()->{d.addFirst(-1);d.addLast(-2);d.removeFirst();d.removeLast();},reps);
            long at=measure(()->{long x=0;for(int v:a)x+=v;sink=x;},100);
            long lt=measure(()->{long x=0;for(int v:l)x+=v;sink=x;},100);
            String row=n+","+aa+","+la+","+ag+","+lg+","+ai+","+li+","+de+","+at+","+lt;
            rows.add(row);System.out.println(row);
        }
        Files.createDirectories(Path.of("data"));
        Files.writeString(Path.of("data/q2_playlist_benchmark.csv"),
          "size,array_append_ns,linked_append_ns,array_get_ns,linked_get_ns,array_front_insert_ns,linked_front_insert_ns,deque_ends_ns,array_iter_ns,linked_iter_ns\n"+String.join("\n",rows)+"\n");
        System.out.println("Interpretation: timings are JVM/machine dependent; compare trends, not isolated nanoseconds.");
    }
}
