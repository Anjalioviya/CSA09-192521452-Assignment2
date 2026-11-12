# CSA09 Programming in Java – Assignment 2 (CO2)

**Student:** Anjali Oviya  
**Register number:** 192521452  
**Java version:** Java 21  
**Repository name:** `CSA09-192521452-Assignment2`

This repository contains executable Java implementations for Q1–Q7, benchmark drivers, raw CSV data, test cases, and output logs. Measurements are machine/JVM dependent: rerun the benchmark programs on your own Mac and retain the newly generated CSV files as your own experimental evidence before writing the report.

## Project structure
- `Task01` – String, StringBuilder, StringBuffer ticket generation and timing
- `Task02` – ArrayList, LinkedList and ArrayDeque playlist operation comparison
- `Task03` – Student equality/hashCode contract and HashSet experiments
- `Task04` – HashMap, TreeMap, LinkedHashMap inventory lookup and reporting
- `Task05` – generic Leaderboard, Comparator ordering and comparator tests
- `Task06` – stable emergency triage using PriorityQueue
- `Task07` – Q1 redesign: repeated concatenation versus StringBuilder
- `data` – CSV benchmark results (sample run), test inputs and output logs

## Compile and run
Open Terminal in this folder. For each task:

```bash
javac --release 21 -d out Task01/*.java
java -cp out TicketBenchmark
```

Replace `Task01` and `TicketBenchmark` with the task folder and main class below.

| Task | Compile | Run |
|---|---|---|
| Q1 | `javac --release 21 -d out Task01/*.java` | `java -cp out TicketBenchmark` |
| Q2 | `javac --release 21 -d out Task02/*.java` | `java -cp out PlaylistBenchmark` |
| Q3 | `javac --release 21 -d out Task03/*.java` | `java -cp out Attendance` |
| Q4 | `javac --release 21 -d out Task04/*.java` | `java -cp out PharmacyBenchmark` |
| Q5 | `javac --release 21 -d out Task05/*.java` | `java -cp out LeaderboardDemo` |
| Q6 | `javac --release 21 -d out Task06/*.java` | `java -cp out Triage` |
| Q7 | `javac --release 21 -d out Task07/*.java` | `java -cp out RedesignBenchmark` |

The `-d out` option keeps compiled `.class` files separate from source. Do not commit the generated `out/` folder.

## Re-run raw experiments
Q1, Q2, Q4 and Q7 print timing results and write CSV files under `data/`. Run from the repository root so the relative output paths resolve. Each timing is a repeated measurement; results vary with processor load, JVM warm-up, garbage collection and JDK build. Do not present the included sample values as measurements from your own laptop. Run the programs locally and use the generated CSVs for your report.

## Important design notes
- Q1: `String` is immutable; repeated `+` in a loop can repeatedly copy accumulated characters. `StringBuilder` is appropriate for a local ticket. `StringBuffer` synchronizes individual operations, but a shared log still needs an explicit concurrency design (e.g. one lock/queue or a dedicated logging service) if an entire multi-step record must be atomic.
- Q2: `ArrayList` suits append-heavy and random-access playlists; `LinkedList` has O(n) indexed access and node overhead; `ArrayDeque` is suitable for double-ended Up Next operations, but not indexed access.
- Q3: `equals` and `hashCode` must agree. Fields used for hashing should not be mutated while an object is stored in a hash collection.
- Q4: `HashMap` gives expected O(1) lookup; `TreeMap` keeps key order; `LinkedHashMap` preserves insertion order.
- Q5: a comparator chain expresses descending points, descending goal difference, then ascending name. The generic leaderboard accepts `Comparator<? super T>` and sorts a copy.
- Q6: lower severity number means greater urgency; arrival sequence breaks ties FIFO. A basic priority queue can starve low-priority patients; an ageing policy is discussed in source comments/README notes.
- Q7: the redesign targets repeated string copying. It compares both approaches with the same generated ticket content and checks equality.

## Q7 Design challenge selection
Q7 uses Q1's repeated-concatenation workload as the original limitation. The redesigned version accumulates text with `StringBuilder`. The benchmark compares the two under matching inputs and checks identical output. Your report should diagnose the bottleneck from your own rerun, state the measured result and discuss the trade-offs; do not assume the redesigned version wins for every tiny workload.

## Submission checklist
1. Execute every task yourself and capture genuine output screenshots.
2. Re-run the benchmark drivers and retain the generated CSV files.
3. Add at least one meaningful Git commit per task (the assignment explicitly says the commit history will be checked).
4. Share the repository with faculty (public, or private with faculty added as collaborator).
5. Put the working GitHub repository URL on the report cover page.
6. Be prepared to explain the design, tests, measurements and limitations in a viva.

