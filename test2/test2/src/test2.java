import java.util.*;
import java.lang.*;

public class test2 {
    public static void main(String[] args){
        //initialize the rocketA object
        rocket rocketA = new rocket(30000,75.2);

        //initialize the correction factors || (These will be initialized as rocket objects)
        rocket cFactA = new rocket(0,12.3);
        rocket cFactB = new rocket(15000,0.0);
        rocket cFactC = new rocket(-2000,5.0);
        rocket cFactD = new rocket(0,-14.5);

        rocket[] ivalue={rocketA,cFactA,cFactB,cFactC,cFactD};
        char[] express={'a','+','(','d','%','(','c','+','A','*','b',')',')','#'}; //a+(d%(c+A*b))
        parseExpression(express,ivalue);
    }//end of main

    /*created the method parseExpression to parse through the expression character array and return the value.
     * this was made an expression so that the while loop did not have to be repeated within the main method every time we wanted to evaluate an expression
     * the method will take a character array of an expression and an array of rocket objects*/
    public static void parseExpression(char[] express1, rocket[] values1){//method to calculate expressions represented as character arrays
        //creating a stack for the integer operands
        GenericManagerStacks<rocket> opnd=new GenericManagerStacks<rocket>();
        //creating a stack for the Operators
        GenericManagerStacks<Opertobj> oper=new GenericManagerStacks<Opertobj>();
        System.out.println("pushing Operator # with priority -100");
        Opertobj pnode1=new Opertobj('#',-100); //creating an operator objects and pushing it onto the stack
        oper.pushnode(pnode1);
        int oprior;
        rocket exvalue;//exvalue will hold the value that we want to return after calculation of expression is complete
        int i, num;
        rocket ivalu;
        char[] vart2={'A','a','b','c','d'}; //the variable array that corresponds to rocket[] ivalue
//        char [] vart={'A','B','C','D','E','F','0','1','2','3','4','5','6','7','8','9'}; //the variable array with both letters and numbers
        char [] opert={'%','*','+',')','(','#'};//this is the set of operators that will be evaluated in expressions
        // now create the evaluation priority for the symbols.  The higher the priority, the higher the number in the table
        int [] intvalp={3,2,1,99,-99,-100};//operator % has priority of 3, * has priority of 2, and + has priority of 1
        //WE MUST INITIALIZE the OPERATOR STACK so the first Operator can be pushed on.  I must put an end of operation on the operator stack.
        i=0;
        while(express1[i]!='#') {
            System.out.println("parsing: "+express1[i]);
            if(((express1[i]>='a')&&(express1[i]<='z'))||((express1[i]>='A')&&(express1[i]<='Z')))
            // Check to see if this character is a variable or an operator.
            {// we have a variable or a constant
                System.out.println("this is an operand: "+express1[i]);
                // find the character in the vart table that corresponds with the value
                ivalu=findval(express1[i],vart2,values1,4); //originally was ivalu=findval(express1[i],vart,values1,15);
                if(ivalu.thrust==-99)System.out.println("no value in table for"+express1[i]);
                // now that we have the value we need to place it on the operand stack
                System.out.println("we're pushing it on the operand stack: "+ivalu);
                opnd.pushnode(ivalu);
            }//end of variable stack
            else {//we are an operator
                System.out.println("this is an operator: "+express1[i]);
                if(express1[i]=='(') {//this is a left parenthesis, push it on the stack
                    // System.out.println("pushing on operator stack"+express[i]);
                    //Create node to push on stack
                    Opertobj pnodeo=new Opertobj(express1[i],-99);
                    oper.pushnode(pnodeo);
                }else if(express1[i]==')') {// this is a right parenthesis, we must begin to pop operands and pereators
                    //until we find the a left parenthesis (
                    while((oper.peeknode()).operator!='(') {//must pop and evaluate the stuff on operand and operator stack
                        popevalandpush(oper,opnd);
                    }
                    // now pop the ( node
                    oper.popnode();
                }//end of this is a right parenthesis
                else {//this is not either ( or ) is is another operator
                    oprior=findval2(express1[i],opert,intvalp,4);
                    System.out.println("peeking at top of stack: "+(oper.peeknode()).priority);
                    //**********oprior MUST BE STRICTLY GREATER THAN BEFORE WE CAN PUT IT ON THE STACK********
                    while(oprior<=(oper.peeknode()).priority)popevalandpush(oper,opnd);
                    //now push this operator on the stack.
                    System.out.println("pushing Operator "+express1[i]+", with priority: "+oprior);
                    Opertobj pnodeo=new Opertobj(express1[i],oprior);
                    oper.pushnode(pnodeo);
                }//this is the end of this is not () operator
            }//end of on operator stack
            i++;
        }//end of while express loop
        //we have found the # in the evaluation now we must evaluate the operator stack
        while((oper.peeknode()).operator!='#') {//we are finishing up operator stack
            popevalandpush(oper,opnd);
        }// end of finishing up operator stack
        //we're done, get value of opnd stack and print
        exvalue=opnd.popnode();
        System.out.println ("the value for this expression is: "+exvalue+"\n");//prints out the expression value
    }//end of parseexpression method

