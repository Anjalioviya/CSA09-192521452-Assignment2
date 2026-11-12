import java.util.*;

public class LeaderboardDemo {
    static class Team {
        final String name; final int points; final int goalDiff;
        Team(String name,int points,int goalDiff){this.name=name;this.points=points;this.goalDiff=goalDiff;}
        @Override public String toString(){return name+"("+points+","+goalDiff+")";}
    }
    static class Leaderboard<T> {
        private final List<T> items=new ArrayList<>();
        private final Comparator<? super T> order;
        Leaderboard(Comparator<? super T> order){this.order=Objects.requireNonNull(order);}
        void add(T item){items.add(Objects.requireNonNull(item));}
        // Return a sorted copy; insertion order in the backing list is unchanged.
        List<T> ranking(){
            List<T> copy=new ArrayList<>(items);
            copy.sort(order);
            return copy;
        }
        List<T> from(List<? extends T> source){
            List<T> copy=new ArrayList<>(source);copy.sort(order);return copy;
        }
    }
    public static void main(String[] args){
        Comparator<Team> byRank=Comparator.comparingInt((Team t)->t.points).reversed()
            .thenComparing(Comparator.comparingInt((Team t)->t.goalDiff).reversed())
            .thenComparing(t->t.name,String.CASE_INSENSITIVE_ORDER);
        Leaderboard<Team> board=new Leaderboard<>(byRank);
        board.add(new Team("Panthers",9,4));board.add(new Team("Eagles",9,6));
        board.add(new Team("Cobras",6,1));board.add(new Team("Bisons",9,4));
        System.out.println(board.ranking());
        System.out.println("Bisons above Panthers: alphabetical tie-break (B before P).");
        List<Team> tieTest=List.of(new Team("Zeta",5,2),new Team("Alpha",5,2));
        System.out.println("Tie test: "+board.from(tieTest));
    }
}
