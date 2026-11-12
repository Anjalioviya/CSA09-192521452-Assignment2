import java.nio.file.*;
import java.util.*;

public class RedesignBenchmark {
    static String original(String[] fields){String s="";for(String f:fields)s+=f+"\n";return s;}
    static String redesigned(String[] fields){StringBuilder b=new StringBuilder();for(String f:fields)b.append(f).append('\n');return b.toString();}
    static long measure(java.util.function.Supplier<String> op,int reps){
        long start=System.nanoTime();int checksum=0;for(int i=0;i<reps;i++)checksum+=op.get().length();
        if(checksum==0)throw new AssertionError();return (System.nanoTime()-start)/reps;
    }
    public static void main(String[] args)throws Exception{
        int[] counts={10,50,200,500};
        List<String> rows=new ArrayList<>();
        System.out.println("fields,repetitions,original_plus_ns,redesigned_builder_ns,outputs_equal");
        for(int n:counts){
            String[] f=new String[n];for(int i=0;i<n;i++)f[i]="field"+i+"=ticket-value";
            for(int i=0;i<5;i++){original(f);redesigned(f);}
            int reps=Math.max(100,10000/n);
            long o=measure(()->original(f),reps),r=measure(()->redesigned(f),reps);
            boolean equal=original(f).equals(redesigned(f));
            if(!equal)throw new AssertionError("Output mismatch");
            String row=n+","+reps+","+o+","+r+","+equal;rows.add(row);System.out.println(row);
        }
        Files.createDirectories(Path.of("data"));
        Files.writeString(Path.of("data/q7_redesign_benchmark.csv"),
          "fields,repetitions,original_plus_ns,redesigned_builder_ns,outputs_equal\n"+String.join("\n",rows)+"\n");
        System.out.println("Validation: PASS (identical generated ticket text).");
    }
}
