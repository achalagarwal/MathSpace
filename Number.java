package MathSpace;

public class Number extends Literal{
    Double number;
    Number(double num){
        super(num);
        this.number = num;
    }

    @Override
    public String toString() {
        return Double.toString(this.number);
    }

    public static boolean isNumber(String s){
        s= s.replaceAll("\\s","");
        boolean hasDecimal = false;
        for(int i = 0;i<s.length();i++){
            char ch = s.charAt(i);
            if(((int)ch<=(int)'9'&&(int)ch>=(int)'0')||ch=='.'){
                if(ch=='.'&& !hasDecimal){
                    hasDecimal = true;
                }

                else if(ch=='.'&& hasDecimal)
                    return false;
            }
            else
                return false;
        }
        return true;
    }

    @Override
    public double getValue() {
        return super.getValue();
    }
}