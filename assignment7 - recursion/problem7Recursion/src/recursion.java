/*This program is capable of recursively calculating factorials.
* It will use helper method runRecursion to check if the factorial is larger than 0.
* After the factorial has been determined to be larger than 0, method  recursiveFactorial will be used to calculate the factorial.
* This program contains class recursion and two methods, runRecursion and recursiveFactorial.*/
public class recursion {
    public static void main(String[] args){
        runRecursion(12);
        runRecursion(25);
        runRecursion(-5);
    }//end of main

    public static void runRecursion(int x){//a helper method for recursiveFactorial
        if(x<=0){//if the number is 0 or smaller, it will print out this error message rather than running the recursive method
            System.out.println("Factorial must be larger than 0!");
        }else{
            //it will run the recursive function
            System.out.println("The factorial of " + x + " is " + recursiveFactorial(x));
        }
    }//end of runRecursion

    public static float recursiveFactorial(int factorial){//the actual method of recursion for calculating factorials
        if(factorial<=1){
            return (float)(1.0);
        }else{
            return factorial * recursiveFactorial(factorial-1);// method is calling upon itself
        }//end of recursiveFactorial
    }
}//end of class recursion