import com.sun.xml.internal.xsom.impl.Ref;
import sun.plugin2.gluegen.runtime.CPU;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class Process{

    int listed;
    int arrival;
    int CPUburst;
    int total;
    int io;


    String status;
    int op;
    int IOburst;
    int Remaining_time;
    int Finishing_time;
    int IO_time;
    int Ready_time;
    int Turnaround_time;
    int Waiting_time;
    int CPURemaining;
    int CPURunning;
    double R;
    Boolean newready;

    Process(int l,int a, int b, int c, int m){
        listed = l;
        arrival = a;
        CPUburst = b;
        total = c;
        io = m;
        op = 0;
        IOburst = 0;
        Remaining_time = c;
        IO_time = 0;
        Ready_time = 0;
        Waiting_time = 0;
        CPURunning = 0;
    }
}





public class lab2 {

    static int randomOS(ArrayList<Integer> random, int U) {
        int x = random.get(0);
        random.remove(0);
        return 1 + (x % U);
    }

    static void Ready_insert(ArrayList<Process> Ready, Process ip){
        int insert = 0;
        for (int r=0; r<Ready.size(); r++){
            if (ip.Remaining_time<Ready.get(r).Remaining_time){
                Ready.add(r,ip);
                insert++;
                break;
            }
        }
        if (insert == 0){
            Ready.add(ip);
        }
    }

    static void Ready_sorted(ArrayList<Process> Ready){
        ArrayList<Process> Sorted = new ArrayList<Process>();

    }

