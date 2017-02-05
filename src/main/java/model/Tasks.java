package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by darthMilash on 30.01.2017.
 */
public class Tasks {

    static Logger logger = LoggerFactory.getLogger(Tasks.class);
    static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss");
    /**
     * Метод возвращает список задач, которые будут выполнены в данном промежутке времени
     * Создаем новый лист
     * @param start задача должна быть выполнена не раньше заданного времени
     * @param end задача должна быть выполнена не позже заданного времени
     * если время следующего выполнения задачи относительно заданного времени from выполняется не раньше from и не позже to
     * задача добавляется в список
     * @return список с задачами
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) {
        TaskList list = new ArrayTaskList();
        Iterator iterator = tasks.iterator();
        while(iterator.hasNext()) {
            Task task = (Task) iterator.next();
            Date sdate = task.nextTimeAfter(start);
            if ((start.before(sdate) || start.equals(sdate)) && (end.after(sdate) || end.equals(sdate))) {
                list.add(task);
            }
        }
        return list;
    }

    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end)  throws IllegalArgumentException{
        if(end.before(start)) {
            logger.error("The end date must not be earlier than start date");
            throw new IllegalArgumentException();
        }
        SortedMap<Date, Set<Task>> map = new TreeMap<>();
        Iterable<Task> tasksList = incoming(tasks, start, end);
        Iterator iterator = tasksList.iterator();
        while(iterator.hasNext()) {
            Task task = (Task)iterator.next();
            Date date = start;
            while((date.before(end) || date.equals(end))) {
//                if (date.equals(task.nextTimeAfter(date)) && date.getTime() != -1) {
                    Set<Task> set = new HashSet<>();
                    date = task.nextTimeAfter(date);
                    if (map.containsKey(date)) {
                        set = map.get(date);
                    }
                    set.add(task);
                    map.put((Date) date.clone(), set);
                    date.setTime(date.getTime() + task.getRepeatInterval());
//                } else {
//                    date = task.nextTimeAfter(date);
//                    Set<Task> set = new HashSet<>();
//                    if (map.containsKey(date)) {
//                        set = map.get(date);
//                    }
//                    set.add(task);
//                    map.put((Date) date.clone(), set);
//                }

            }

//            System.out.println(map);
        }
        for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
            System.out.println(sdf.format(pair.getKey()));
            for (Task task : pair.getValue()) {
                System.out.println(task.getTitle());
            }

        }
        return map;
    }
}

