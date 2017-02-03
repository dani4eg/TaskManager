package model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by darthMilash on 30.01.2017.
 */
public class Main {


    static Logger logger = LoggerFactory.getLogger(Main.class);

    static ArrayTaskList list = new ArrayTaskList();
    static int t;



    static int start;
    static int end;



    static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss");
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


//
//    public static int sum(int a, int b) {
//        return a+b;
//    }

    public static void main(String[] args) throws IllegalArgumentException, ParseException, IOException {


        TaskIO.read(list, new FileReader("tFile.txt"));




//        TaskIO.readText(list, "tFile.txr");

        System.out.println("The TaskManager v0.1 started");
        System.out.println("Press 1: If u wanna see task list");
        System.out.println("Press 2: If u wanna see calendar of tasks");
        System.out.println("Press 3: If u wanna add the task");
        System.out.println("Press 4: If u wanna change the task");
        System.out.println("Press 5: If u wanna delete the task");
        System.out.println("Press 6: If u wanna have detail task information");
        while (true) {
            try {
                t = Integer.parseInt(reader.readLine());
                if (t == 1) {
                    showList();
                    break;
                } else if (t == 2) {
                    calendar();
                    break;
                } else if (t == 3) {
                    add(list);
                    break;
                } else if (t == 4) {
                    change();
                    break;
                } else if (t == 5) {
                    delete();
                    break;
                } else if (t == 6) {
                    info();
                    break;
                } else System.out.println("Incorrect input");
            } catch (IllegalArgumentException e) {
                System.out.println("Incorrect input");
            }

        }

    }

    private static void showList() {
        if (list.size() == 0) {
            System.out.println("The TaskList is empty");
        } else System.out.println(list);
    }

    private static void add(ArrayTaskList list) throws IOException, ParseException {
        String title;
        Date tStart;
        Date tEnd;
        int interval = 0;
        Task task;
        System.out.println("Enter the Title of task");
        title = reader.readLine();
        System.out.println("Enter the start date of task as dd.MM.yyyy HH.mm.ss");
        tStart = sdf.parse(reader.readLine());
        System.out.println("Enter the end date of task as dd.MM.yyyy HH.mm.ss. " +
                "If end date is empty, the task reproduce one time");
        try {
            while (true) {
                tEnd = sdf.parse((reader.readLine()));
                if (tStart.after(tEnd)) {
                    System.out.println("The end date must not be earlier than start date");
                } else break;
            }
            System.out.println("Enter the interval in sec");
            interval = Integer.parseInt(reader.readLine());
            task = new Task(title, tStart, tEnd, interval);
            System.out.println("Your task repeat after " + task.reInterval(interval * 1000));
        } catch (ParseException e) {
            task = new Task(title, tStart);
        }
        active(task);
        System.out.println("The task " + task + " created");
        list.add(task);
        TaskIO.writeText(list, new File("tFile.txt"));
    }

    private static void calendar() throws IOException {
        Date dstart;
        Date dend;
        try {
            System.out.println("Enter start date as dd.MM.yyyy HH.mm.ss");
            dstart = sdf.parse(reader.readLine());
            while (true) {
                    System.out.println("Enter end date as dd.MM.yyyy HH.mm.ss");
                    dend = sdf.parse(reader.readLine());
                    if (dstart.after(dend)) {
                        System.out.println("The end date must not be earlier than start date");
                    } else break;
            }
            Tasks.calendar(list, dstart, dend);
        }
        catch (ParseException e) {
            logger.error("Incorrect input of date");
            System.out.println("Incorrect input of date");
        }
    }

