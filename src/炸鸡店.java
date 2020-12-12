import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

abstract class Drinks{
    protected String name;
    protected double cost;
    protected LocalDate manufacture_date;
    protected int keep;
    Drinks(String name,double cost,LocalDate date,int keep)
    {
        this.name=name;
        this.cost=cost;
        this.manufacture_date=date;
        this.keep=keep;
    }
    Drinks()
    {

    }
    public boolean is_overdue()
    {
        return manufacture_date.plusDays(keep).isBefore(LocalDate.now());
    }
    public abstract String toString();
}
class Beer extends Drinks
{
    private float degree;
    Beer(String name,double cost,LocalDate date,float degree)
    {
        this.name=name;
        this.cost=cost;
        this.manufacture_date=date;
        this.keep=30;
        this.degree=degree;
    }
    public String toString()
    {
        return ("啤酒名称："+name+"\n"+"酒精度数："+degree+"\n"+"饮料费用："+cost+"\n"+"生产日期："+manufacture_date+"\n"+"保质期："+keep+"天");
    }
}
class Juice extends Drinks
{
    Juice(String name,double cost,LocalDate date)
    {
        this.name=name;
        this.cost=cost;
        this.manufacture_date=date;
        this.keep=2;
    }
    public String toString()
    {
        return ("果汁名称："+name+"\n"+"饮料费用："+cost+"\n"+"生产日期："+manufacture_date+"\n保质期:"+keep+"天");
    }
}
class SetMeal
{
    private String name;
    private double price;
    private String chicken_name;
    private Drinks drink;
    SetMeal(String name,double price,String c_name,Drinks d)
    {
        this.name=name;
        this.price=price;
        this.chicken_name=c_name;
        this.drink=d;
    }
    public String toString()
    {
        return ("套餐名称："+name+"\n套餐价格："+price+"\n炸鸡名："+chicken_name+"\n"+drink.toString());
    }
    public Drinks getDrink()
    {
        return drink;
    }
    public double getPrice()
    {
        return price;
    }
}
interface FriedChickenRestaurant{
    void sealMeal(SetMeal meal); //出售套餐
    void buy(SetMeal meal); //批量进货
}
class IngredientSortOutException extends RuntimeException{//果汁或啤酒售完
    private final String message;
    public IngredientSortOutException(String s) {
        this.message = s;
    }
    public String getMessage()
    {
        return message;
    }
}
class OverdraftBalanceException extends RuntimeException{//进货费用超出拥有余额
    private String message;
    public OverdraftBalanceException(String s)
    {
        this.message=s;
    }
    public String getMessage()
    {
        return message;
    }
}
class West2FriedChickenRestaurant implements FriedChickenRestaurant{
    private double balance;//余额
    private List<Beer> beers_list;
    private List<Juice> juices_list;
    West2FriedChickenRestaurant(double balance)
    {
        this.balance=balance;
    }
    static {
        //System.out.println("静态代码块");
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();
        List<SetMeal> meals_list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = scanner.next();
            double price = scanner.nextDouble();
            String chicken_name = scanner.next();
            int type = scanner.nextInt();//1为啤酒，0为果汁
            double cost = scanner.nextDouble();
            int year = scanner.nextInt();
            int month = scanner.nextInt();
            int day = scanner.nextInt();
            LocalDate date = LocalDate.of(year, month, day);
            System.out.println(name+" "+price+" "+chicken_name+" "+type+" "+cost+" "+date);
            if (type==1) {
                //System.out.println("啤酒");
                String beer_name = scanner.next();
                float degree = scanner.nextFloat();
                Beer b = new Beer(beer_name, cost, date, degree);
                SetMeal t = new SetMeal(name, price, chicken_name, b);
                meals_list.add(t);
            }
            else {
                //System.out.println("果汁");
                String juice_name = scanner.next();
                Juice j = new Juice(juice_name, cost, date);
                SetMeal t = new SetMeal(name, price, chicken_name, j);
                meals_list.add(t);
            }
        }
    }
    public void setBeers_list(int count)
    {
        this.beers_list = new ArrayList<>();
        for(int i=0;i<count;i++)
        {
            Scanner scanner = new Scanner(System.in);
            String name = scanner.next();
            double cost = scanner.nextDouble();
            int year = scanner.nextInt();
            int month = scanner.nextInt();
            int day = scanner.nextInt();
            LocalDate date = LocalDate.of(year,month,day);
            float degree = scanner.nextFloat();
            Beer t = new Beer(name,cost,date,degree);
            beers_list.add(t);
        }
    }
    public void setJuices_list(int count)
    {
        this.juices_list = new ArrayList<>();
        for(int i=0;i<count;i++)
        {
            Scanner scanner = new Scanner(System.in);
            String name = scanner.next();
            double cost = scanner.nextDouble();
            int year = scanner.nextInt();
            int month = scanner.nextInt();
            int day = scanner.nextInt();
            LocalDate date = LocalDate.of(year,month,day);
            Juice t = new Juice(name,cost,date);
            juices_list.add(t);
        }
    }
    private void use(Beer beer)
    {
        int flag=0;
        for(int i=0;i<beers_list.size();i++)
        {
            Beer s = beers_list.get(i);
            if(s.is_overdue()) beers_list.remove(i);
        }
        for(int i=0;i<beers_list.size();i++)
        {
            Beer s = beers_list.get(i);
            if(s.name.equals(beer.name))
            {
                beers_list.remove(i);
                flag=1;
            }
        }
        if(flag==0)
        {
            throw new IngredientSortOutException("该种啤酒已售完");
        }
    }
    private void use(Juice juice)
    {
        int flag=0;
        for(int i=0;i<juices_list.size();i++)
        {
            Juice s = juices_list.get(i);
            if(s.is_overdue()) juices_list.remove(i);
            if(s.name.equals(juice.name))
            {
                juices_list.remove(i);
                flag=1;
            }
        }
        if(flag==0)
        {
            throw new IngredientSortOutException("该种果汁已售完");
        }
    }
    public void sealMeal(SetMeal meal) throws IngredientSortOutException
    {
        System.out.println("点单：");
        System.out.println(meal.toString());
        if(meal.getDrink() instanceof Beer)
        {
            this.use((Beer) meal.getDrink());
        }
        else
        {
            this.use((Juice) meal.getDrink());
        }
    }
    public void buy(SetMeal meal) throws OverdraftBalanceException
    {
        if(balance< meal.getPrice())
        {
            throw new OverdraftBalanceException("余额不足，还差"+(meal.getPrice()-balance)+"元");
        }
        else {
            balance=balance- meal.getPrice();
            if(meal.getDrink() instanceof Beer)
            {
                beers_list.add((Beer) meal.getDrink());
            }
            else
            {
                juices_list.add((Juice) meal.getDrink());
            }
        }

    }
}
public class 炸鸡店 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        double balance = scanner.nextDouble();
        int year = scanner.nextInt();
        int month = scanner.nextInt();
        int day = scanner.nextInt();
        LocalDate localDate1 = LocalDate.of(year,month,day);
        double cost = scanner.nextDouble();
        float degree = scanner.nextFloat();
        Drinks drink1 = new Beer("啤酒1",cost,localDate1,degree);
        System.out.println(drink1.toString());
        if(drink1.is_overdue()) System.out.println("已过期");
        else System.out.println("未过期");
        Drinks drink2 = new Juice("果汁1",cost,localDate1);
        System.out.println(drink2.toString());
        if(drink2.is_overdue()) System.out.println("已过期");
        else System.out.println("未过期");
        double price = scanner.nextDouble();
        SetMeal meal = new SetMeal("套餐一",price,"炸鸡",drink1);
        System.out.println(meal.toString());
        West2FriedChickenRestaurant west_2 = new West2FriedChickenRestaurant(balance);
        int count = scanner.nextInt();
        west_2.setBeers_list(count);
        try{
            west_2.buy(meal);
        }catch(OverdraftBalanceException e){
            System.out.println(e.getMessage());
        }
        try {
            west_2.sealMeal(meal);
        }catch (IngredientSortOutException e){
            System.out.println(e.getMessage());
        }
    }
}
