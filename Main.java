import java.math.BigInteger;
import java.io.*;

class Fraction{
    BigInteger a;
    BigInteger b;
    Fraction(){
        a = BigInteger.ZERO;
        b = BigInteger.ZERO;
    }
    Fraction(BigInteger num, BigInteger den){
        a = num;
        b = den;
    }
}

public class Main
{
    public static Fraction[] initializeTree(){
        // creates the tree:    | 0/1 | 1/2 | 1/1 | 2/1 | 1/0 |
        // AT ANY POINT IN TIME: THE TREE ONLY NEEDS 5 INDICES AS YOU TRAVERSE
        Fraction five = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(0));
        Fraction four = new Fraction(BigInteger.valueOf(2), BigInteger.valueOf(1));
        Fraction three = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(1));
        Fraction two = new Fraction(BigInteger.valueOf(1), BigInteger.valueOf(2));
        Fraction on = new Fraction(BigInteger.valueOf(0), BigInteger.valueOf(1));
        Fraction[] ft = new Fraction[]{on,two,three,four,five};
        return ft;
    }
    public static BigInteger ineq(BigInteger M, BigInteger N, BigInteger a, BigInteger b){
        return ((N.multiply(a.pow(2))).subtract(M.multiply(b.pow(2)))).abs(); // = |N(a^2)-M(b^2)| <- this is the inequality the prof wants us to evaluate to find a/b
    }
    public static void goLeft(Fraction[] arr){
        // the "left wing" is added to the center to give you the "left leg", the "right wing" is added to the center to give you the "right leg"
        // The tree is traversed by evaluating N(a^2) >=< M(b^2). If N.. > go left, if N.. < go right.
        // the traversal shifts the entire array to focus on the chosen leg as the new center
        arr[4].a = arr[2].a;              // |_|_|x|_|y| -> |_|_|x|_|x|
        arr[4].b = arr[2].b;              // |_|_|x|_|y| -> |_|_|x|_|x|
        arr[2].a = arr[1].a;              // |_|a|x|_|x| -> |_|a|a|_|x|
        arr[2].b = arr[1].b;              // |_|a|x|_|x| -> |_|a|a|_|x|
        arr[1].a = arr[0].a.add(arr[2].a);// |d|a|a|_|x| -> |d|(d+a)|a|_|x|
        arr[1].b = arr[0].b.add(arr[2].b);// |d|a|a|_|x| -> |d|(d+a)|a|_|x|
        arr[3].a = arr[2].a.add(arr[4].a);// |d|a|a|_|x| -> |d|(d+a)|a|(a+x)|x|
        arr[3].b = arr[2].b.add(arr[4].b);// |d|a|a|_|x| -> |d|(d+a)|a|(a+x)|x|
    }
    public static void goRight(Fraction[] arr){
        // the "left wing" is added to the center to give you the "left leg", the "right wing" is added to the center to give you the "right leg"
        // The tree is traversed by evaluating N(a^2) >=< M(b^2). If N.. > go left, if N.. < go right.
        // the traversal shifts the entire array to focus on the chosen leg as the new center
        arr[0].a = arr[2].a;              // |d|_|x|_|_| -> |x|_|x|_|_|
        arr[0].b = arr[2].b;              // |d|_|x|_|_| -> |x|_|x|_|_|
        arr[2].a = arr[3].a;              // |x|_|x|b|_| -> |x|_|b|b|_|
        arr[2].b = arr[3].b;              // |x|_|x|b|_| -> |x|_|b|b|_|
        arr[1].a = arr[0].a.add(arr[2].a);// |x|_|b|b|_| -> |x|(x+b)|b|b|_|
        arr[1].b = arr[0].b.add(arr[2].b);// |x|_|b|b|_| -> |x|(x+b)|b|b|_|
        arr[3].a = arr[2].a.add(arr[4].a);// |x|(x+b)|b|b|y| -> |x|(x+b)|b|(b+y)|y|
        arr[3].b = arr[2].b.add(arr[4].b);// |x|(x+b)|b|b|y| -> |x|(x+b)|b|(b+y)|y|
    }
    public static Fraction findIt(BigInteger M, BigInteger N){
        Fraction[] ft = initializeTree();
        BigInteger compCenter = ineq(M,N,ft[2].a,ft[2].b);// |N(a^2)-M(b^2)|
        int result = compCenter.compareTo(ft[2].b);       // |N(a^2)-M(b^2)| >=< b ?
        int wingCheck = 0;
        while(result == 1 || result == 0){                // result changes to -1 when the ineq method returns < b
            wingCheck = N.multiply(ft[2].a.pow(2)).compareTo(M.multiply(ft[2].b.pow(2))); // N(a^2) >=< M(b^2)
            if(wingCheck == 1){
                goLeft(ft);             // go left if N(a^2) > M(b^2)
            }
            else if(wingCheck == -1){
                goRight(ft);            // go right if N(a^2) < M(b^2)
            }
            compCenter = ineq(M,N,ft[2].a,ft[2].b).abs();       // |N(a^2)-M(b^2)|
            result     = compCenter.compareTo(ft[2].b);         // |N(a^2)-M(b^2)| >=< b ?
        }
        return ft[2];
    }
    public static void main(String[] args) throws IOException
    {
        //file stuff
        FileReader input = new FileReader("input.txt");
        BufferedReader bufferRead = new BufferedReader(input);
        File out = new File("output.txt");
        if(!out.exists()){
            out.createNewFile();
        }
        FileWriter output = new FileWriter(out);
        BufferedWriter bufferWrite = new BufferedWriter(output);
        BigInteger m = new BigInteger(bufferRead.readLine());
        BigInteger n = new BigInteger(bufferRead.readLine());
        //tree search/creation
        Fraction a_b = findIt(m,n);
        //output
        bufferWrite.write(a_b.a.toString()+"\n"+a_b.b.toString());
        bufferWrite.close();
        //close output end program
    }
}
