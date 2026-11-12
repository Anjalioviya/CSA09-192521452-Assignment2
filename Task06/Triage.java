import java.util.*;

public class Triage {
    static class Patient {
        final String name;final int severity;final long arrival;
        Patient(String name,int severity,long arrival){if(severity<1||severity>5)throw new IllegalArgumentException("Severity must be 1..5");this.name=name;this.severity=severity;this.arrival=arrival;}
        @Override public String toString(){return name+"(S"+severity+")";}
    }
    private long counter=0;
    // Lower severity number is more urgent; equal severity is FIFO by arrival.
    private final PriorityQueue<Patient> queue=new PriorityQueue<>(
        Comparator.comparingInt((Patient p)->p.severity).thenComparingLong(p->p.arrival));
    void admit(String name,int severity){queue.add(new Patient(name,severity,counter++));}
    Patient next(){return queue.poll();}
    List<Patient> waitingInTreatmentOrder(){List<Patient> copy=new ArrayList<>(queue);copy.sort(queue.comparator());return copy;}
    public static void main(String[] args){
        Triage er=new Triage();
        er.admit("Ravi",3);er.admit("Meena",1);er.admit("John",3);
        er.admit("Asha",5);er.admit("Kumar",1);er.admit("Lata",2);
        StringBuilder order=new StringBuilder();Patient p;
        while((p=er.next())!=null)order.append(p).append(' ');
        System.out.println(order.toString().trim());
        System.out.println("Expected: Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)");
    }
}
