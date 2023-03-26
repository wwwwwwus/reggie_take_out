package top.wusong.common;

//用于获取每个线程的字段，在更新新增操作时避免发送线程冲突
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程下的用户id
     * @param id
     */
    public static void setEmployeeId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取当前线程下的用户id
     * @return
     */
    public static Long getEmployeeId(){
        return threadLocal.get();
    }
}
