
//PV操作类
public class syn
{
	int count = 0;//信号量
	String name = null;
	syn(){	}
	syn(int inputCount,String inputName)
	{ 
		count = inputCount; 
		name = inputName;
	}
	
	public synchronized void Wait()
	{
		count--;
		if( count < 0 )
		{
			System.out.println("wait!"+name);
			try{
				this.wait();
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public synchronized void Signal()
	{
		count++;
		if( count <= 0 )
		{
			System.out.println("notify!"+name);
			this.notify();
		}

	}
}
