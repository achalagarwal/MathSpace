package MathSpace;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.events.CloseEventsTest;

import javax.sound.sampled.Line;
import java.util.*;

public class Workspace {
    ArrayList<Expression> expressions;
    ArrayList<Equation> equations;
    Set<Literal> variables;
    Object solveLock;
    ArrayList<LinearGroup> lgroups;
    Workspace(){
        lgroups = new ArrayList<>();
        expressions = new ArrayList<>();
        equations = new ArrayList<>();
        variables = new HashSet<>();
    }
    public void add(Equation e) throws Exception{
        variables.addAll(Utility.literalUnify(variables,e.vars));
        e.updateExpressions();
        this.equations.add(e);
        this.generateGroupsLastAdded();
    }
    public void add(Expression e){
        ArrayList<Literal> temp1 = new ArrayList<>(e.literals);
        expressions.add(e);
        for(int i = 0;i<temp1.size();i++){
            Literal l1 = temp1.get(i);
            boolean inserted = false;
           for(Literal l2:variables) {
               if (l1.equals(l2)) {
                   temp1.remove(i);
                   temp1.add(i, l2);
                   inserted = true;
                   break;
               }
           }
           if(!inserted)
               this.variables.add(l1);
        }
        e.literals = new HashSet<>(temp1);
        e.recurseLiterals();
    }
    public void checkLinearGroup(ArrayList<Equation> alist){
        new Group(alist); //if solved recurse all groups (damn)

    }
    public void generateGroupsLastAdded() throws Exception {
//        ArrayList<Equation> alist = new ArrayList<>();
//        alist.add(equations.get(equations.size()-1));
//        checkLinearGroup(alist);
//        int groupSize = 2;
//        for(int j = 0;j<this.equations)
//        for(int i = 0;i<this.equations.size()-groupSize;i++){
//            alist.add(this.equations.get(i));
//            checkLinearGroup(alist);
//            alist.remove(1);
//        }
        ArrayList<Equation> alist = new ArrayList<>();
        Equation eq = equations.get(equations.size()-1);
        alist.add(eq);
        LinearGroup lg = new LinearGroup(alist);
        lgroups.add(lg);
        if(lg.solve()==1) {

            lgroups.remove(lg);
            linearGroupSolve();
            return;
        }

        int s = lgroups.size()-1;
        //checkLinearGroup(alist);

        for(int i = 0;i<s;i++){
            alist = new ArrayList<>(lgroups.get(i).eqs);
            if(!alist.contains(eq)) {
                alist.add(eq);
                lg = new LinearGroup(alist);
                lgroups.add(lg);
                if (lg.solve() == 1) {
                    lgroups.remove(lg);
                    i--;
                    linearGroupSolve();
                    s = lgroups.size()-1;
                }

            }
        }
    }
    public void linearGroupSolve() throws Exception {
        Iterator<LinearGroup> i = lgroups.iterator();
        while(i.hasNext()){
            if(i.next().solve() == 1){
                i.remove();
                linearGroupSolve();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        Workspace w = new Workspace();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String abc = sc.nextLine();
            if(abc.equalsIgnoreCase("q")||abc.equalsIgnoreCase("quit"))
                break;
            w.add(Operator.checkChain(abc));
//            for(Literal l:prev.literals){
//                l.addLiteral(w.variables);
//            }
        }
        for(Expression e:w.expressions){
            e.getResult();
            if(e.hasResult())
                System.out.println(e.toString() + " --> " + e.result);
        }
        for(Literal l:w.variables){
            if(l.hasValue())
            System.out.println(l.toString()+" -> "+l.getValue());
        }
    }

    private void add(Object o) throws Exception {
        if(o instanceof Expression)
            add((Expression)o);
        else if(o instanceof Equation)
            add((Equation) o);
    }
}
