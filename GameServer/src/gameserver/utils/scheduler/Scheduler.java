package gameserver.utils.scheduler;

import gameserver.utils.scheduler.Expression;
import gameserver.utils.ThreadPoolManager;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.lang.Thread;
import java.util.Collection;

import javolution.util.FastList;
import javolution.util.FastMap;

public class Scheduler {
    
    
    private static final Logger log = Logger.getLogger(Scheduler.class);
    
    protected FastMap<Expression, Runnable> scheduledTasks;
    
    protected Collection<Runnable> longTask;
    
    
    /**
     * @return Scheduler instance.
     */
    private static final class SingletonHolder {
        private static final Scheduler INSTANCE = new Scheduler();
    }

    public static Scheduler getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    private Scheduler() {
        longTask = new FastList<Runnable>().shared();
        scheduledTasks = new FastMap<Expression, Runnable>().shared();
        ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int e = execute();
                if (e > 0)
                    log.info(e + " scheduled tasks executed.");
            }
        }, 1000, 60000);
    }
    
    public void schedule(Runnable r, String expr, boolean longTask) {
        synchronized (scheduledTasks) {
            scheduledTasks.put(new Expression(expr), r);
        }
        if (longTask) {
            synchronized(this.longTask) {
                this.longTask.add(r);
            }
        }   
    }
    
    public void schedule(Runnable r, String expr) {
        schedule(r, expr, false);
    }
    
    public int execute() {
        int executed    = 0;
        Date now        = new Date();
        synchronized(scheduledTasks) { 
            synchronized(longTask) {
                Iterator it = scheduledTasks.keySet().iterator();
                while (it.hasNext()) {
                    final Expression expr = (Expression)it.next();
                    if (expr.isValideDate(now)) {
                        Runnable r = scheduledTasks.get(expr);
                        if (longTask.contains(r))
                            ThreadPoolManager.getInstance().execute(r);
                        else
                            r.run();
                        executed++;
                    }
                }
                return executed;
            }
        }
    }
}
