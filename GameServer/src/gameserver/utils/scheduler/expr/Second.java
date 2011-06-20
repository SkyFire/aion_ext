package gameserver.utils.scheduler.expr;

import gameserver.utils.scheduler.expr.Expr;

public class Second extends Expr {
    
    protected int max = 60;
    
    public Second(String str) {
        super(str);
        setMax(max);
        parse();
    }
}
