import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/*
 * hss 20230505
 * 
 */
public class lockFreeQueue<T> 
{
	private final AtomicReference<Node<T>> head, tail;
	private final AtomicInteger size;

	public lockFreeQueue() {
		head = new AtomicReference<>(null);
		tail = new AtomicReference<>(null);
		size = new AtomicInteger(0);
	}

	public void enqueue(T value) {
		Node<T> node = new Node<>(value);
		Node<T> prevTail = tail.getAndSet(node);
		if (prevTail != null) {
			prevTail.next.set(node);
		} else {
			if(node == null)
				System.out.println("enqueue error null!");
			else;
			head.set(node);
		}
		size.incrementAndGet();
	}

	public T dequeue() {
		Node<T> currentHead, currentTail;
		do {			
			currentHead = head.get();
			if(currentHead == null)
			{
				System.out.println("dequeue errror currentHead null!");
								
			}
			
			if (currentHead == null) {
				return null;
			}
			currentTail = tail.get();
		} while(!head.compareAndSet(currentHead, currentHead.next.get()));
		
		T value = currentHead.value;
		
		if (currentHead == currentTail) {
			tail.compareAndSet(currentTail, null);
		}
		size.decrementAndGet();
		
		return value;
	}

	public int size() {
		return size.get();
	}

	private static class Node<T> {
		private final T value;
		private final AtomicReference<Node<T>> next;

		private Node(T value) {
			this.value = value;
			this.next = new AtomicReference<>(null);
		}
	}

}
