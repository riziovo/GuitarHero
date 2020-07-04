package guitarsynth;

import java.util.Iterator;

public class ArrayRingBuffer<T> implements BoundedQueue<T>
{
    private int capacity;
    private int size;
    private int front, end;
    private boolean full;
    private T[] buffer;

    ArrayRingBuffer(int x)
    {
        this.capacity = x;
        this.buffer = (T[])new Object[this.capacity];
        this.front = 0;
        this.end = 0;
        this.size = 0;
    }

    @Override
    public void add(T x)
    {
        //if(this.isFull()) return;
        if(this.full) return;
        if(this.end == this.capacity) this.end = 0;

        this.buffer[this.end] = x;
        this.end = this.end + 1;
        if(this.end+1 == this.front) full = true;

        this.size+=1;
    }

    public void resetHeaders()
    {
        this.front = this.end = 0;
    }

    @Override
    public T remove()
    {
        if(this.isEmpty()) return null;

        this.full = false;
        this.front = this.front + 1;
        T item = this.buffer[this.front-1];
        if(this.front == this.capacity) this.front = 0;

        this.size-=1;
        return item;
    }

    @Override
    public T get()
    {
        if(this.isEmpty()) return null;

        return this.buffer[this.front];
    }

    @Override
    public int capacity()
    {
        return this.capacity;
    }

    @Override
    public int size()
    {
        return this.size;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new ArrayRingBufferIterator();
    }

    private class ArrayRingBufferIterator implements Iterator<T>
    {
        int pos;
        ArrayRingBufferIterator()
        {
            this.pos = front;
        }

        @Override
        public boolean hasNext()
        {
            if(isEmpty()) return false;
            return end+1 != front;
        }

        @Override
        public T next()
        {
            pos += 1;
            return buffer[pos-1];
        }
    }
}
