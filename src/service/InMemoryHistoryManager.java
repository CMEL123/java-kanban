package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class InMemoryHistoryManager  implements HistoryManager {
    private static class Node {
        Node prev;
        Task item;
        Node next;

        Node(Node prev, Task el, Node next) {
            this.prev = prev;
            this.item = el;
            this.next = next;
        }
    }

    HashMap<Integer, Node> history = new LinkedHashMap<>();
    Node head;
    Node tail;

    @Override
    public void add(Task task) {
        Node node = history.get(task.getIdTask());
        removeNode(node);
        linkLast(task);
        history.put(task.getIdTask(), tail);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> list = new ArrayList<>();
        Node current = head;
        while (current != null) {
            list.add(current.item);
            current = current.next;
        }
        return list;
    }



    @Override
    public String toString() {
        String retStr = "";
        ArrayList<Task> list = this.getHistory();
        for (int i = 0; i < list.size(); i++) {
            retStr += list.toString();
        }
        return retStr;
    }

    public void remove(int id) {
        Node node = history.get(id);
        removeNode(node);
        history.remove(id);
    }

    private void linkLast(Task task) {
        final Node l = tail;
        final Node newNode = new Node(l, task, null);
        if (l == null) {
            head = newNode;
        } else {
            l.next = newNode;
        }
        tail = newNode;
    }

    private void removeNode(Node node) {
        if (node == null) return;

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        history.remove(node.item.getIdTask());
    }

}
