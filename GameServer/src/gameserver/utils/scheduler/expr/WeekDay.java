package gameserver.utils.scheduler.expr;

import gameserver.utils.scheduler.expr.Expr;

public class WeekDay extends Expr {
    
    protected int max = 6;
    
    public WeekDay(String str) {
        super(str);
        setMax(max);
        parse();
    }
}
