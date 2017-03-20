package com.darth.milash.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.util.Iterator;


/**
 * Created by darthMilash on 30.01.2017.
 */
public class TaskIO {

    static Logger logger = LoggerFactory.getLogger(TaskIO.class);
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
            logger.info("Binary writing task SUCCESSFUL");
        } catch (IOException e) {
            logger.error("Binary writing task in stream ERROR");
        }
    }

    /**
     * Binary reading task from stream in tasklist
     * @param tasks
     * @param in
     */
    public static void read(TaskList tasks, InputStream in) {
        try (ObjectInputStream in2 = new ObjectInputStream(in)) {
            int num = in2.readInt();
            for (int i=0; i<num; i++) {
                Task task = null;
                try {
                    task = (Task) in2.readObject();
                } catch (ClassNotFoundException e) {
                    logger.error("Class not found");
                }
                tasks.add(task);
                logger.info("Binary reading task SUCCESSFUL");
            }
        } catch (IOException e) {
            logger.error("Binary reading task from stream ERROR");
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
            logger.error("Binary writing task in file ERROR");
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
            logger.error("Binary reading task from file ERROR");
        }
    }

    /**
     * Text writing task in stream from task
     * @param tasks
     * @param out
     */
    public static void write(TaskList tasks, Writer out){
        try (BufferedWriter writer = new BufferedWriter(out)) {
            Iterator iterator=tasks.iterator();
            while(iterator.hasNext()) {
                Task task = (Task) iterator.next();
                writer.write(task.toString());
                if (iterator.hasNext()) {
                    writer.write(";");
                }
                else
                    writer.write(".");
                writer.newLine();
            }
            logger.info("Text writing task SUCCESSFUL");
        } catch (IOException e) {
            logger.error("Text writing task in stream ERROR");
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
                tasks.add(Spliter.splitString(str));
            }
            logger.info("Text reading task SUCCESSFUL");
        }catch(IOException e){
            logger.error("Text reading task from stream ERROR");
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
            logger.error("Text writing task in file ERROR");
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
            logger.error("Text reading task from file ERROR");
        }
    }
}
