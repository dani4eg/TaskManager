package model;

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
    Date date = null;
    SimpleDateFormat sdate = new SimpleDateFormat("[YYYY-MM-dd hh:mm:ss.SSS]", Locale.ENGLISH);

    Logger logger = LoggerFactory.getLogger(Task.class);
    /**
     * Constructor for task without interval
     * @param title - name of task
     * @param time - date of task
     * @throws IllegalArgumentException
     */
    public Task(String title, int time) throws IllegalArgumentException {
        if (time < 0 ) {
            logger.error("The time can not be negative");
            throw new IllegalArgumentException ();
        }
        this.title = title;
        date = new Date(time*1000);
        this.start = date;
        this.end = date;
    }

    public Task(String title, Date time) throws IllegalArgumentException {
        this.title = title;
        this.start = time;
        this.end = time;
        logger.info("Task \"" + title + "\" created. Start date: " +
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
        if (start < 0 || end <0 || interval <0)
        {
            logger.error("The time or interval of " + title + " can not be negative");
            throw new IllegalArgumentException();
        }
        else if (start > end) {
            logger.error("The end date of " + title + " must not be earlier than start date");
            throw new IllegalArgumentException();
        }
        this.title = title;
        this.start = new Date(start*1000);
        this.end = new Date(end*1000);
        this.interval = interval*1000;
    }
    public Task(String title, Date start, Date end, int interval) throws IllegalArgumentException {
        if (interval <0) {
            logger.error("The time or interval of " + this.title + " can not be negative");
            throw new IllegalArgumentException();
        }
        else if (start.after(end)) {
            logger.error("The end date of " + this.title + " must not be earlier than start date");
            throw new IllegalArgumentException();
        }
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval*1000;
        logger.info("Task \"" + title + "\" created. Start date: " +
                sdate.format(start) + " End date: " +
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

    /**
     * метод установлпения названия задачи
     * @param title название, которое надо поменять
     */
    public void setTitle(String title) {
        logger.info("The \"" + this.title + "\" changed for \"" + title + "\"");
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
        logger.info("The \"" + this.title + "\" changed the status from: " + this.active + " to " + active);
        this.active = active;
    }

    /**
     * метод возвращает время не повторяюшейся задачи
     * @return start если задача не повторяется - возвращает start - при том, что start = time (см конструктор)
     * @return start если задача повторяется, при этом start = start (см конструктор)
     */
    public Date getTime() {
        return this.start;
    }

    /**
     * метод изменяет повторяющейся задачу на не повторяющейся
     * @param time = start и = end, если задача не повторяется, то start = end
     * interval = 0 т.к. задача не повторяется
     */
    public void setTime(int time) throws IllegalArgumentException {
        if (time < 0) {
            logger.error("The time can not be negative");
            throw new IllegalArgumentException();
        }
        date = new Date(time*1000);
        this.start = date;
        this.end = date;
        this.interval = 0;

    }

    public void setTime(Date time) throws IllegalArgumentException {
        logger.info("The \"" + this.title + "\" changed the start date from: " +
                sdate.format(this.start) + " to: " +
                sdate.format(time) + ". Not repeated");
        this.start = time;
        this.end = time;
        this.interval = 0;
    }

    /**
     * метод возвращает время начала повторяюшейся задачи
     * @return start если задача повторяется, при этом start = start (см конструктор)
     * @return start если задача не повторяется - возвращает start - при том, что start = time (см конструктор)
     */
    public Date getStartTime() {
        return this.start;
    }

    /**
     * метод возвращает время окончания повторяюшейся задачи
     * @return end если задача повторяется, при этом end = end (см конструктор)
     * @return end если задача не повторяется - возвращает end - при том, что end = time (см конструктор)
     */
    public Date getEndTime() {
        return end;
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
            logger.error("The time or interval of \"" + this.title + "\" can not be negative");
            throw new IllegalArgumentException();
        }
        else if (start > end) {
            logger.error("The end date of \"" + this.title + "\" must not be earlier than start date");
            throw new IllegalArgumentException();
        }
        date = new Date(start*1000);
        this.end = new Date(end*1000);
        logger.info("The \"" + this.title + "\" changed the start date from: " +
                sdate.format(this.start) + " to: " +
                sdate.format(date) + ". End date changed to: " +
                sdate.format(this.end) + ". Repeated");
        this.start = date;
        this.interval = interval;
    }

    public void setTime(Date start, Date end, int interval) throws IllegalArgumentException {
        if (interval <0) {
            logger.error("The time or interval of \"" + this.title + "\" can not be negative");
            throw new IllegalArgumentException ("The time or interval can not be negative");
        }
        else if (start.after(end)) {
            logger.error("The end date of \"" + this.title + "\" must not be earlier than start date");
            throw new IllegalArgumentException ("The end date must not be earlier than start date");
        }
        this.end = end;
        logger.info("The \"" + this.title + "\" changed the start date from: " +
                sdate.format(this.start) + " to: " +
                sdate.format(start) + ". End date changed to: " +
                sdate.format(this.end) + ". Repeated");
        this.start = start;
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
                    return this.start;
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

    public String reInterval(int i) {
        String txt="";
        txt = Interval.reInterval(i);
        return txt;
    }
}

