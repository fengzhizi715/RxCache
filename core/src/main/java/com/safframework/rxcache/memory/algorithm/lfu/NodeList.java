package com.safframework.rxcache.memory.algorithm.lfu;

/**
 * Created by tony on 2018/10/22.
 */
public class NodeList {

    Node head;
    Node tail;
    int length;

    public NodeList() {
        head = null;
        tail = null;
        length = 0;
    }

    public void prepend(Node node) {

        if (head == null) {
            tail = node;
            node.next = null;
        } else {
            node.next = head;
            head.prev = node;
        }
        head = node;
        node.prev = null;
        length++;
    }

    public void append(Node node) {

        if (tail == null) {
            prepend(node);
        } else {
            tail.next = node;
            node.next = null;
            node.prev = tail;
            tail = node;
            length++;
        }
    }

    public void insertAfter(Node position, Node node) {

        if (position.equals(tail)) {
            append(node);
        } else {
            node.next = position.next;
            node.prev = position;
            position.next = node;
            node.next.prev = node;
            length++;
        }
    }

    public void remove(Node node) {

        if (node.equals(tail) && node.equals(head)) { /* single node in LinkedList */
            head = null;
            tail = null;
        } else if (node.equals(tail)) {
            tail = tail.prev;
            tail.next = null;
        } else if (node.equals(head)) {
            head = head.next;
            head.prev = null;
        } else {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        }
        node.next = null;
        node.prev = null;
        length--;
    }

    public void clear() {

        Node walk = head;
        while (walk != null) {
            Node temp = walk;
            walk = walk.next;
            remove(temp);
        }
    }
}
