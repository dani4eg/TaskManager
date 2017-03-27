package com.darth.milash.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by darthMilash on 30.01.2017.
 */
public class Task implements Cloneable, Serializable {

    private String title;
    private Date start;
    private Date end;
    private int interval;
    private boolean active;
    private Date date = null;
    private final SimpleDateFormat sdate = new SimpleDateFormat("[YYYY-MM-dd HH:mm:ss.SSS]", Locale.ENGLISH);

    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);
    /**
     * Constructor for task without interval
     * @param title - name of task
     * @param time - date of task
     * @throws IllegalArgumentException
     */
    public Task(String title, int time) throws IllegalArgumentException {
        if (time < 0 ) {
            LOGGER.error("The time can not be negative");
            throw new IllegalArgumentException ();
        }
        this.title = title;
        date = new Date((long)time*1000);
        this.start = date;
        this.end = date;
    }


    public Task(String title, Date time) throws IllegalArgumentException {
        this.title = title;
        this.start = new Date(time.getTime());
        this.end = new Date(time.getTime());
        LOGGER.info("Task \"" + this.title + "\" created. Start date: " +
                sdate.format(time) + ". Active: " + this.active);
    }

    /**
     * @param title - name of task
     * @param start - date of task
     * @param end - end date of task
     * @param interval -
     * @throws IllegalArgumentException
     */
    public Task(String title, int start, int end, int interval) throws IllegalArgumentException {
        if (start < 0 || end <0 || interval <0) {
            LOGGER.error("The time or interval of " + title + " can not be negative");
            throw new IllegalArgumentException();
        }
        else if (start > end) {
            LOGGER.error("The end date of " + title + " must not be earlier than start date");
            throw new IllegalArgumentException();
        }
        this.title = title;
        this.start = new Date((long)start*1000);
        this.end = new Date((long)end*1000);
        this.interval = interval*1000;
    }
    public Task(String title, Date start, Date end, int interval) throws IllegalArgumentException {
        if (interval <0) {
            LOGGER.error("The time or interval of " + this.title + " can not be negative");
            throw new IllegalArgumentException();
        }
        else if (start.after(end)) {
            LOGGER.error("The end date of " + this.title + " must not be earlier than start date");
            throw new IllegalArgumentException();
        }
        this.title = title;
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        this.interval = interval*1000;
        LOGGER.info("Task \"" + title + "\" created. Start date: " +
                sdate.format(start) + " End d9ate: " +
                sdate.format(end) + ". Active: " + this.active);
    }

    public int getInterval() {
        return interval;
    }
    /**
     * метод считывания названия задачи
     * return title название задачи
     */
    public String getTitle(){
        return title;
    }

    public StringProperty getTitlePropetry() {
        return new SimpleStringProperty(title);
    }
    /**
     * метод установлпения названия задачи
     * @param title название, которое надо поменять
     */
    public void setTitle(String title) {
        LOGGER.info("\"" + this.title + "\" changed for \"" + title + "\"");
        this.title = title;
    }

    /**
     * метод считывания состояния задачи
     * @return active true - если задача активна, falls - если не активна
     */
    public boolean isActive() {
        return active;
    }

    /**
     * метод установлпения состояния задачи
     * @param active true - если задача активна, falls - если не активна
     */
    public void setActive(boolean active) {
        LOGGER.info("\"" + this.title + "\" changed the status from: " + this.active + " to " + active);
        this.active = active;
    }

    /**
     * метод возвращает время не повторяюшейся задачи
     * @return start если задача не повторяется - возвращает start - при том, что start = time (см конструктор)
     * @return start если задача повторяется, при этом start = start (см конструктор)
     */
    public Date getTime() {
        return new Date(this.start.getTime());
    }

    /**
     * метод изменяет повторяющейся задачу на не повторяющейся
     * @param time = start и = end, если задача не повторяется, то start = end
     * interval = 0 т.к. задача не повторяется
     */
    public void setTime(int time) throws IllegalArgumentException {
        if (time < 0) {
            LOGGER.error("The time can not be negative");
            throw new IllegalArgumentException();
        }
        date = new Date((long)time*1000);
        this.start = date;
        this.end = date;
        this.interval = 0;

    }

    public void setTime(Date time) throws IllegalArgumentException {
        LOGGER.info("\"" + this.title + "\" changed the start date from: " +
                sdate.format(this.start) + " to: " +
                sdate.format(time) + ". Not repeated");
        this.start = new Date(time.getTime());
        this.end = new Date(time.getTime());
        this.interval = 0;
    }

    /**
     * метод возвращает время начала повторяюшейся задачи
     * @return start если задача повторяется, при этом start = start (см конструктор)
     * @return start если задача не повторяется - возвращает start - при том, что start = time (см конструктор)
     */
    public Date getStartTime() {
        return new Date(start.getTime());
    }

    /**
     * метод возвращает время окончания повторяюшейся задачи
     * @return end если задача повторяется, при этом end = end (см конструктор)
     * @return end если задача не повторяется - возвращает end - при том, что end = time (см конструктор)
     */
    public Date getEndTime() {
        return new Date(end.getTime());
    }

    /**
     * метод возвращает интервал времени повторяюшейся задачи
     * @return interval если задача повторяется, interval = interval
     * @return interval если задача не повторяется, interval = 0 по умолчанию, возвращает 0
     */
    public int getRepeatInterval() {
        return interval;
    }

    /**
     * метод изменяет не повторяющейся задачу на повторяющейся
     * @param start = start
     * @param end = end
     * @param interval = interval
     */
    public void setTime(int start, int end, int interval) throws IllegalArgumentException {
        if (interval <0) {
            LOGGER.error("The time or interval of \"" + this.title + "\" can not be negative");
            throw new IllegalArgumentException();
        }
        else if (start > end) {
            LOGGER.error("The end date of \"" + this.title + "\" must not be earlier than start date");
            throw new IllegalArgumentException();
        }
        date = new Date((long)start*1000);
        this.end = new Date((long)end*1000);
        LOGGER.info("\"" + this.title + "\" changed the start date from: " +
                sdate.format(this.start) + " to: " +
                sdate.format(date) + ". End date changed to: " +
                sdate.format(this.end) + ". Repeated");
        this.start = date;
        this.interval = interval;
    }

    public void setTime(Date start, Date end, int interval) throws IllegalArgumentException {
        if (interval <0) {
            LOGGER.error("The time or interval of \"" + this.title + "\" can not be negative");
            throw new IllegalArgumentException();
        }
        else if (start.after(end)) {
            LOGGER.error("The end date of \"" + this.title + "\" must not be earlier than start date");
            throw new IllegalArgumentException();
        }
        this.end = new Date(end.getTime());
        LOGGER.info("\"" + this.title + "\" changed the start date from: " +
                sdate.format(this.start) + " to: " +
                sdate.format(start) + ". End date changed to: " +
                sdate.format(this.end) + ". Repeated");
        this.start = new Date(start.getTime());
        this.interval = interval;
    }

    public void setStart(Date start) {
        this.start = new Date(start.getTime());
    }

    public void setEnd(Date end) {
        this.end = new Date(end.getTime());
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * метод проверяет повторяется ли задача
     * @return true если interval>0
     * @return falls если interval<0
     */
    public boolean isRepeated() {
        return this.interval>0;
    }

    /**
     * метод возвращает время следующего выполнения задачи после указания время
     * @param current указанное время
     * @return -1 если задача не активная
     * @return start + (interval*i) если задача активна, и указанное время current >= start и в сумме с start не больше end
     * если задача не повторяется, то end = start, и условие end > start + current не выполняется
     * @return start если задача активна, и указанное время current < start
     * @return -1 если задача не выполняется в указанное время current или была выполненна
     */
    public Date nextTimeAfter(Date current) throws IllegalArgumentException {
        Date date = new Date(-1);
        if (isActive()) {
            if (isRepeated()) {
                for (long i = this.start.getTime(); i <= this.end.getTime(); i = i + this.interval) {
                    date.setTime(i);
                    if (date.after(current) || date.equals(current)) {
                        return date;
                    } else date.setTime(-1);
                }
            } else {
                if (this.start.after(current))
                    return new Date(this.start.getTime());
                else
                    return new Date(-1);
            }
        }
        return date;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Task)) return false;
        Task task = (Task) object;
        if (interval != task.interval) return false;
        if (isActive() != task.isActive()) return false;
        if (!getTitle().equals(task.getTitle())) return false;
        if (!start.equals(task.start)) return false;
        return end.equals(task.end);
    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + interval;
        result = 31 * result + (isActive() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        String text = "";
        text += "\"" + this.title + "\"";
        if (isRepeated()) {
            text += " from " + sdate.format(this.start);
            text += " to " + sdate.format(this.end);
            text += " every [" + Interval.reInterval(interval) + "]";
        }
        else {
            text += " at " + sdate.format(this.start);
        }
        text +=  (this.active ? " inactive" : "");
        return text;
    }

    public Task clone() throws CloneNotSupportedException {
        Task task;
        if (this.interval ==0) {
            task = new Task(this.getTitle(), (Date)this.start.clone());
        }
        else
            task = new Task(this.getTitle(), (Date)this.start.clone(), (Date)this.end.clone(), this.interval);
        return task;
    }

    public String reInterval(int interval) {
        String txt="";
        txt = Interval.reInterval(interval);
        return txt;
    }
}

