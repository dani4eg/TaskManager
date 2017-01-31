package model;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by darthMilash on 30.01.2017.
 */
public class Main {

//    static Logger logger = LoggerFactory.getLogger(Main.class);
//
//    public static int sum(int a, int b) {
//        return a+b;
//    }

    public static void main(String[] args) throws IllegalArgumentException, ParseException {

//        logger.info("Hello world");
//
//        int result = sum(4, 5);
//        System.err.println("4 + 5 is " + result);



        Task task1 = new Task("Task1", new Date(1984731506202L), new Date(1984731506204L), 86400);
        Task task2 = new Task("Task2", -1484743545);
        Task task3 = new Task("Task3", new Date(1484731506204L), new Date(1984731506204L), 20000);

        Task task4 = new Task("Task4", new Date(1474731506204L), new Date(1984731506204L), 2000);
        Task task5 = new Task("Task5", new Date(1488731506204L), new Date(1984731506204L), 2000);
        Task task6 = new Task("Task6", new Date(1489731506204L), new Date(1984731506204L), 200);
        Task task7 = new Task("Task7", 14847315, 1234315062, 148473);
        Task task8 = new Task("Task8", 1484743545, 1984731506, 86402);

        task1.setActive(true);
        task2.setActive(true);
        task3.setActive(true);
        task8.setActive(true);
        task7.setActive(true);



        ArrayTaskList list = new ArrayTaskList();
        list.add(task1);
        list.add(task2);
        list.add(task3);
        list.add(task4);
        list.add(task5);
        list.add(task6);
        list.add(task7);
        list.add(task8);

        LinkedTaskList list2 = new LinkedTaskList();
        File bFile = new File("bFile.txt");
        TaskIO.writeBinary(list, bFile);
        TaskIO.readBinary(list2, bFile);

        System.out.println("ArrayList. Testing BINARY writing tasks in file");
        System.out.println(list);
        System.out.println("LinkedList. Testing BINARY reading tasks from file in tasklist");
        System.out.println(list2);




        LinkedTaskList list3 = new LinkedTaskList();
        list3.add(task1);
        list3.add(task2);
        list3.add(task3);
        list3.add(task4);
        list3.add(task5);
        list3.add(task6);
        list3.add(task7);
        list3.add(task8);
        ArrayTaskList list4 = new ArrayTaskList();

        File tFile = new File("tFile.txt");
        TaskIO.writeText(list3, tFile);
        TaskIO.readText(list4,tFile);

        System.out.println("LinkedList. Testing TEXT writing tasks in file");
        System.out.println(list3);
        System.out.println("ArrayList. Testing TEXT reading tasks from file in tasklist");
        System.out.println(list4);





    }

}