    private static void change() throws IOException, ParseException {
        int ch;
        String sTitle;
        System.out.println("Enter task title u want to change");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.getTask(i).getTitle());
        }
        sTitle = reader.readLine();
        for (int i = 0; i < list.size(); i++) {
            if (sTitle.equals(list.getTask(i).getTitle())) {
                System.out.println("What u wanna do?");
                System.out.println("1 - Change title \n2 - Change time or interval \n3 - Change active\n4 - Exit program");
                while (true) {
                    try {
                        ch = Integer.parseInt(reader.readLine());
                        if (ch == 1) {
                            System.out.println("Enter new Title");
                            String tit = reader.readLine();
                            list.getTask(i).setTitle(tit);
                            System.out.println("Change title complete");
                            break;
                        }
                        else if (ch == 2) {
                            System.out.println("Enter the start date of task as dd.MM.yyyy HH.mm.ss");
                            Date newSdate = sdf.parse(reader.readLine());
                            Date newEdate;
                            try {
                                while (true) {
                                    System.out.println("Enter end date");
                                    newEdate = sdf.parse(reader.readLine());
                                    if (newSdate.after(newEdate)) {
                                        System.out.println("The end date must not be earlier than start date");
                                    } else break;
                                }
                                System.out.println("Enter interval");
                                int interv = Integer.parseInt(reader.readLine());
                                list.getTask(i).setTime(newSdate, newEdate, interv);
                                    System.out.println(list.getTask(i).getTitle() + " Changed\nYour task repeat after "
                                            + list.getTask(i).reInterval(interv * 1000));
                            }
                            catch (ParseException e) {
                                list.getTask(i).setTime(newSdate);
                                System.out.println(list.getTask(i).getTitle() + " Changed");
                            }
                            break;
                        }
                        else if (ch == 3) {
                            active(list.getTask(i));
                            break;
                        }
                        else if (ch == 4) break;
                        else System.out.println("Incorrect input. Repeat please");
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Incorrect input. Repeat please");
                    }
                }
                TaskIO.writeText(list, new File("tFile.txt"));
                break;
            }
        }
    }

    private static void active(Task task) throws IOException {
        String active;
        while (true) {
            System.out.println("Make the task active? y/n");
            active = reader.readLine();
            if (active.equals("y")) {
                task.setActive(true);
                break;
            } else if (active.equals("n")) {
                task.setActive(false);
                break;
            } else System.out.println("Incorrect input");
        }
    }

    private static void delete() throws IOException {
        String sTitle;
        System.out.println("Enter task title u want to delete");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.getTask(i).getTitle());
        }
        sTitle = reader.readLine();
        for (int i = 0; i < list.size(); i++) {
            if (sTitle.equals(list.getTask(i).getTitle())) {
                list.remove(list.getTask(i));
                System.out.println("Task deleted");
                break;
            }
        }
        TaskIO.writeText(list, new File("tFile.txt"));
    }

    private static void info() throws IOException {
        String sTitle;
        System.out.println("Enter task title u want to detail info");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.getTask(i).getTitle());
        }
        sTitle = reader.readLine();
        for (int i = 0; i < list.size(); i++) {
            if (sTitle.equals(list.getTask(i).getTitle())) {
                System.out.println(list.getTask(i));
                break;
            }
        }
    }
 }



//
//
//
//        Task task1 = new Task("Task1", new Date(1984731506202L), new Date(1984731506204L), 86400);
//        Task task2 = new Task("Task2", 1484743545);
//        Task task3 = new Task("Task3", new Date(1484731506204L), new Date(1984731506204L), 20000);
//
//        Task task4 = new Task("Task4", new Date(1474731506204L), new Date(1984731506204L), 2000);
//        Task task5 = new Task("Task5", new Date(1488731506204L), new Date(1984731506204L), 2000);
//        Task task6 = new Task("Task6", new Date(1489731506204L), new Date(1984731506204L), 200);
//        Task task7 = new Task("Task7", 120, 121, 1);
//        Task task8 = new Task("Task8", 1484743545, 1984731506, 86402);
//
//        task1.setActive(true);
//        task2.setActive(true);
//        task3.setActive(true);
//        task8.setActive(true);
//        task7.setActive(true);
//
//        task1.setTitle("TASKKK1");
//
//        task1.setTime(1000000);
//
//
//
//        ArrayTaskList list = new ArrayTaskList();
//        list.add(task1);
//        list.add(task2);
//        list.add(task3);
//        list.add(task4);
////        list.add(task5);
////        list.add(task6);
////        list.add(task7);
////        list.add(task8);
////
//        LinkedTaskList list2 = new LinkedTaskList();
//        File bFile = new File("bFile.txt");
//        TaskIO.writeBinary(list, bFile);
//        TaskIO.readBinary(list2, bFile);
////
//        System.out.println("ArrayList. Testing BINARY writing tasks in file");
//        System.out.println(list);
//        System.out.println("LinkedList. Testing BINARY reading tasks from file in tasklist");
//        System.out.println(list2);
////
////
////
////
//        LinkedTaskList list3 = new LinkedTaskList();
//        list3.add(task1);
//        list3.add(task2);
//        list3.add(task3);
//        list3.add(task4);
//        list3.add(task5);
//        list3.add(task6);
//        list3.add(task7);
//        list3.add(task8);
//        ArrayTaskList list4 = new ArrayTaskList();
//
//        File tFile = new File("tFile.txt");
//        TaskIO.writeText(list3, tFile);
//        TaskIO.readText(list4,tFile);
//
//        System.out.println("LinkedList. Testing TEXT writing tasks in file");
//        System.out.println(list3);
//        System.out.println("ArrayList. Testing TEXT reading tasks from file in tasklist");
//        System.out.println(list4);

//
//        System.out.println(task1);
//        System.out.println(task4);







