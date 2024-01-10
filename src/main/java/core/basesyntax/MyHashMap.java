package core.basesyntax;

import java.util.LinkedList;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int MULTIPLAYER_CAPACITY = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private LinkedList<Node<K, V>>[] table;
    private int size;

    public MyHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getHashIndex(key);
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }
        for (Node<K, V> node : table[index]) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
        }
        table[index].add(new Node<>(key, value));
        size++;
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getHashIndex(key);
        LinkedList<Node<K, V>> bucket = table[index];
        if (bucket != null) {
            for (Node<K, V> entry : bucket) {
                if (Objects.equals(entry.key, key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashIndex(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = key.hashCode() % table.length;
            if (index < 0) {
                index *= -1;
            }
        }
        return index;
    }

    private void resize() {
        int newCapacity = table.length * MULTIPLAYER_CAPACITY;
        LinkedList<Node<K, V>>[] newTable = new LinkedList[newCapacity];

        for (LinkedList<Node<K, V>> bucket : table) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newIndex = Math.abs(node.key.hashCode() % newCapacity);
                    LinkedList<Node<K, V>> newBucket = newTable[newIndex];
                    if (newBucket == null) {
                        newBucket = new LinkedList<>();
                        newTable[newIndex] = newBucket;
                    }
                    newBucket.add(node);
                }
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
