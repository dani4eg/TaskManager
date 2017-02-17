//package controller;
//
//import model.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//import java.util.Set;
//
//
//public class lololo {
//
//    private static TaskList list = new ArrayTaskList();
//    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss");
//    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//    static Logger logger = LoggerFactory.getLogger(Tasks.class);
//
//    public static void main(String[] args) throws IllegalArgumentException, ParseException, IOException {
//
//        TaskIO.read(list, new FileReader("tFile.txt"));
//
//        final Thread myThready = new Thread(new Runnable()
//        {
//            public void run()
//            {
//                long d;
//                Date sdate = new Date();
//                Date edate = new Date(sdate.getTime() + (66400000));
//                Map<Date, Set<Task>> map = Tasks.calendar(list, sdate, edate);
//                for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
//                    d = pair.getKey().getTime() - (sdate.getTime());
//                    sdate = pair.getKey();
//                    System.out.println("Near task done after " + d/1000 + " sec.");
//                    try {
//                        Thread.sleep(d);
//                        System.out.println(sdf.format(pair.getKey()));
//                        for (Task task : pair.getValue()) {
//                            logger.info("The " + task.getTitle() + " is done.");
//                            System.out.println("DING DING.......The " + task.getTitle() + " is done.");
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        myThready.start();
//
//        int t = 0;
//        System.out.println("Current date  " + sdf.format(new Date()));
//        System.out.println("----------------The TaskManager v0.1 started---------------");
//        System.out.println("Press 1: If u wanna see task list");
//        System.out.println("Press 2: If u wanna see calendar of tasks");
//        System.out.println("Press 3: If u wanna add the task");
//        System.out.println("Press 4: If u wanna change the task");
//        System.out.println("Press 5: If u wanna delete the task");
//        System.out.println("Press 6: If u wanna have detail task information");
//        System.out.println("Press 7: If u wanna exit the programm");
//        while (t != 7) {
//            System.out.println("---------------U are in Main menu. Press number---------------");
//            try {
//                t = Integer.parseInt(reader.readLine());
//                if (t == 1) {
//                    showList();
//                } else if (t == 2) {
//                    calendar();
//                } else if (t == 3) {
//                    add(list);
//                } else if (t == 4) {
//                    change();
//                } else if (t == 5) {
//                    delete();
//                } else if (t == 6) {
//                    info();
//                } else if (t == 7) {
//                } else System.out.println("Incorrect input");
//            } catch (IllegalArgumentException e) {
//                System.out.println("Incorrect input");
//            }
//        }
//    }
//
//    private static void showList() {
//        System.out.println("---------------Show list---------------");
//        if (list.size() == 0) {
//            System.out.println("The List is empty");
//        } else {
//            System.out.println("Size list: " + list.size() + ". Elements:");
//            System.out.println(list);
////            for (int i = 0; i < list.size(); i++) {
////                System.out.println(list.getTask(i));
////            }
//        }
//    }
//
//    private static void add(TaskList list) throws IOException, ParseException {
//        System.out.println("---------------Add task---------------");
//        String title;
//        Date tStart;
//        Date tEnd;
//        int interval;
//        Task task;
//        System.out.println("Enter the Title of task");
//        while (true) {
//            title = reader.readLine();
//            if (!title.equals("")) break;
//            else System.out.println("Empty title. Repeat please");
//        }
//        System.out.println("Enter the start date of task as dd.MM.yyyy HH.mm.ss");
//        while (true) {
//            try {
//                tStart = sdf.parse(reader.readLine());
//                break;
//            } catch (ParseException e) {
//                System.out.println("Incorrect start date");
//            }
//        }
//        System.out.println("Enter the end date of task as dd.MM.yyyy HH.mm.ss. " +
//                "Or enter any text then the task reproduce one time");
//        try {
//            while (true) {
//                tEnd = sdf.parse((reader.readLine()));
//                if (tStart.after(tEnd)) {
//                    System.out.println("The end date must not be earlier than start date");
//                } else break;
//            }
//            while (true) {
//                try {
//                    System.out.println("Enter interval");
//                    interval = Integer.parseInt(reader.readLine());
//                    if (interval > 0) break;
//                    else System.out.println("Interval must be >=0");
//                } catch (NumberFormatException e) {
//                    System.out.println("Incorrect input of interval");
//                }
//            }
//            task = new Task(title, tStart, tEnd, interval);
//            System.out.println("Your task repeat after " + task.reInterval(interval * 1000));
//        } catch (ParseException e) {
//            task = new Task(title, tStart);
//        }
//        active(task);
//        System.out.println("The task " + task + " created");
//        list.add(task);
//        TaskIO.writeText(list, new File("tFile.txt"));
//    }
//
//    private static void calendar() throws IOException {
//        Map<Date, Set<Task>> map;
//        System.out.println("---------------Calendar of tasks---------------");
//        Date dstart;
//        Date dend;
//        while (true) {
//            try {
//                System.out.println("Enter start date as dd.MM.yyyy HH.mm.ss");
//                dstart = sdf.parse(reader.readLine());
//                while (true) {
//                    try {
//                        System.out.println("Enter end date as dd.MM.yyyy HH.mm.ss");
//                        dend = sdf.parse(reader.readLine());
//                        if (dstart.after(dend)) {
//                            System.out.println("The end date must not be earlier than start date");
//                        } else break;
//                    } catch (ParseException e) {
//                        System.out.println("Incorrect input of end date");
//                    }
//                }
//                map = Tasks.calendar(list, dstart, dend);
//                for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
//                    System.out.println(sdf.format(pair.getKey()));
//                    for (Task task : pair.getValue()) {
//                        System.out.println(task.getTitle());
//                    }
//                }
//                break;
//            } catch (ParseException e) {
//                System.out.println("Incorrect input of start date");
//            }
//        }
//    }
//
//    private static void change() throws IOException, ParseException {
//        System.out.println("---------------Task changing---------------");
//        int ch;
//        String sTitle = "2";
//        String loopTitle = "1";
//        System.out.println("Enter task title u want to change");
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.getTask(i).getTitle());
//        }
//        while (!sTitle.equals(loopTitle)) {
//            sTitle = reader.readLine();
//            for (int i = 0; i < list.size(); i++) {
//                if (sTitle.equals(list.getTask(i).getTitle())) {
//                    loopTitle = list.getTask(i).getTitle();
//                    System.out.println("What u wanna do?");
//                    System.out.println("1 - Change title \n2 - Change time or interval \n3 - Change active\n4 - Exit program");
//                    while (true) {
//                        try {
//                            ch = Integer.parseInt(reader.readLine());
//                            if (ch == 1) {
//                                System.out.println("Enter new Title");
//                                String tit = reader.readLine();
//                                list.getTask(i).setTitle(tit);
//                                System.out.println("Change title complete");
//                                break;
//                            } else if (ch == 2) {
//                                Date newSdate;
//                                System.out.println("Enter the start date of task as dd.MM.yyyy HH.mm.ss");
//                                while (true) {
//                                    try {
//                                        newSdate = sdf.parse(reader.readLine());
//                                        break;
//                                    } catch (ParseException e) {
//                                        System.out.println("Incorrect start date. Repeat please");
//                                    }
//                                }
//                                Date newEdate;
//                                try {
//                                    while (true) {
//                                        System.out.println("Enter end date");
//                                        newEdate = sdf.parse(reader.readLine());
//                                        if (newSdate.after(newEdate)) {
//                                            System.out.println("The end date must not be earlier than start date");
//                                        } else break;
//                                    }
//                                    int interv = 0;
//                                    while (true) {
//                                        try {
//                                            System.out.println("Enter interval");
//                                            interv = Integer.parseInt(reader.readLine());
//                                            if (interv > 0) break;
//                                            else System.out.println("Interval must be >=0");
//                                        } catch (NumberFormatException e) {
//                                            System.out.println("Incorrect input of interval");
//                                        }
//                                    }
//                                    list.getTask(i).setTime(newSdate, newEdate, interv * 1000);
//                                    System.out.println(list.getTask(i).getTitle() + " Changed\nYour task repeat after "
//                                            + list.getTask(i).reInterval(interv * 1000));
//                                } catch (ParseException e) {
//                                    list.getTask(i).setTime(newSdate);
//                                    System.out.println(list.getTask(i).getTitle() + " Changed");
//                                }
//                                break;
//                            } else if (ch == 3) {
//                                active(list.getTask(i));
//                                System.out.println(list.getTask(i).getTitle() + " Changed");
//                                break;
//                            } else if (ch == 4) {
//                                System.out.println("Good bye");
//                                break;
//                            } else System.out.println("Incorrect input. Repeat please");
//                        } catch (NumberFormatException e) {
//                            System.out.println("Incorrect input. Repeat please");
//                        }
//                    }
//                    TaskIO.writeText(list, new File("tFile.txt"));
//                    break;
//                }
//            }
//            if (!sTitle.equals(loopTitle)) {
//                System.out.println("Title not found. Repeat please");
//            }
//        }
//    }
//
//    private static void active(Task task) throws IOException {
//        String active;
//        while (true) {
//            System.out.println("Make the task active? y/n");
//            active = reader.readLine();
//            if (active.equals("y")) {
//                task.setActive(true);
//                break;
//            } else if (active.equals("n")) {
//                task.setActive(false);
//                break;
//            } else System.out.println("Incorrect input");
//        }
//    }
//
//    private static void delete() throws IOException {
//        System.out.println("---------------Delete the task---------------");
//        String sTitle = "1";
//        String loopTitle = "2";
//        System.out.println("Enter task title u want to delete");
//        System.out.println(list.size());
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.getTask(i).getTitle());
//        }
//        while (true) {
//            sTitle = reader.readLine();
//            for (int i = 0; i < list.size(); i++) {
//                if (sTitle.equals(list.getTask(i).getTitle())) {
//                    loopTitle = list.getTask(i).getTitle();
//                    list.remove(list.getTask(i));
//                }
//            }
//            if (!sTitle.equals(loopTitle)) {
//                System.out.println("Task not found");
//            } else {
//                TaskIO.writeText(list, new File("tFile.txt"));
//                System.out.println(loopTitle + " deleted");
//                break;
//            }
//        }
//
//    }
//
//    private static void info() throws IOException {
//        System.out.println("---------------Detail task info---------------");
//        String sTitle;
//        System.out.println("Enter task title u want to detail info");
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.getTask(i).getTitle());
//        }
//        sTitle = reader.readLine();
//        for (int i = 0; i < list.size(); i++) {
//            if (sTitle.equals(list.getTask(i).getTitle())) {
//                System.out.println(list.getTask(i));
//                break;
//            }
//        }
//    }
//
//
//
// }
//
//
//
//
//
//
