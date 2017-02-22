package model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {

    private static TaskList list = new ArrayTaskList();
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String fileName = "tFile.txt";
    private static String formatDate = "dd.MM.yyyy HH.mm.ss";


    public static void main(String[] args) throws IllegalArgumentException, ParseException, IOException {

        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        TaskIO.read(list, new FileReader(fileName));

        MyThread myThready = new MyThread();
        myThready.start();

        int choose=0;
        System.out.println("Current date  " + sdf.format(new Date()));
        System.out.println("----------------The TaskManager v0.1 started---------------");
        System.out.println("Press 1: If u wanna see task list");
        System.out.println("Press 2: If u wanna see calendar of tasks");
        System.out.println("Press 3: If u wanna add the task");
        System.out.println("Press 4: If u wanna change the task");
        System.out.println("Press 5: If u wanna delete the task");
        System.out.println("Press 6: If u wanna have detail task information");
        System.out.println("Press 7: If u wanna exit the programm");
        while (choose!=7) {
            System.out.println("---------------U are in Main menu. Press number---------------");
            try {
                choose = Integer.parseInt(reader.readLine());
                if (choose == 1) {
                    showList();
                } else if (choose == 2) {
                    calendar();
                } else if (choose == 3) {
                    add(list);
                } else if (choose == 4) {
                    change();
                } else if (choose == 5) {
                    delete();
                } else if (choose == 6) {
                    info();
                } else if (choose == 7) {
                    System.out.println("BYE");
                    myThready.stop();
                } else System.out.println("Incorrect input");
            } catch (IllegalArgumentException e) {
                System.out.println("Incorrect input");
            }
        }
    }

    private static void showList() {
        System.out.println("---------------Show list---------------");
        if (list.size() == 0) {
            System.out.println("The List is empty");
        } else {
            System.out.println("Size list: " + list.size() + ". Elements:");
            System.out.println(list);
//            for (int i = 0; i < list.size(); i++) {
//                System.out.println(list.getTask(i));
//            }
        }
    }

    private static void add(TaskList list) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        System.out.println("---------------Add task---------------");
        String title;
        Date tStart;
        Date tEnd;
        int interval;
        Task task;
        System.out.println("Enter the Title of task");
        while (true) {
            title = reader.readLine();
            if ("".equals(title)) System.out.println("Empty title. Repeat please");
            else break;
        }
        System.out.println("Enter the start date of task as dd.MM.yyyy HH.mm.ss");
        while (true) {
            try {
                tStart = sdf.parse(reader.readLine());
                break;
            } catch (ParseException e) {
                System.out.println("Incorrect start date");
            }
        }
        System.out.println("Enter the end date of task as dd.MM.yyyy HH.mm.ss. " +
                "Or enter any text then the task reproduce one time");
        try {
            while (true) {
                tEnd = sdf.parse((reader.readLine()));
                if (tStart.after(tEnd)) {
                    System.out.println("The end date must not be earlier than start date");
                } else break;
            }
            while (true) {
                try {
                    System.out.println("Enter interval");
                    interval = Integer.parseInt(reader.readLine());
                    if (interval > 0) break;
                    else System.out.println("Interval must be >=0");
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect input of interval");
                }
            }
            task = new Task(title, tStart, tEnd, interval);
            System.out.println("Your task repeat after " + task.reInterval(interval * 1000));
        } catch (ParseException e) {
            task = new Task(title, tStart);
        }
        active(task);
        System.out.println("The task " + task + " created");
        list.add(task);
        TaskIO.writeText(list, new File(fileName));
    }

    private static void calendar() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        Map<Date, Set<Task>> map;
        System.out.println("---------------Calendar of tasks---------------");
        Date dstart;
        Date dend;
        while (true) {
            try {
                System.out.println("Enter start date as dd.MM.yyyy HH.mm.ss");
                dstart = sdf.parse(reader.readLine());
                while (true) {
                    try {
                        System.out.println("Enter end date as dd.MM.yyyy HH.mm.ss");
                        dend = sdf.parse(reader.readLine());
                        if (dstart.after(dend)) {
                            System.out.println("The end date must not be earlier than start date");
                        } else break;
                    } catch (ParseException e) {
                        System.out.println("Incorrect input of end date");
                    }
                }
                map = Tasks.calendar(list, dstart, dend);
                for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
                    System.out.println(sdf.format(pair.getKey()));
                    for (Task task : pair.getValue()) {
                        System.out.println(task.getTitle());
                    }
                }
                break;
            } catch (ParseException e) {
                System.out.println("Incorrect input of start date");
            }
        }
    }

    private static void change() throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        System.out.println("---------------Task changing---------------");
        int choose;
        String sTitle = "2";
        String loopTitle = "1";
        System.out.println("Enter task title u want to change");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.getTask(i).getTitle());
        }
        while (!sTitle.equals(loopTitle)) {
            sTitle = reader.readLine();
            for (int i = 0; i < list.size(); i++) {
                if (sTitle.equals(list.getTask(i).getTitle())) {
                    loopTitle = list.getTask(i).getTitle();
                    System.out.println("What u wanna do?");
                    System.out.println("1 - Change title \n2 - Change time or interval \n3 - Change active\n4 - Exit program");
                    while (true) {
                        try {
                            choose = Integer.parseInt(reader.readLine());
                            if (choose == 1) {
                                System.out.println("Enter new Title");
                                String tit = reader.readLine();
                                list.getTask(i).setTitle(tit);
                                System.out.println("Change title complete");
                                break;
                            } else if (choose == 2) {
                                Date newSdate;
                                System.out.println("Enter the start date of task as dd.MM.yyyy HH.mm.ss");
                                while (true) {
                                    try {
                                        newSdate = sdf.parse(reader.readLine());
                                        break;
                                    } catch (ParseException e) {
                                        System.out.println("Incorrect start date. Repeat please");
                                    }
                                }
                                Date newEdate;
                                try {
                                    while (true) {
                                        System.out.println("Enter end date");
                                        newEdate = sdf.parse(reader.readLine());
                                        if (newSdate.after(newEdate)) {
                                            System.out.println("The end date must not be earlier than start date");
                                        } else break;
                                    }
                                    int interv = 0;
                                    while (true) {
                                        try {
                                            System.out.println("Enter interval");
                                            interv = Integer.parseInt(reader.readLine());
                                            if (interv > 0) break;
                                            else System.out.println("Interval must be >=0");
                                        } catch (NumberFormatException e) {
                                            System.out.println("Incorrect input of interval");
                                        }
                                    }
                                    list.getTask(i).setTime(newSdate, newEdate, interv * 1000);
                                    System.out.println(list.getTask(i).getTitle() + " Changed\nYour task repeat after "
                                            + list.getTask(i).reInterval(interv * 1000));
                                } catch (ParseException e) {
                                    list.getTask(i).setTime(newSdate);
                                    System.out.println(list.getTask(i).getTitle() + " Changed");
                                }
                                break;
                            } else if (choose == 3) {
                                active(list.getTask(i));
                                System.out.println(list.getTask(i).getTitle() + " Changed");
                                break;
                            } else if (choose == 4) {
                                System.out.println("Good bye");
                                break;
                            } else System.out.println("Incorrect input. Repeat please");
                        } catch (NumberFormatException e) {
                            System.out.println("Incorrect input. Repeat please");
                        }
                    }
                    TaskIO.writeText(list, new File(fileName));
                    break;
                }
            }
            if (!sTitle.equals(loopTitle)) {
                System.out.println("Title not found. Repeat please");
            }
        }
    }

    private static void active(Task task) throws IOException {
        String active;
        while (true) {
            System.out.println("Make the task active? y/n");
            active = reader.readLine();
            if ("y".equals(active)) {
                task.setActive(true);
                break;
            } else if ("n".equals(active)) {
                task.setActive(false);
                break;
            } else System.out.println("Incorrect input");
        }
    }

    private static void delete() throws IOException {
        System.out.println("---------------Delete the task---------------");
        String sTitle = "1";
        String loopTitle = "2";
        System.out.println("Enter task title u want to delete");
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.getTask(i).getTitle());
        }
        while (true) {
            sTitle = reader.readLine();
            for (int i = 0; i < list.size(); i++) {
                if (sTitle.equals(list.getTask(i).getTitle())) {
                    loopTitle = list.getTask(i).getTitle();
                    list.remove(list.getTask(i));
                }
            }
            if (sTitle.equals(loopTitle)) {
                TaskIO.writeText(list, new File(fileName));
                System.out.println(loopTitle + " deleted");
                break;

            } else {
                System.out.println("Task not found");
            }
        }

    }

    private static void info() throws IOException {
        System.out.println("---------------Detail task info---------------");
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






