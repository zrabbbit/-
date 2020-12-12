import java.util.Scanner;

public class 多线程 {
    public static class ADD extends Thread{
        int num;
        int sum=0;
        int y;
        ADD(int l,int y)
        {
            num=l;
            this.y=y;
        }
        public void run()
        {
            for(int i=1;i<=10;i++)
            {
                int t=num+i;
                while(t>0)
                {
                    if(t%10==y)
                    {
                        sum=sum+num+i;
                        break;
                    }
                    else
                    {
                        t=t/10;
                    }
                }
            }
            System.out.println(getName()+" "+sum);
        }
    }
    public static void main(String[] args){
        int result=0;
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int y = scanner.nextInt(); //1~n中含有某个数字y的所有数字之和
        for(int i=0;i<n/10;i++)
        {
            ADD add = new ADD(i*10,y);
            add.start();
            try{
                add.join();
            }catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            result = result + add.sum;
        }
        if(n%10!=0)
        {
            for(int i=n/10*10+1;i<=n;i++)
            {
                int t=i;
                while(t>0)
                {
                    if(t%10==y)
                    {
                        result=result+i;
                        break;
                    }
                    else
                    {
                        t=t/10;
                    }
                }
            }
        }
        System.out.println("result:"+result);
    }


}
