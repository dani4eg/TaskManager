package com.darth.milash.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Created by darthMilash on 30.01.2017.
 */
public class ArrayTaskList extends TaskList implements Cloneable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayTaskList.class);
    private Task[] tasks;

    public ArrayTaskList(int size) {
        tasks = new Task[size];
    }

    public ArrayTaskList() {
        tasks  =new Task[10];
    }

    /**
     * Add tasks in array
     * @param task the task
     */
    public void add(Task task) throws NullPointerException {
        if (size >= tasks.length) {
            Task[] tArr = new Task[(size*3)/2 +1];
            System.arraycopy(tasks, 0, tArr, 0, size);
            tasks = tArr;
        }
        LOGGER.info("The \"{}\" added to ArrayList", task.getTitle());
        tasks[size] = task;
        size++;
    }

    /**
     * Return the task by index
     * @param index the index of task
     * @return task
     */
    public Task getTask(int index) {
        return tasks[index];
    }

    /**
     * Delete the task
     * @param task the task that is deleted
     * @return true or falls - if the task is not found in the list
     */
    public boolean remove (Task task) throws NullPointerException
    {
        for (int i = 0; i < size; i++) {
            if (task.equals(tasks[i])) {
                LOGGER.info("The \"{}\" deleted from ArrayList", task.getTitle());
                System.arraycopy(tasks, i+1, tasks, i, size-1-i);
                size--;
                return true;
            }
        }
        LOGGER.info("The element not found in ArrayList");
        return false;
    }

    /**
     * Iterator
     * @return iterator
     */
    public Iterator iterator() {
        return new Iterator() {
            private Task[] list = tasks;
            private int count;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public Task next() {
                if (list[0] == null) {
                    throw new NoSuchElementException();
                }
                Task task;
                task = list[count];
                count++;
                return task;
            }

            @Override
            public void remove() {
                if (count == 0) {
                    throw new IllegalStateException();
                }
                else {
                    for (int i = count-1; i < size -1 ; i++) {
                        System.arraycopy(tasks, i+1, tasks, i, size-1-i);
                    }
                    count--;
                    size--;
                }
            }
        };
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ArrayTaskList that = (ArrayTaskList) object;
        return Arrays.equals(tasks, that.tasks);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tasks);
    }

    @Override
    public String toString() {
        String text = "";
        SimpleDateFormat date = new SimpleDateFormat("[YYYY-MM-dd HH:mm:ss.SSS]", Locale.ENGLISH);
        for (int i = 0; i < size; i++) {
            text += "\"" + getTask(i).getTitle() + "\"";
            if (getTask(i).isRepeated()) {
                text += " from " + date.format(getTask(i).getStartTime());
                text += " to " + date.format(getTask(i).getEndTime());
                text += " every [" + Interval.reInterval(getTask(i).getInterval()) + "]";
            }
            else {
                text += " at " + date.format(getTask(i).getTime());
            }
            text +=  (getTask(i).isActive() ? " inactive" : "");
            if (i==size-1) text+=".";
            else text+=";";
            text += "\n";
        }
        return text;
    }


    public String toString2() {
        String text = "";
        for (int i = 0; i < size; i++) {
            text += "\"" + getTask(i).getTitle() + "\"";
            if (i==size-1) text+=".";
            else text += ";\n";
        }
        return text;
    }

    public ArrayTaskList clone() {
        ArrayTaskList arrayTaskList = null;
        try {
            arrayTaskList = (ArrayTaskList) super.clone();
            arrayTaskList.tasks  = Arrays.copyOf(tasks, tasks.length);
        } catch (CloneNotSupportedException e) {
            LOGGER.error("CloneNotSupportedException");
        }
        return arrayTaskList;
    }


}