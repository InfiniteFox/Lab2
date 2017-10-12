# Lab2

### How to Use
```
1.Run lab2.java
2.Keyboard input the file name with extension
3.Keyboard input whether verbose <y/n>
4.Keyboard input which type of scheduling to use
  0 -> FCFS
  1 -> RR
  2 -> SJF
  3 -> HPRN
```

### Example Input
```
[System]
Enter the file name with extension: 
[Input]
/Users/pengsihan/Downloads/inputs/input-4.txt
[System]
--verbose <y/n>
[Input]
n
[System]
Scheduling type: 
[Input]
3
```

### Example Output
```
The original input was: 5 (0 3 200 3) (0 9 500 2) (0 20 500 1) (100 1 100 3) (100 100 500 1)
The (sorted) input is:  5 (0 3 200 3) (0 9 500 2) (0 20 500 1) (100 1 100 3) (100 100 500 1)

The scheduling algorithm used was Highest Penalty Ratio Next

Process 0:
        (A,B,C,M) = (0,3,200,3)
        Finishing time: 1634
        Turnaround time: 1634
        I/O time: 597
        Waiting time: 837

Process 1:
        (A,B,C,M) = (0,9,500,2)
        Finishing time: 2290
        Turnaround time: 2290
        I/O time: 994
        Waiting time: 796

Process 2:
        (A,B,C,M) = (0,20,500,1)
        Finishing time: 1669
        Turnaround time: 1669
        I/O time: 492
        Waiting time: 677

Process 3:
        (A,B,C,M) = (100,1,100,3)
        Finishing time: 1337
        Turnaround time: 1237
        I/O time: 297
        Waiting time: 840

Process 4:
        (A,B,C,M) = (100,100,500,1)
        Finishing time: 1292
        Turnaround time: 1192
        I/O time: 492
        Waiting time: 200

Summary Data:
        Finishing time: 2290
        CPU Utilization: 0.786026
        I/O Utilization: 0.686463
        Throughput: 0.218341 processes per hundred cycles
        Average turnaround time: 1604.400000
        Average waiting time: 670.000000
```
        
