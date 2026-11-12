import java.util.StringJoiner;
import java.util.StringJoiner;
import java.util.Locale;
import java.nio.file.*;
import java.util.*;

public class TicketBenchmark {
    static String[] fields(int n) {
        String[] a = new String[n];
        for (int i=0;i<n;i++) a[i] = "FIELD" + i + "=Passenger" + (i%97);
        return a;
    }
    static String plus(String[] f) {
        String s = "";
        for (String x : f) s += x + "\n";
        return s;
    }
    static String builder(String[] f) {
        StringBuilder b = new StringBuilder();
        for (String x : f) b.append(x).append('\n');
        return b.toString();
    }
    static String buffer(String[] f) {
        StringBuffer b = new StringBuffer();
        for (String x : f) b.append(x).append('\n');
        return b.toString();
    }
    static long time(java.util.function.Supplier<String> op, int reps) {
        long start=System.nanoTime();
        int checksum=0;
        for(int i=0;i<reps;i++) checksum += op.get().length();
        long elapsed=System.nanoTime()-start;
        if(checksum==0) throw new AssertionError();
        return elapsed/reps;
    }
    public static void main(String[] args) throws Exception {
        int[][] workloads={{6,1},{50,10000},{200,1000}};
        System.out.println("fields,tickets,plus_ns_per_ticket,builder_ns_per_ticket,buffer_ns_per_ticket");
        List<String> rows=new ArrayList<>();
        for(int[] w:workloads) {
            String[] f=fields(w[0]); int reps=w[1];
            // Warm-up each strategy
            for(int i=0;i<3;i++){plus(f);builder(f);buffer(f);}
            long p=time(()->plus(f),reps), b=time(()->builder(f),reps), sb=time(()->buffer(f),reps);
            String row=w[0]+","+reps+","+p+","+b+","+sb;
            rows.add(row); System.out.println(row);
        }
        Files.createDirectories(Path.of("data"));
        Files.writeString(Path.of("data/q1_ticket_benchmark.csv"),
            "fields,tickets,plus_ns_per_ticket,builder_ns_per_ticket,buffer_ns_per_ticket\n"+String.join("\n",rows)+"\n");
        String[] check=fields(50);
        if(!plus(check).equals(builder(check)) || !builder(check).equals(buffer(check)))
            throw new AssertionError("Ticket strategies differ");
        System.out.println("Output equivalence: PASS");
        System.out.println("Estimated intermediate strings: repeated + creates an intermediate result per append; builders use a mutable buffer and one final String.");
    }
}
