package gameserver.utils.scheduler.expr;

import gameserver.utils.scheduler.expr.Expr;

public class Hour extends Expr {
    
    protected int max = 24;
    
    public Hour(String str) {
        super(str);
        setMax(max);
        parse();
    }
}
