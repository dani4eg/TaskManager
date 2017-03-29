package com.darth.milash.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * Created by darthMilash on 30.01.2017.
 */
public class Tasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tasks.class);
    /**
     * Returns a list of tasks that will be performed in a given time interval
     * @param start the task must be executed no earlier than the specified time
     * @param end the task must be executed no later than the specified time
     * @return list with tasks
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
        LOGGER.info("Add in calendar list succes");
        return list;
    }

    /**
     * Creates a map collection with tasks
     * @param tasks the task
     * @param start start date
     * @param end end date
     * @return map collection with tasks in a given time interval
     * @throws IllegalArgumentException
     */
    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end)  throws IllegalArgumentException{
        if(end.before(start)) {
            LOGGER.error("The end date must not be earlier than start date");
            throw new IllegalArgumentException();
        }
        SortedMap<Date, Set<Task>> map = new TreeMap<>();
        Iterable<Task> tasksList = incoming(tasks, start, end);
        Iterator iterator = tasksList.iterator();
        while(iterator.hasNext()) {
            Task task = (Task)iterator.next();
            Date date = start;
            while((date.before(end) || date.equals(end))) {
                if (task.nextTimeAfter(date).getTime() == -1) {
                    break;
                }
                else {
                    Set<Task> set = new HashSet<>();
                    date = task.nextTimeAfter(date);
                    if (map.containsKey(date)) {
                        set = map.get(date);
                    }
                    set.add(task);
                    map.put((Date) date.clone(), set);
                    date.setTime(date.getTime() + task.getRepeatInterval());
                }
            }
        }
        return map;
    }
}

