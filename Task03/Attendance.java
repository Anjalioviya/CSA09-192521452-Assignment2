import java.util.*;

public class Attendance {
    static class Student {
        private final String regNo;
        private final String name;
        Student(String regNo,String name){this.regNo=Objects.requireNonNull(regNo);this.name=name;}
        @Override public boolean equals(Object o) {
            if(this==o)return true;
            if(!(o instanceof Student s))return false;
            return regNo.equals(s.regNo);
        }
        // TODO 1 completed: equal students (same regNo) must have equal hash codes.
        @Override public int hashCode(){return Objects.hash(regNo);}
        @Override public String toString(){return regNo+" "+name;}
    }
    public static void main(String[] args) {
        Set<Student> scans=new HashSet<>();
        scans.add(new Student("192311001","Arun"));
        scans.add(new Student("192311002","Divya"));
        scans.add(new Student("192311001","Arun"));
        System.out.println("Unique students: "+scans.size());

        // Demonstrate a faulty implementation: equals uses regNo, hashCode uses identity.
        Set<Student> faulty=new HashSet<>();
        Student x=new Student("192311010","Maya"), y=new Student("192311010","Maya");
        faulty.add(x); faulty.add(y);
        System.out.println("Corrected duplicate test size: "+faulty.size());

        Set<Student> additional=new HashSet<>();
        additional.add(new Student("192311020","Nila"));
        additional.add(new Student("192311021","Nila"));
        System.out.println("Distinct registration numbers: "+additional.size());
        System.out.println("Contract checks: "+(x.equals(y) && x.hashCode()==y.hashCode() ? "PASS":"FAIL"));
    }
}