    public static void main(String[] args) {

        ArrayList<Process>All = new ArrayList<Process>();
        ArrayList<Process>SortedAll = new ArrayList<Process>();
        ArrayList<Process>Ready = new ArrayList<Process>();
        ArrayList<Process>Running = new ArrayList<Process>();
        //ArrayList<Process>Blocked = new ArrayList<Process>();
        //ArrayList<Process>Unstarted = new ArrayList<Process>();
        ArrayList<Integer>random = new ArrayList<Integer>();
        ArrayList<Process>ToRunning = new ArrayList<Process>();

        int time = 0;
        int process_number = 0;
        int Terminated = 0;
        int CPURunning = 0;
        int IORunning = 0;

        System.out.println("Enter the file name with extension: ");

        Scanner input = new Scanner(System.in);

        File file = new File(input.nextLine());

        File file1 = new File("/Users/pengsihan/Downloads/random-numbers.txt");

        System.out.println("--verbose <y/n>");

        String verbose = input.nextLine();

        System.out.println("Scheduling type: ");

        int type = input.nextInt();

        //Input the process and random_numbers
        try {

            input = new Scanner(file);

            process_number = input.nextInt();

            for (int i=0; i<process_number; i++) {
                int a = input.nextInt();
                int b = input.nextInt();
                int c = input.nextInt();
                int m = input.nextInt();
                Process newprocess = new Process(i, a, b, c, m);
                newprocess.status = "Unstarted";
                //Unstarted.add(newprocess);
                All.add(newprocess);
                if(SortedAll.size()==0){
                    SortedAll.add(newprocess);
                }else{
                    int insert = 0;
                    for(int s=0; s<SortedAll.size(); s++){
                        if(newprocess.arrival < SortedAll.get(s).arrival){
                            SortedAll.add(s, newprocess);
                            insert++;
                            break;
                        }
                    }
                    if (insert == 0){
                        SortedAll.add(newprocess);
                    }

                }
            }

            input.close();

            Scanner random_numbers = new Scanner(file1);

            while(random_numbers.hasNext()){
                int x = random_numbers.nextInt();
                random.add(x);
            }

            random_numbers.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }



        //Start Scheduling
        System.out.print("The original input was: "+process_number);
        for (Process original:All){
            System.out.print(" ("+original.arrival+" "+original.CPUburst+" "+original.total+" "+original.io+")");
        }
        System.out.println();
        System.out.print("The (sorted) input is:  "+process_number);
        for (Process original:SortedAll){
            System.out.print(" ("+original.arrival+" "+original.CPUburst+" "+original.total+" "+original.io+")");
        }
        System.out.println();
        System.out.println();


        if (type == 0 ) { //FCFS
            System.out.println("The scheduling algorithm used was First Come First Served");
            System.out.println();

            while (Terminated < process_number) { //Start another cycle

                int block = 0;

                for (Process p:SortedAll) {
                    if (p.status == "Blocked") {
                        if (p.IOburst == 0) {
                            if (Running.size() == 0 && Ready.size() == 0) {
                                p.status = "Running";
                                int op = randomOS(random, p.CPUburst);
                                if (p.Remaining_time <= op) { //last cycle
                                    p.op = p.Remaining_time;
                                } else { //not last cycle
                                    p.op = op;
                                    p.IOburst = op * p.io;
                                }
                                Running.add(p);
                            } else {
                                p.status = "Ready";
                                p.Ready_time++;
                                p.op = 0;
                                Ready.add(p);
                            }
                        } else {
                            block++;
                            p.IO_time++;
                            p.IOburst--;
                            p.op--;
                        }
                    }
                    if (p.status == "Unstarted") {
                        if (p.arrival == time) {
                            if (Running.size() == 0 && Ready.size() == 0) {
                                p.status = "Running";
                                int op = randomOS(random, p.CPUburst);
                                if (p.Remaining_time <= op) { //last cycle
                                    p.op = p.Remaining_time;
                                } else { //not last cycle
                                    p.op = op;
                                    p.IOburst = op * p.io;
                                }
                                Running.add(p);
                            } else {
                                p.status = "Ready";
                                p.Ready_time++;
                                Ready.add(p);
                            }
                        }
                    }
                }
                if (block>0){
                    IORunning++;
                }


                //Check Running
                if (Running.size() == 0) { //if empty
                    if (Ready.size() != 0) {
                        Process p = Ready.get(0);
                        p.status = "Running";
						int op = randomOS(random, p.CPUburst);
						if (p.Remaining_time <= op){ //last cycle
							p.op = p.Remaining_time;
						}else{ //not last cycle
							p.op = op;
							p.IOburst = op*p.io;
						}
                        Running.add(p);
                        Ready.remove(0);
                    }
                }

                for (Process p:SortedAll){
                    if (p.status=="Ready"){
                        p.Waiting_time++;
                    }
                }

                if (verbose.compareTo("y")==0){
                    System.out.print("Before cycle    "+(time+1)+":");
                    for(Process p:SortedAll){
                        System.out.print("   "+p.status+p.op);
                    }
                    System.out.println();
                }

                time++;

                //Run


                if (Running.size() == 1) { //if not empty
                    CPURunning++;
                    Process p = Running.get(0);
                    p.op--;
                    p.Remaining_time--;
                    if (p.op==0) { //Done
                        if (p.Remaining_time == 0) { //finish
                            p.status = "Terminated";
                            p.Finishing_time = time;
                            p.Turnaround_time = time-p.arrival;
                            Terminated++;
                        }else { //not finish
                            p.status = "Blocked";
                            p.op = p.IOburst+1;
                            //Blocked.add(p);
                        }
                        Running.remove(0);
                    }
                }
            }
        }
        if (type == 1) { //RR with quantum 2
            System.out.println("The scheduling algorithm used was Round Robbin");
            System.out.println();

            while (Terminated < process_number) { //Start another cycle

                int block = 0;

                for (Process p:SortedAll) {
                    if (p.status == "Blocked") {
                        if (p.CPURemaining>0){
                            if (Running.size() == 0 && Ready.size() == 0) {
                                p.status = "Running";
                                if (p.CPURemaining<=2){
                                    p.op = p.CPURemaining;
                                    p.CPURemaining = 0;
                                }else{
                                    p.op = 2;
                                    p.CPURemaining -= 2;
                                }
                                Running.add(p);
                            } else {
                                p.status = "Ready";
                                p.Ready_time++;
                                p.op = 0;
                                Ready.add(p);
                            }
                        }
                        else{
                            if (p.IOburst == 0) {
                                if (Running.size() == 0 && Ready.size() == 0) {
                                    p.status = "Running";
                                    int op = randomOS(random, p.CPUburst);
                                    if (p.Remaining_time <= op) { //last cycle
                                        p.op = p.Remaining_time;
                                    } else { //not last cycle
                                        p.op = op;
                                        p.IOburst = op * p.io;
                                    }
                                    if (p.op>2){
                                        p.CPURemaining = p.op-2;
                                        p.op = 2;
                                    }
                                    Running.add(p);
                                } else {
                                    p.status = "Ready";
                                    p.Ready_time++;
                                    p.op = 0;
                                    Ready.add(p);
                                }
                            } else {
                                block++;
                                p.IO_time++;
                                p.IOburst--;
                                p.op--;
                            }
                        }
                    }
                    if (p.status == "Unstarted") {
                        if (p.arrival == time) {
                            if (Running.size() == 0 && Ready.size() == 0) {
                                p.status = "Running";
                                int op = randomOS(random, p.CPUburst);
                                if (p.Remaining_time <= op) { //last cycle
                                    p.op = p.Remaining_time;
                                } else { //not last cycle
                                    p.op = op;
                                    p.IOburst = op * p.io;
                                }
                                if (p.op>2){
                                    p.CPURemaining = p.op-2;
                                    p.op = 2;
                                }
                                Running.add(p);
                            } else {
                                p.status = "Ready";
                                p.Ready_time++;
                                Ready.add(p);
                            }
                        }
                    }
                }
                if (block>0){
                    IORunning++;
                }


                //Check Running
                if (Running.size() == 0) { //if empty
                    if (Ready.size() != 0) {
                        Process p = Ready.get(0);
                        p.status = "Running";
                        if (p.CPURemaining>0){
                            if (p.CPURemaining<=2){
                                p.op = p.CPURemaining;
                                p.CPURemaining = 0;
                            }else{
                                p.op = 2;
                                p.CPURemaining -= 2;
                            }
                        }else{
                            int op = randomOS(random, p.CPUburst);
                            if (p.Remaining_time <= op){ //last cycle
                                p.op = p.Remaining_time;
                            }else{ //not last cycle
                                p.op = op;
                                p.IOburst = op*p.io;
                            }
                            if (p.op>2){
                                p.CPURemaining = p.op-2;
                                p.op = 2;
                            }

                        }
                        Running.add(p);
                        Ready.remove(0);
                    }
                }

                for (Process p:SortedAll){
                    if (p.status=="Ready"){
                        p.Waiting_time++;
                    }
                }

                if (verbose.compareTo("y")==0){
                    System.out.print("Before cycle    "+(time+1)+":");
                    for(Process p:SortedAll){
                        System.out.print("   "+p.status+p.op);
                    }
                    System.out.println();
                }

                time++;

                //Run


                if (Running.size() == 1) { //if not empty
                    CPURunning++;
                    Process p = Running.get(0);
                    p.op--;
                    p.Remaining_time--;
                    if (p.op==0) { //Done
                        if (p.Remaining_time == 0) { //finish
                            p.status = "Terminated";
                            p.Finishing_time = time;
                            p.Turnaround_time = time-p.arrival;
                            Terminated++;
                        }else { //not finish
                            p.status = "Blocked";
                            p.op = p.IOburst+1;
                            //Blocked.add(p);
                        }
                        Running.remove(0);
                    }
                }

            }
        }

        if (type == 2) { //SJF
            System.out.println("The scheduling algorithm used was Shortest Job First");
            System.out.println();


            while (Terminated < process_number) { //Start another cycle

                int block = 0;

                for (Process p:SortedAll) {
                    if (p.status == "Blocked") {
                        if (p.IOburst == 0) {
                            if (Running.size() == 0 && Ready.size() == 0) {
                                p.status = "ToRunning";
                                ToRunning.add(p);
                            } else {
                                p.status = "Ready";
                                p.Ready_time++;
                                p.op = 0;
                                Ready_insert(Ready,p);
                            }
                        } else {
                            block++;
                            p.IO_time++;
                            p.IOburst--;
                            p.op--;
                        }
                    }
                    if (p.status == "Unstarted") {
                        if (p.arrival == time) {
                            if (Running.size() == 0 && Ready.size() == 0) {
                                p.status = "ToRunning";
                                ToRunning.add(p);
                            } else {
                                p.status = "Ready";
                                p.Ready_time++;
                                p.op = 0;
                                Ready_insert(Ready,p);
                            }
                        }
                    }
                }

                if (block>0){
                    IORunning++;
                }


                //Check ToRunning
                if (ToRunning.size() != 0){
                    //Take out the shortest process
                    int shortest_time = Integer.MAX_VALUE;
                    int shortest = 0;
                    for (int s=0; s<ToRunning.size(); s++){
                        if (ToRunning.get(s).Remaining_time < shortest_time){
                            shortest = s;
                            shortest_time = ToRunning.get(s).Remaining_time;
                        }
                    }
                    Process run = ToRunning.get(shortest);
                    run.status = "Running";
                    int op = randomOS(random, run.CPUburst);
                    if (run.Remaining_time <= op) { //last cycle
                        run.op = run.Remaining_time;
                    } else { //not last cycle
                        run.op = op;
                        run.IOburst = op * run.io;
                    }
                    Running.add(run);
                    ToRunning.remove(shortest);


                    //Insert the rest of process to Ready
                    for (int s=0; s<ToRunning.size(); s++){
                        Process ready = ToRunning.get(s);
                        ready.CPURemaining = ready.op;
                        ready.op = 0;
                        ready.status = "Ready";
                        Ready_insert(Ready,ready);
                    }

                    ToRunning = new ArrayList<Process>();
                }


                //Check Running
                if (Running.size() == 0) { //if empty
                    if (Ready.size() != 0) {
                        Process p = Ready.get(0);
                        p.status = "Running";
                        int op = randomOS(random, p.CPUburst);
                        if (p.Remaining_time <= op){ //last cycle
                            p.op = p.Remaining_time;
                        }else{ //not last cycle
                            p.op = op;
                            p.IOburst = op*p.io;
                        }
                        Running.add(p);
                        Ready.remove(0);
                    }
                }



                for (Process p:SortedAll){
                    if (p.status=="Ready"){
                        p.Waiting_time++;
                    }
                }

                if (verbose.compareTo("y")==0){
                    System.out.print("Before cycle    "+(time+1)+":");
                    for(Process p:SortedAll){
                        System.out.print("   "+p.status+p.op);
                    }
                    System.out.println();
                }

                time++;

                //Run


                if (Running.size() == 1) { //if not empty
                    CPURunning++;
                    Process p = Running.get(0);
                    p.op--;
                    p.Remaining_time--;
                    if (p.op==0) { //Done
                        if (p.Remaining_time == 0) { //finish
                            p.status = "Terminated";
                            p.Finishing_time = time;
                            p.Turnaround_time = time-p.arrival;
                            Terminated++;
                        }else { //not finish
                            p.status = "Blocked";
                            p.op = p.IOburst+1;
                            //Blocked.add(p);
                        }
                        Running.remove(0);
                    }
                }




            }

        }

        if (type == 3) { //HPRN
            System.out.println("The scheduling algorithm used was Highest Penalty Ratio Next");
            System.out.println();



            while (Terminated<process_number){

                int block = 0;

                for (Process p:SortedAll) {
                    if (p.status == "Blocked") {
                        if (p.IOburst == 0) {
                            if (Running.size() == 0 && Ready.size() == 0) {
                                p.status = "ToRunning";
                                ToRunning.add(p);
                            } else {
                                p.status = "Ready";
                                p.Ready_time++;
                                p.newready = true;
                                p.op = 0;
                                Ready_insert(Ready,p);
                            }
                        } else {
                            block++;
                            p.IO_time++;
                            p.IOburst--;
                            p.op--;
                        }
                    }
                    if (p.status == "Unstarted") {
                        if (p.arrival == time) {
                            if (Running.size() == 0 && Ready.size() == 0) {
                                p.status = "ToRunning";
                                ToRunning.add(p);
                            } else {
                                p.status = "Ready";
                                p.Ready_time++;
                                p.op = 0;
                                Ready_insert(Ready,p);
                            }
                        }
                    }
                }

                if (block>0){
                    IORunning++;
                }

                //Update Process.R
                for (Process p: SortedAll){
                    if (p.status == "Ready" || p.status == "ToRunning"){
                        if(p.CPURunning==0){
                            p.R = (double) (time-p.arrival)/1;
                        }
                        else{
                            p.R = (double) (time-p.arrival)/p.CPURunning;
                        }
                    }
                }

                //Check ToRunning
                if( ToRunning.size() != 0){
                    //Take the highest Process.R in ToRunning
                    int highest = 0;
                    double highest_R = Double.MIN_VALUE;
                    Process hp = ToRunning.get(0);
                    for (int s=0; s<ToRunning.size(); s++){
                        if (ToRunning.get(s).R == hp.R){
                            if (ToRunning.get(s).arrival==hp.arrival){
                                if (ToRunning.get(s).listed<hp.listed){
                                    highest = s;
                                    hp = ToRunning.get(s);
                                }
                            }
                            if (ToRunning.get(s).arrival<hp.arrival){
                                highest = s;
                                hp = ToRunning.get(s);
                            }
                        }
                        if (ToRunning.get(s).R > hp.R){
                            highest = s;
                            hp = ToRunning.get(s);
                        }
                    }
                    Process run = ToRunning.get(highest);
                    run.status = "Running";
                    int op = randomOS(random, run.CPUburst);
                    if (run.Remaining_time <= op) { //last cycle
                        run.op = run.Remaining_time;
                    } else { //not last cycle
                        run.op = op;
                        run.IOburst = op * run.io;
                    }
                    Running.add(run);
                    ToRunning.remove(highest);


                    //Insert the rest of process to Ready
                    for (int s=0; s<ToRunning.size(); s++){
                        Process ready = ToRunning.get(s);
                        ready.CPURemaining = ready.op;
                        ready.op = 0;
                        ready.status = "Ready";
                        Ready_insert(Ready,ready);
                    }

                    ToRunning = new ArrayList<Process>();

                }

                //Check Running
                if (Running.size() == 0) { //if empty
                    if (Ready.size() != 0) {
                        int highest = 0;

                        Process hp = Ready.get(0);
                        for (int r=0; r<Ready.size(); r++){
                            if (Ready.get(r).R == hp.R){
                                if (Ready.get(r).arrival==hp.arrival){
                                    if (Ready.get(r).listed < hp.listed){
                                        highest = r;
                                        hp = Ready.get(r);
                                    }
                                }
                                if (Ready.get(r).arrival<hp.arrival){
                                    highest = r;
                                    hp = Ready.get(r);
                                }
                            }
                            if (Ready.get(r).R > hp.R){
                                highest = r;

                                hp = Ready.get(r);
                            }
                        }
                        Process p = Ready.get(highest);
                        p.status = "Running";
                        int op = randomOS(random, p.CPUburst);
                        if (p.Remaining_time <= op){ //last cycle
                            p.op = p.Remaining_time;
                        }else{ //not last cycle
                            p.op = op;
                            p.IOburst = op*p.io;
                        }
                        Running.add(p);
                        Ready.remove(highest);
                    }
                }



                for (Process p:SortedAll){
                    if (p.status=="Ready"){
                        p.newready = false;
                        p.Waiting_time++;
                    }
                }

                if (verbose.compareTo("y")==0){
                    System.out.print("Before cycle    "+(time+1)+":");
                    for(Process p:SortedAll){
                        System.out.print("   "+p.status+p.op);
                    }
                    System.out.println();
                }

                time++;

                //Run


                if (Running.size() == 1) { //if not empty
                    CPURunning++;
                    Process p = Running.get(0);
                    p.op--;
                    p.Remaining_time--;
                    p.CPURunning++;
                    if (p.op==0) { //Done
                        if (p.Remaining_time == 0) { //finish
                            p.status = "Terminated";
                            p.Finishing_time = time;
                            p.Turnaround_time = time-p.arrival;
                            Terminated++;
                        }else { //not finish
                            p.status = "Blocked";
                            p.op = p.IOburst+1;
                            //Blocked.add(p);
                        }
                        Running.remove(0);
                    }
                }


            }

        }

        //Get the output

        for (int op = 0; op<process_number; op++){ //each process
            Process output = SortedAll.get(op);
            System.out.println("Process "+op+":");
            System.out.print("        ");
            System.out.println("(A,B,C,M) = ("+output.arrival+","+output.CPUburst+","+output.total+","+output.io+")");
            System.out.print("        ");
            System.out.println("Finishing time: "+output.Finishing_time);
            System.out.print("        ");
            System.out.println("Turnaround time: "+output.Turnaround_time);
            System.out.print("        ");
            System.out.println("I/O time: "+output.IO_time);
            System.out.print("        ");
            System.out.println("Waiting time: "+output.Waiting_time);
            System.out.println();
        }

        //summary
        int TT = 0;
        int WT = 0;
        for (Process p: All){
            TT += p.Turnaround_time;
            WT += p.Waiting_time;

        }
        System.out.println("Summary Data:");
        System.out.print("        ");
        System.out.println("Finishing time: "+time);
        System.out.print("        ");
        double CPUU = (double)CPURunning / time;
        System.out.println("CPU Utilization: "+ String.format("%.6f",CPUU));
        System.out.print("        ");
        double IOU = (double) IORunning / time;
        System.out.println("I/O Utilization: "+ String.format("%.6f",IOU));
        System.out.print("        ");
        double Throughput = (double) Terminated *100 /time;
        System.out.println("Throughput: "+ String.format("%.6f",Throughput)+" processes per hundred cycles");
        System.out.print("        ");
        double ATT = (double) TT / Terminated;
        System.out.println("Average turnaround time: "+String.format("%.6f",ATT));
        System.out.print("        ");
        double AWT = (double) WT / Terminated;
        System.out.println("Average waiting time: "+String.format("%.6f",AWT));

    }

}
