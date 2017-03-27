package com.darth.milash.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Created by darthMilash on 30.01.2017.
 */
public class LinkedTaskList extends TaskList implements Cloneable{

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkedTaskList.class);
    /**
     * Ссылка на первый элемент списка
     */
    private Link first;

    /**
     * Класс Link создает элемент списка
     * task наши данные в виде задач
     * next - ссылка на следующий элемент списка
     */

    public static class Link {
        final private Task task;
        private Link next;

        public Task getTask() {
            return task;
        }
        public Link getNext() {
            return next;
        }
        public void setNext(Link next) {
            this.next = next;
        }
        public Link(Task task) {
            this.task = task;
        }
    }

    /**
     * Метод добавления задачи в список
     * @param task задача, которую мы добавляем
     * newLink создаем новый элемент
     * newLink --> старое значение first
     * first --> newLink
     * И добавляем в начало
     * Увеличиваем size на единицу
     * @throws NullPointerException исключение при вводе пустого значения
     */
    @Override
    public void add(Task task) throws NullPointerException {
        Link newLink = new Link(task);
        newLink.next = first;
        first = newLink;
        size++;
        LOGGER.info("The \"" + task.getTitle() + "\" added to LinkedList");
    }

    /**
     * Метод возвращает задачу которая находится на указанном месте в списке
     * @param index место в списке
     * @return возвращает задачу
     * @throws NullPointerException исключение если индекс больше чем размер списка
     */
    @Override
    public Task getTask(int index) throws NullPointerException {
        if (index>=size()) {
            LOGGER.error("The element not found");
        }
        Link current = first;
        for (int i = 0; i < index; i++) {
            current=current.next;
        }
        return current.task;
    }

    /**
     * Метод удаляет задачу и возвращает true - если задача была в списке
     * Если в списке было более одной задачи - удаляет одну
     * Уменьшает размер списка на 1 после удаления
     * @param task ищем задачу, которую надо удалить
     * @return возвращает true или falls - если задача не найдена в списке
     * @throws NullPointerException при удалении пустого элемента
     */
    @Override
    public boolean remove (Task task)throws NullPointerException {
        if (task == null ) {
            LOGGER.error("Deleted empty element in LinkedList");
        }
        Link current = first;
        Link prev = first;
        while (current.getTask()!=task) {
            if (current.getNext() == null) {
                LOGGER.info("The element not found in LinkedList");
                return false;
            }
            else {
                prev = current;
                current = current.getNext();
            }
        }
        if (current.equals(first)) {
            first = first.getNext();
        }
        else prev.setNext(current.getNext());
        LOGGER.info("The \"" + task.getTitle() + "\" deleted from LinkedList");
        size--;
        return true;
    }

    /**
     * Метод возвращает список задач, которые будут выполнены в данном промежутке времени
     * Создаем новый лист
     * @param from задача должна быть выполнена не раньше заданного времени
     * @param to задача должна быть выполнена не позже заданного времени
     * если время следующего выполнения задачи относительно заданного времени from выполняется не раньше from и не позже to
     * задача добавляется в список
     * @return список с задачами
     * @throws IllegalArgumentException исключение, если время или интервал отрицательное
     */


    /**
     * Создание итератора
     * @return итератор с переопределенными методами
     */
    public Iterator iterator() {
        return new Iterator() {
            private Link current = first;
            private Link prev = null;
            private Link prev2 = null;

            /**
             * Проверка на наличие следующего элемента
             * Если текущий элемент не пустой
             * @return true
             */
            @Override
            public boolean hasNext() {
                return current != null;
            }

            /**
             * Метод перехода итератора на след элемент
             * @throws NoSuchElementException если первый элемент равен null
             * @return task
             */
            @Override
            public Task next() {
                if (first == null) {
                    throw new NoSuchElementException();
                }
                prev2 = prev;
                prev = current;

                current = current.getNext();
                return prev.getTask();
            }

            /**
             * Метод удаления элемента
             * @throws IllegalStateException, если next() не выполнится хоть раз
             * Удаляем элемент
             * Уменьшаем размер на 1
             */
            @Override
            public void remove() {
                if (prev == null) {
                    throw new IllegalStateException();
                } else if (prev.getTask().getTitle().equals(first.getTask().getTitle())) { //first element
                    first=current;
                    first.setNext(current.getNext());
                } else if(current==null){
                    prev.setNext(null);
                } else {
                    prev2.setNext(prev.getNext());
                    prev.setNext(current.getNext());
                }
                size--;
            }
        };

    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof LinkedTaskList)) return false;
        LinkedTaskList that = (LinkedTaskList) object;
        if (this.size() == that.size()) {
            Iterator list1 = this.iterator();
            Iterator list2 = that.iterator();
            while (list1.hasNext()) {
                Object object1 = list1.next();
                Object object2 = list2.next();
                if (!(object1 == null ? object2==null : object1.equals(object2))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 0;
        Iterator iterator=this.iterator();
        while(iterator.hasNext()) {
            result = 31 * result + iterator.next().hashCode();
        }
        return result;
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
                text += " at " + date.format(getTask(i).getStartTime());
            }
            text +=  (getTask(i).isActive() ? " inactive" : "");
            if (i==size-1) text+=".";
            else text+=";";
            text += "\n";
        }
        return text;
    }

    @Override
    public LinkedTaskList clone() {
        LinkedTaskList clone = null;
        try {
            clone = (LinkedTaskList) super.clone();
        } catch (CloneNotSupportedException e) {
            LOGGER.error("CloneNotSupportedException");
        }
        return clone;
    }


}
