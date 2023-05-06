
public class Main 
{
	private int mSize = 0;
	private static short mSerialNum = 0; //使用累加的序列号
	private static lockFreeQueue<Short> mQueue = new lockFreeQueue<Short>();
	private static final int MAXSIZE = 20;	//最大容量20

	//producer
	static class Producer implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while( mQueue.size() < MAXSIZE )
			{
				Global.empty.Wait();
				//临界区
				mQueue.enqueue(mSerialNum);
				System.out.println("生产者在缓冲区生产了物品:"+mSerialNum+";size="+mQueue.size());
				mSerialNum++;
				try{
					Thread.sleep(10);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				Global.full.Signal();				
			}
		}
	}

	//consumer 
	static class Consumer implements Runnable{
		public void run(){
			while( mQueue.size() < MAXSIZE )
			{
				Global.full.Wait();
				//临界区	
				short temp = 0;
				if( mQueue.size() > 0 )
				{
					//System.out.println("消费前剩余:"+mQueue.size());
					try{
						temp = mQueue.dequeue();
						System.out.println("消费者在缓冲区消费了物品:" + temp+"，剩余:"+mQueue.size());
					}catch(Exception e)
					{
						System.out.println("Consumer exception"+mQueue.size());
						e.printStackTrace();						
					}
				}
				else;

				try{
					Thread.sleep(10);
				}catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				Global.empty.Signal();
			}
			System.out.print("consumer alive!");

		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hello testMain!");

		Producer p = new Producer();
		Consumer c = new Consumer();

		Thread pp = new Thread(p);
		Thread cp = new Thread(c);

		pp.start();
		cp.start();
	}


}
