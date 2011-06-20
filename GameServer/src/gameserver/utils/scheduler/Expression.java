package gameserver.utils.scheduler;

import gameserver.utils.scheduler.expr.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.GregorianCalendar;

class Expression {
    
    protected Map<String, Expr> expressions;
    
/*    public static void main(String[] args) {
        Expression a = new Expression();
        a.parse(args[0]);
        if (a.isValideDate(new Date()))
            System.out.println("the date is valid");
    }*/
    
    public Expression() {
        expressions = new HashMap<String, Expr>();
    }
    
    public Expression(String expr) {
        expressions = new HashMap<String, Expr>();
        parse(expr);
    }
    
    public boolean isValideDate(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        if (expressions.containsKey("day")) {
            for (int a : expressions.get("day").getValues())
            if (!expressions.get("day").isValidValue(calendar.get(calendar.DAY_OF_WEEK)))
                return false;
        }
        
        if (expressions.containsKey("hour")) {
            for (int a : expressions.get("hour").getValues())
            if (!expressions.get("hour").isValidValue(calendar.get(calendar.HOUR_OF_DAY)))
                return false;
        }
        
        if (expressions.containsKey("minute")) {
            if (!expressions.get("minute").isValidValue(calendar.get(calendar.MINUTE)))
                return false;
        }
        
        return true;
    }
    
    public void parse(String expr) {
        String[] parts = expr.split(" ");
        for (int a = 0; a < parts.length;a++) {
            switch(a) {
                case 0:
                    expressions.put("minute", new Minute(parts[a]));
                    break;
                case 1:
                    expressions.put("hour", new Hour(parts[a]));
                    break;
                case 2:
                    expressions.put("day", new WeekDay(parts[a]));
                    break;
            }
        }
    }
    
}
