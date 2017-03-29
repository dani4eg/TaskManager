package com.darth.milash.model;

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
        LOGGER.info("Task \"{}\" created. Start date: {}." +
                " Active: {}", this.title, sdate.format(time),this.active);
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
            LOGGER.error("The time or interval of {} can not be negative", title);
            throw new IllegalArgumentException();
        }
        else if (start > end) {
            LOGGER.error("The end date of {} must not be earlier than start date", title);
            throw new IllegalArgumentException();
        }
        this.title = title;
        this.start = new Date((long)start*1000);
        this.end = new Date((long)end*1000);
        this.interval = interval*1000;
    }
    public Task(String title, Date start, Date end, int interval) throws IllegalArgumentException {
        if (interval <0) {
            LOGGER.error("The time or interval of {} can not be negative", this.title);
            throw new IllegalArgumentException();
        }
        else if (start.after(end)) {
            LOGGER.error("The end date of {} must not be earlier than start date", this.title);
            throw new IllegalArgumentException();
        }
        this.title = title;
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        this.interval = interval*1000;
        LOGGER.info("Task \"{}\" created. Start date: {}" +
                " End date: {}" +
                ". Active: {}", title, sdate.format(start), sdate.format(end), this.active);
    }

    public int getInterval() {
        return interval;
    }

    public String getTitle(){
        return title;
    }

    /**
     * Changes title of task
     * @param title the title
     */
    public void setTitle(String title) {
        LOGGER.info("\"{}\" changed for \"{}\"", this.title, title);
        this.title = title;
    }

    /**
     * Task states
     * @return true - if task is active, false - if not active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Changes task states
     * @param active true - if task is active, false - if not active
     */
    public void setActive(boolean active) {
        LOGGER.info("\"{}\" changed the status from: {} to {}", this.title, this.active, active);
        this.active = active;
    }

    public Date getTime() {
        return new Date(this.start.getTime());
    }

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

    /**
     * Changes task time to non repeat
     * @param time the time of task complete
     */
    public void setTime(Date time) throws IllegalArgumentException {
        LOGGER.info("\"{}\" changed the start date from: {} to: {}. " +
                "Not repeated", this.title, sdate.format(this.start), sdate.format(time));
        this.start = new Date(time.getTime());
        this.end = new Date(time.getTime());
        this.interval = 0;
    }

    public Date getStartTime() {
        return new Date(start.getTime());
    }

    public Date getEndTime() {
        return new Date(end.getTime());
    }

    public int getRepeatInterval() {
        return interval;
    }


    public void setTime(int start, int end, int interval) throws IllegalArgumentException {
        if (interval <0) {
            LOGGER.error("The time or interval of \"{}\" can not be negative", this.title);
            throw new IllegalArgumentException();
        }
        else if (start > end) {
            LOGGER.error("The end date of \"{}\" must not be earlier than start date", this.title);
            throw new IllegalArgumentException();
        }
        date = new Date((long)start*1000);
        this.end = new Date((long)end*1000);
        LOGGER.info("\"{}\" changed the start date from: {} to: {}" +
                ". End date changed to: {}. " +
                "Repeated", this.title, sdate.format(this.start), sdate.format(date), sdate.format(this.end));
        this.start = date;
        this.interval = interval;
    }

    /**
     * Changes task time to repeat
     * @param start the start of task
     * @param end the end of task
     * @param interval the time interval to repeat
     */
    public void setTime(Date start, Date end, int interval) throws IllegalArgumentException {
        if (interval <0) {
            LOGGER.error("The time or interval of \"{}\" can not be negative", this.title);
            throw new IllegalArgumentException();
        }
        else if (start.after(end)) {
            LOGGER.error("The end date of \"{}\" must not be earlier than start date", this.title);
            throw new IllegalArgumentException();
        }
        this.end = new Date(end.getTime());
        LOGGER.info("\"{}\" changed the start date from: {} to: {}" +
                ". End date changed to: {}. " +
                "Repeated", this.title, sdate.format(this.start), sdate.format(date), sdate.format(this.end));
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

    public boolean isRepeated() {
        return this.interval>0;
    }

    /**
     * Time of completion of tasks after the specified time
     * @param current the specified time
     * @return -1 if task is not active
     * @return start + (interval*i) if task is active and current before start and the sum is not more end time
     * @return start if task is active and current < start
     * @return -1 start before current
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

