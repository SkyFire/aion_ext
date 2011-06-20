package gameserver.utils.scheduler.expr;

import gameserver.utils.scheduler.expr.Expr;

public class Minute extends Expr {
    
    protected int max = 60;
    
    public Minute(String str) {
        super(str);
        setMax(max);
        parse();
    }
}
