package com.yjfei.queue;import java.util.ArrayList;import java.util.Date;import java.util.HashSet;import java.util.List;import java.util.Set;import java.util.concurrent.CountDownLatch;import org.junit.Test;import com.yjfei.queue.common.StatusEnum;import com.yjfei.queue.common.Task;import com.yjfei.queue.service.TaskService;import com.yjfei.queue.service.impl.TaskServiceImpl;public class TestLockClash {    private static final int SIZE  = 30;    private static final int Count = 10;    static List<Long>        list  = new ArrayList<Long>();    static Date              date  = new Date();    public static void setup() {        TaskService service = new TaskServiceImpl();        Task task = new Task();        for (int i = 0; i < SIZE; i++) {            task.setGmtCreated(new Date(i, 3, i));            task.setGmtModified(new Date(i, 3, i));            task.setStatus(StatusEnum.Successs.getCode());            task.setCreator("test");            task.setModifier("modify");            service.insert(task);            list.add(task.getID());        }    }    private static class LockTask extends Thread {        private int            Num;        private TaskService    service = new TaskServiceImpl();        private List<Long>     result  = new ArrayList<Long>();        private CountDownLatch countDown;        public LockTask(int Num, CountDownLatch countDown) {            this.Num = Num;            this.countDown = countDown;        }        public long lock(Task task) {            task.setStatus(StatusEnum.Lock.getCode());            task.setModifier(String.valueOf(Num));            long c = service.updateByIDandReverseStatus(task);            if (c == 1) {                return task.getID();            } else {                return 0;            }        }        public void run() {            while (true) {                List<Task> list = service.listNonSuccessTask(0, 300);                if (list.size() == 0)                    break;                for (Task task : list) {                    long id = lock(task);                    if (id != 0) {                        result.add(id);                    }                }                System.out.print("Thread" + Num + "  lock:");                System.out.println(result);            }            countDown.countDown();            service.close();        }        public int getNum() {            return Num;        }        public List<Long> getResult() {            return result;        }    }    //娴嬭瘯鍐茬獊    @Test    public void TestClash() {        new Thread() {            public void run() {                while (true) {                    setup();                    try {                        Thread.sleep(1000 * 60);                    } catch (InterruptedException e) {                        e.printStackTrace();                    }                }            }        }.start();        int num = 0;        while (true) {            CountDownLatch countDown = new CountDownLatch(Count);            long start = System.currentTimeMillis();            List<LockTask> lockList = new ArrayList<LockTask>();            for (int i = 0; i < Count; i++) {                LockTask lock = new LockTask(i, countDown);                lock.start();                lockList.add(lock);            }            try {                countDown.await();                new TaskServiceImpl().clearTaskStatus();                System.out.println("cost:" + (System.currentTimeMillis() - start) / 1000 + "绉�");                Set<Long> set = new HashSet<Long>();                List<Long> hit = new ArrayList<Long>();                List<Integer> hitList = new ArrayList<Integer>();                for (LockTask lock : lockList) {                    for (Long n : lock.getResult()) {                        if (set.contains(n)) {                            hit.add(n);                            hitList.add(lock.getNum());                        } else {                            set.add(n);                        }                    }                }                if (hit.size() != 0) {                    System.out.print(num + "娆�:");                    System.out.println(hit);                    System.out.println("鍐茬獊绾跨▼:" + hitList);                    throw new RuntimeException(hit.toString());                }                num++;            } catch (InterruptedException e) {                e.printStackTrace();            }        }    }    public static void finish() {        TaskService service = new TaskServiceImpl();        for (long n : list) {            service.deleteByID(n);        }        service.close();    }}