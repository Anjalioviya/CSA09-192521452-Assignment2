import java.util.*;
import java.nio.file.*;

public class PharmacyBenchmark {
    record Medicine(String code,String name,int stock) {}
    static volatile int sink;
    static long lookupTime(Map<String,Medicine> map,List<String> keys,int reps){
        long s=System.nanoTime();
        for(int r=0;r<reps;r++) for(String k:keys) sink += map.get(k).stock();
        return (System.nanoTime()-s)/((long)reps*keys.size());
    }
    static long reportTime(Map<String,Medicine> map,int reps){
        long s=System.nanoTime();
        for(int r=0;r<reps;r++){int count=0;for(var e:map.entrySet())count+=e.getKey().length();sink=count;}
        return (System.nanoTime()-s)/reps;
    }
    public static void main(String[] args)throws Exception{
        int[] sizes={1000,10000,50000};
        List<String> rows=new ArrayList<>();
        System.out.println("medicines,hash_get_ns,tree_get_ns,linked_get_ns,hash_iter_ns,tree_iter_ns,linked_iter_ns");
        for(int n:sizes){
            Map<String,Medicine> h=new HashMap<>(),t=new TreeMap<>(),l=new LinkedHashMap<>();
            for(int i=0;i<n;i++){String k=String.format(Locale.ROOT,"MED%06d",i);Medicine m=new Medicine(k,"Medicine"+i,i%500);h.put(k,m);t.put(k,m);l.put(k,m);}
            List<String> keys=new ArrayList<>();for(int i=0;i<100;i++)keys.add(String.format(Locale.ROOT,"MED%06d",(i*7919)%n));
            long hg=lookupTime(h,keys,10),tg=lookupTime(t,keys,10),lg=lookupTime(l,keys,10);
            long hi=reportTime(h,5),ti=reportTime(t,5),li=reportTime(l,5);
            String row=n+","+hg+","+tg+","+lg+","+hi+","+ti+","+li;rows.add(row);System.out.println(row);
            // Requirement checks: TreeMap key order, LinkedHashMap insertion order.
            if(!t.keySet().iterator().next().equals("MED000000"))throw new AssertionError("TreeMap order");
            if(!l.keySet().iterator().next().equals("MED000000"))throw new AssertionError("LinkedHashMap order");
        }
        Files.createDirectories(Path.of("data"));
        Files.writeString(Path.of("data/q4_map_benchmark.csv"),
          "medicines,hash_get_ns,tree_get_ns,linked_get_ns,hash_iter_ns,tree_iter_ns,linked_iter_ns\n"+String.join("\n",rows)+"\n");
        System.out.println("Ordering checks: PASS");
    }
}
