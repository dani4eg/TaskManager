package model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by darthMilash on 30.01.2017.
 */
public class TaskIO {

    /**
     * Binary writing task in stream from task
     * @param tasks
     * @param out
     */
    public static void write(TaskList tasks, OutputStream out) {
        try (ObjectOutputStream out2 = new ObjectOutputStream(out)) {
            out2.writeInt(tasks.size());
            for (Task task : tasks) {
                out2.writeObject(task);
            }
        } catch (IOException e) {
            System.out.println("Binary writing task in stream ERROR");
        }
    }

    /**
     * Binary reading task from stream in tasklist
     * @param tasks
     * @param in
     */
    public static void read(TaskList tasks, InputStream in) {
        try (ObjectInputStream in2 = new ObjectInputStream(in)) {
            int n = in2.readInt();
            for (int i=0; i<n; i++) {
                Task task = null;
                try {
                    task = (Task) in2.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                tasks.add(task);
            }
        } catch (IOException e) {
            System.out.println("Binary reading task from stream ERROR");
        }
    }

    /**
     * Binary writing task in file from task
     * @param tasks
     * @param file
     */
    public static void writeBinary(TaskList tasks, File file) {
        try (ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream(file))) {
            write(tasks, out2);
        } catch (IOException e) {
            System.out.println("Binary writing task in file ERROR");
        }
    }

    /**
     * Binary reading task from file in tasklist
     * @param tasks
     * @param file
     */
    public static void readBinary(TaskList tasks, File file) {
        try (ObjectInputStream in2 = new ObjectInputStream(new FileInputStream(file))) {
            read(tasks, in2);
        } catch (IOException e) {
            System.out.println("Binary reading task from file ERROR");
        }
    }

    /**
     * Text writing task in stream from task
     * @param tasks
     * @param out
     */
    public static void write(TaskList tasks, Writer out){
        try (BufferedWriter writer = new BufferedWriter(out)) {
            Iterator it=tasks.iterator();
            while(it.hasNext()) {
                Task task = (Task) it.next();
                writer.write(task.toString());
                if (it.hasNext()) {
                    writer.write(";");
                }
                else
                    writer.write(".");
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Text writing task in stream ERROR");
        }
    }

    /**
     * Text reading task from stream in tasklist
     * @param tasks
     * @param in
     * @throws ParseException
     */
    public static void read(TaskList tasks, Reader in) throws ParseException {
        try(BufferedReader reader = new BufferedReader(in)) {
            String str;
            while((str=reader.readLine())!=null){
                tasks.add(splitString(str));
            }
        }catch(IOException e){
            System.out.println("Text reading task from stream ERROR");
        }

    }

    /**
     * Text writing task in file from task
     * @param tasks
     * @param file
     */
    public static void  writeText(TaskList tasks, File file) {
        try (Writer writer = new FileWriter(file)) {
            write(tasks, writer);
        } catch (IOException e) {
            System.out.println("Text writing task in file ERROR");
        }
    }

    /**
     * Text reading task from file in tasklist
     * @param tasks
     * @param file
     * @throws ParseException
     */
    public static void readText(TaskList tasks, File file) throws ParseException {
        try (Reader reader = new FileReader(file)) {
            read(tasks, reader);
        }catch(IOException e){
            System.out.println("Text reading task from file ERROR");
        }
    }

    public static Task splitString(String str) throws ParseException {
        Task task;
        String title = "";
        Date dStart = null;
        Date dEnd = null;
        Date dTime = null;
        int interval=0;
        boolean active = false;
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");

        /**
         * Title
         */
        Pattern patTitle = Pattern.compile("\".+\"");
        Matcher mTitle = patTitle.matcher(str);
        if (mTitle.find()) {
            title = str.substring(mTitle.start()+1, mTitle.end()-1);;
        }

        /**
         * DateStart
         */
        String start = "";
        Pattern patStart = Pattern.compile("from.+to");
        Matcher mStart = patStart.matcher(str);
        if (mStart.find()) {
            start = str.substring(mStart.start()+6, mStart.end()-4);
            dStart=date.parse(start);
        }

        /**
         * DateEnd
         */
        String end = "";
        Pattern patEnd = Pattern.compile("to.+every");
        Matcher mEnd = patEnd.matcher(str);
        if (mEnd.find()) {
            end = str.substring(mEnd.start()+4, mEnd.end()-7);
            dEnd=date.parse(end);
        }

        /**
         * DateTime
         */
        String time = "";
        Pattern patTime = Pattern.compile("at.+]");
        Matcher mTime = patTime.matcher(str);
        if (mTime.find()) {
            time = str.substring(mTime.start()+4, mTime.end()-1);
            dTime = date.parse(time);
        }

        /**
         * Interval
         */
        String days = "";
        String hours = "";
        String minutes = "";
        String seconds = "";
        Pattern pat = Pattern.compile("every.+day");
        Matcher m = pat.matcher(str);
        if (m.find()) {
            days = str.substring(m.start()+7, m.end()-4);
            interval=Integer.parseInt(days) * 86400;
        }
        Pattern pat2 = Pattern.compile("hour");
        Matcher m2 = pat2.matcher(str);
        if (m2.find()) {
            hours = str.substring(m2.start()-3, m2.end()-5);
            try {
                interval+= Integer.parseInt(hours) * 3600;
            }
            catch (NumberFormatException e) {
                String cHours = str.substring(m2.start()-2, m2.end()-5);
                interval+= Integer.parseInt(cHours) * 3600;
            }
        }
        Pattern pat3 = Pattern.compile("minute");
        Matcher m3 = pat3.matcher(str);
        if (m3.find()) {
            minutes = str.substring(m3.start()-3, m3.end()-7);
            try {
                interval+= Integer.parseInt(minutes) * 60;
            }
            catch (NumberFormatException e) {
                String cMinutes = str.substring(m3.start()-2, m3.end()-7);
                interval+= Integer.parseInt(cMinutes) * 60;
            }
        }
        Pattern patSec = Pattern.compile("second");
        Matcher mSec = patSec.matcher(str);
        if (mSec.find()) {
            seconds = str.substring(mSec.start()-3, mSec.end()-7);
            try {
                interval+= Integer.parseInt(seconds);
            }
            catch (NumberFormatException e) {
                String cSeconds = str.substring(mSec.start()-2, mSec.end()-7);
                interval+= Integer.parseInt(cSeconds);
            }
        }

        /**
         * Active
         */
        Pattern patActive = Pattern.compile("inactive");
        Matcher mActive = patActive.matcher(str);
        if (mActive.find()) {
            active = true;
        }

        /**
         * Create task
         */
        if (dStart!=null && dEnd!=null) {
            task = new Task(title, dStart, dEnd, interval);
        }
        else task = new Task(title, dTime);
        if (active) {
            task.setActive(true);
        }
        return task;
    }
}
