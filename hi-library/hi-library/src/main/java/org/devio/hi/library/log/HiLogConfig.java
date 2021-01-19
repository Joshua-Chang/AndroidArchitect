package org.devio.hi.library.log;

public abstract class HiLogConfig {
    static int MAX_LEN=512;
    static HiStackTraceFormatter HI_STACK_TRACE_FORMATTER=new HiStackTraceFormatter();
    static HiThreadFormatter HI_THREAD_FORMATTER=new HiThreadFormatter();

    public String getGlobalTag(){return "HiLog"; }
    public boolean enable(){return true;}
    public boolean includeThread(){return false;}
    public int stackTraceDepth(){
        return 5;
    }
    public HiLogPrinter[]printers(){
        return null;
    }
    public JsonParser injectJsonParser(){
        return null;
    }
    public interface JsonParser{
        String toJson(Object src);
    }
}
