package gameserver.utils.scheduler.expr;

import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.InvocationTargetException;

public class Expr {
    
    protected String str;
    
    protected List<Expr> expressions;
    
    protected int max;
    protected int from;
    protected int to;
    protected int interval = 1;
    
    public Expr(String str) {
        this.str = str;
        expressions = new ArrayList<Expr>();
    }
    
    public void parse() {
        String[] parts = str.split(",");
        if (parts.length == 1) {
            parsePart(parts[0]);
        } else {
            split(parts);
        }
    }
    
    public void parsePart(String str) {
        str = parseInterval(str);
        parseRange(str);
    }
    
    public void parseRange(String str) {
        String[] tmp = str.split("-");
        if (tmp.length > 1) {
            parseRange(tmp);
        } else {
            parseSimpleRange(str);
        }
    }
    
    public void parseSimpleRange(String str) {
        if (str.compareTo("*") == 0) {
            from    = 0;
            to      = getMax();
        } else {
            from    = Integer.parseInt(str);
            to      = Integer.parseInt(str);
        }
    }
    
    public void parseRange(String[] str) {
        from = Integer.parseInt(str[0]);
        to = Integer.parseInt(str[1]);
    }
    
    public String parseInterval(String str) {
        String[] tmp = str.split("/");
        if (tmp.length > 1) {
            str = tmp[0];
            interval = Integer.parseInt(tmp[1]);
        }
        return str;
    }
    
    public void debug() {
        if (expressions.size() > 0) {
            for (Expr expr : expressions)
                expr.debug();
            return;
        }
        str = "Original str : " + str + "\n";
        str += "from : " + from + "\n";
        str += "to : " + to + "\n";
        str += "interval : " + interval + "\n";
        System.out.println(str);
    }
    
    
    public void split(String[] parts) {
        for (int i=0;i<parts.length;i++) {
            try {
                Object[] args = new Object[1];
                args[0] = parts[i];
                expressions.add(
                    getClass().getConstructor(
                        new Class[] { String.class }
                    ).newInstance(
                        args
                    )
                );
            } catch (NoSuchMethodException e) {
                System.out.println(e);
            } catch (InstantiationException e) {
                System.out.println(e);
            } catch (IllegalAccessException e) {
                System.out.println(e);
            } catch (InvocationTargetException e) {
                System.out.println(e);
            }
        }
    }
    
    public int getMax() {
        return this.max;
    }
    
    public void setMax(int max) {
        this.max = max;
    }
    
    public List<Integer> getValues() {
        List<Integer> values = new ArrayList<Integer>();
        if (from == to) {
            values.add(from);
            return values;
        }
        for (int i = from ; i < to ; i += interval)
            values.add(i);
        return values;
    }
    
    public boolean isValidValue(int value) {
        return getValues().contains(value);
    }
}