    //IntEval method will look at the operator and calculate the result of two operands dependent on the operator
    public static rocket IntEval(rocket obj1, char oper, rocket obj2){
        rocket result = new rocket(-99,-99);//a new rocket object to be returned after calculation
        switch(oper){
            case '%':
                //add value to direction
                //if else statement used to set thrust equal to rocket thrust
                if(obj1.getThrust()==0){
                    result.setThrust(obj2.getThrust());
                    //calculating retropressure of result
                    result.setRetropressure((int)(obj2.getThrust()*(obj2.getRetropercent()/100)));
                }else if(obj2.getThrust()==0){
                    result.setThrust(obj1.getThrust());
                    //calculating retropressure of result
                    result.setRetropressure((int)(obj1.getThrust()*(obj1.getRetropercent()/100)));
                }
                result.setDirection(obj1.getDirection()+obj2.getDirection());
                return result;
            case '*':
                //add value to thrust
                result.setThrust(obj1.getThrust()+obj2.getThrust());
                //if else statement used to set direction equal to rocket direction
                if(obj1.getDirection()==0.0){
                    result.setDirection(obj2.getDirection());
                    //calculating retropressure of result
                    result.setRetropressure((int)(obj2.getThrust()*(obj2.getRetropercent()/100)));
                }else if(obj2.getDirection()==0.0){
                    result.setDirection(obj1.getDirection());
                    //calculating retropressure of result
                    result.setRetropressure((int)(obj1.getThrust()*(obj1.getRetropercent()/100)));
                }
                return result;
            case '+':
                //add value to both thrust and direction
                result.setThrust(obj1.getThrust()+obj2.getThrust());
                result.setDirection(obj1.getDirection()+obj2.getDirection());
                //calculating retropressure of result
                result.setRetropressure((int)(result.getThrust()*(result.getRetropercent()/100)));
                return result;
            default:
                System.out.println("bad operator "+oper);
                return result;
        }//end of switch statement
    }//end of IntEval

    //findval will be used if we have an operand
    public static rocket findval(char x, char [] vtab, rocket[] valtb, int last) {
        int i;
        rocket vreturn = new rocket(-99,-99);
        // this finds the character x in the value table vtab and returns the
        //correspond interger value table from valtb
        for(i=0;i<=last; i++)
            if(vtab[i]==x){
                vreturn=valtb[i];
                //vreturn=valtb[i];
            }
        System.out.println("found this char: "+x+", value is: "+vreturn);
        return vreturn;
    }//end of findval;

    //findval2 will be used if we have an operator
    public static int findval2(char x, char [] vtab, int [] valtb, int last) {
        int i, vreturn=-99;
        // this finds the character x in the value table vtab and returns the
        //correspond interger value table from valtb
        for(i=0;i<=last; i++)
            if(vtab[i]==x)vreturn=valtb[i];
        System.out.println("found this char: "+x+", value is: "+vreturn);
        return vreturn;
    }//end of findval;

    public static void popevalandpush(GenericManagerStacks<Opertobj> x, GenericManagerStacks<rocket> y) {//this is the start of pop and push
        rocket a,b,c;
        char operandx;
        operandx=(x.popnode()).Getopert();
        a=y.popnode();
        b=y.popnode();
        System.out.println("in popeval "+b+operandx+a);
        c=IntEval(b,operandx,a);
        //now push the value back on the stack for integers
        y.pushnode(c);
        return;
    }//This is the end of popevalandpush
}//end of test2 class

class GenericManagerStacks<T>{//generic manager class for stacks
    protected ArrayList<T> mystack;
    protected int number;

    public GenericManagerStacks() {// this is the generic constructor
        number=0;//mcount is the next available value in array myarray
        mystack=new ArrayList<T>(100);//creates an initial arraylist of 100
    }

    public int getnumber(){return number;}
    public int pushnode(T x) {
        System.out.println("in pushnode "+number+", x is: "+x);
        //this pushes a node on the stack.  It will always add to the front(top) of the stack
        mystack.add(number,x);
        number++;
        System.out.println("leaving pushnode");
        return number;
    }// end of pushnode

    public T popnode() {//this function returns the first node in the list
        T nodeval;//this is the value in the node to be popped
        // find the node at the head of the list
        nodeval=mystack.get(number-1);
        //now pop the node by taking it off the list and moving head
        mystack.remove(number-1);
        number--;
        //now return the value of this node.
        return nodeval;
    }// this is the end of popnode

    public T peeknode() {//this function returns the contents of the top of the stack.  It does not
        //pop the node, just allows the user to look (peek) at the contents of the
        //first node on the stack.
        T nodeval; //this is the value to be peeked
        nodeval=mystack.get(number-1);
        return nodeval;
    }//this is the end of peeknode

    boolean stackempty(){if(number==0)return true;
    else return false;}
}//end of GenericManager class

class Opertobj {// this is an operator class it will hold a character operator and it's stack priority
    protected char operator;
    protected int priority;

    public Opertobj(char opert, int pri) {//this is the constructor for the operator object
        operator=opert;
        priority=pri;
    }
    public int Getprior(){return priority;};
    public char Getopert(){return operator;};
}//this is the end of the operator class

//create a class for rocket objects
class rocket{
    protected int thrust;
    protected double direction;
    protected double retropercent;
    protected int retropressure;

    //constructor for rocket object
    public rocket(int thrust, double direction){
        this.thrust = thrust;
        this.direction = direction;
        this.retropercent = 10.0; //retropercent will always equal 10.0%
        this.retropressure = (int)(thrust*(retropercent/100)); //retropressure will always equal thrust * 10%
    }

    //getters for variables
    public int getThrust(){return thrust;}
    public double getDirection(){return direction;}
    public double getRetropercent(){return retropercent;}
    public int getRetropressure(){return retropressure;}

    //setters for variables
    public void setThrust(int thrust){this.thrust = thrust;}
    public void setDirection(double direction){this.direction = direction;}
    public void setRetropercent(double retropercent){this.retropercent = retropercent;}
    public void setRetropressure(int retropressure){this.retropressure = retropressure;}

    //toString override
    @Override
    public String toString() {
        return "rocket{" +
                "thrust=" + thrust +
                ", direction=" + direction +
                ", retropercent=" + retropercent +
                ", retropressure=" + retropressure +
                '}';
    }
}