package com.yjfei.queue;import static org.junit.Assert.assertEquals;import static org.junit.Assert.assertNotNull;import static org.junit.Assert.assertTrue;import java.util.ArrayList;import java.util.Date;import java.util.List;import org.junit.AfterClass;import org.junit.BeforeClass;import org.junit.Test;import com.yjfei.queue.common.StatusEnum;import com.yjfei.queue.common.Task;import com.yjfei.queue.service.TaskService;import com.yjfei.queue.service.impl.TaskServiceImpl;public class TestTaskService {    private static final int SIZE    = 100;    private static final int START   = 100;    static TaskService       service = new TaskServiceImpl();    static List<Long>        list    = new ArrayList<Long>();    static Date              date    = new Date();    @BeforeClass    public static void setup() {        Task task = new Task();        for (int i = 0; i < SIZE; i++) {            task.setGmtCreated(new Date(2012, 3, 3));            task.setGmtModified(new Date(2012, 3, 3));            task.setStatus(StatusEnum.Successs.getCode());            task.setCreator("test");            task.setModifier("modify");            service.insert(task);            list.add(task.getID());        }    }    @Test    public void testInsert() {        Task task = new Task();        task.setGmtCreated(new Date());        service.insert(task);        list.add(task.getID());        Task existed = service.findByID(task.getID());        assertNotNull(existed);        assertEquals(task.getID(), existed.getID());    }    @Test    public void testListAll() {        List<Task> existList = service.listAll(0, SIZE);        assertNotNull(existList);        assertTrue(existList.size() == SIZE);    }    @Test    public void testListByStatus() {        List<Task> existList = service.listByStatus(StatusEnum.Successs, 0, SIZE);        assertNotNull(existList);        assertTrue(existList.size() == SIZE);    }    @Test    public void testDelete() {        long c = service.deleteByDate(new Date("2012-3-4"), new Date("2015-3-4"));        System.out.println(c);    }    @Test    public void testListByCreateUser() {        List<Task> existList = service.listByCreateUser("test", 0, SIZE);        assertNotNull(existList);        assertTrue(existList.size() == SIZE);    }    @Test    public void testListByModifyUser() {        List<Task> existList = service.listByModifyUser("modify", 0, SIZE);        assertNotNull(existList);        assertTrue(existList.size() == SIZE);    }    @Test    public void testListByCreateDate() {        List<Task> existList = service.listByCreateDate(new Date(2012, 3, 3), new Date(2012, 3, 9), 0, SIZE);        assertNotNull(existList);        assertTrue(existList.size() == SIZE);    }    @Test    public void testListByModifyDate() {        List<Task> existList = service.listByModifyDate(new Date(2012, 3, 3), new Date(2012, 3, 9), 0, SIZE);        assertNotNull(existList);        assertTrue(existList.size() == SIZE);    }    @Test    public void testClearTaskStatus() {        assertTrue(service.clearTaskStatus() > 0);    }    @AfterClass    public static void finish() {        for (long n : list) {            service.deleteByID(n);        }        service.close();    }}