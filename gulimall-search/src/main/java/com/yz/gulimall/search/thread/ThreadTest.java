package com.yz.gulimall.search.thread;

import java.util.concurrent.*;

/**
 * 异步&线程池练习
 */
public class ThreadTest {

    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start...");
            //runAsync无返回值
//        CompletableFuture.runAsync(()->{
//            System.out.println("当前线程:"+Thread.currentThread().getId());
//            int i=10/2;
//            System.out.println("运行结果："+i);
//        },executor);

        /**
         * 方法完成后的感知
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程:" + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).whenComplete((res, excption) -> {
//            //虽然能得到异常信息，但是没法修改返回数据。
//            System.out.println("异步任务成功完成了...结果是：" + res + ";异常是:" + excption);
//        }).exceptionally(throwable -> {
//            //可以感知异常，同时返回默认值
//            return 10;
//        });
//        Integer integer = future.get();
//        System.out.println("结果："+integer);

        /**
         * 方法执行完成后的处理
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程:" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).handle((res, thr) -> {
//            if (res != null) {
//                return res * 2;
//            }
//            if (thr != null) {
//                return 0;
//            }
//            return 0;
//        });
//        Integer integer = future.get();
//        System.out.println("结果："+integer);


        /**
         * 线程串行化
         * 1）、thenRun：不能获取到上一步的执行结果，无返回值
         *  .thenRunAsync(() -> {
         *             System.out.println("任务2启动了...");
         *         }, executor);
         * 2）、thenAcceptAsync;能接受上一步结果，但是无返回值
         * 3）、thenApplyAsync：;能接受上一步结果，有返回值
         */
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程:" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).thenApplyAsync(res -> {
//            System.out.println("任务2启动了..." + res);
//            return "hello" + res;
//        });
//        String s = future.get();
//        System.out.println(s);

        /**
         * 两个都完成
         */
//        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务1线程：" + Thread.currentThread().getId());
//            int i = 10 / 4;
//            try {
//                //Thread.sleep(3000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println("任务1结束：");
//            return i;
//        }, executor);
//
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(3000);
//                System.out.println("任务2线程：" + Thread.currentThread().getId());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println("任务2结束：");
//            return "Hello";
//        }, executor);

//        future01.runAfterBothAsync(future02,()->{
//            System.out.println("任务3开始了");
//        },executor);
//        future01.thenAcceptBothAsync(future02,(f1,f2)->{
//            System.out.println("任务3开始...之前的结果："+f1+"--》"+f2);
//        },executor);

//        CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
//            return f1 + "：" + f2 + " -> Haha";
//        }, executor);
//        String s = future.get();
//        System.out.println(s);


        /**
         * 两个任务，只要有一个完成，我们就执行任务3
         * runAfterEitherAsync：不感知结果，自己没有返回值
         * acceptEitherAsync：感知结果，自己没有返回值
         * applyToEitherAsync：感知结果，自己有返回值
         */
//        future01.runAfterEitherAsync(future02,()->{
//            System.out.println("任务3开始...之前的结果");
//        },executor);
//        future01.acceptEitherAsync(future02,(res)->{
//            System.out.println("任务3开始...之前的结果:"+res);
//        },executor);
//        CompletableFuture<String> future = future01.applyToEitherAsync(future02, (res) -> {
//            System.out.println("任务3开始...之前的结果：" + res);
//            return res.toString() + "->哈哈";
//        }, executor);
//        String s = future.get();
//        System.out.println(s);

        //多任务组合
        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的图片信息");
            return "hello.jpg";
        }, executor);

        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {

            try {
                Thread.sleep(3000);
                System.out.println("查询商品的属性");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "黑色+256G";
        }, executor);

        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品介绍");
            return "华为";
        }, executor);
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureDesc, futureImg, futureAttr);
//        allOf.get();//等待所有结果完成
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureAttr, futureDesc, futureImg);
        anyOf.get();
        System.out.println("main....end...."+anyOf.get());
    }








    /**
     * 1）、继承 Thread
     * 2）、实现 Runnable 接口
     * 3）、实现 Callable 接口 + FutureTask （可以拿到返回结果，可以处理异常）
     * 4）、线程池
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    /*public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main....start...");
//        1）、继承Thread
        //new Thread01().start();
        //2.实现Runnable接口
//        Runable01 runable01 = new Runable01();
//        new Thread(runable01).start();
        //3.实现 Callable 接口+ FutureTask （可以拿到返回结果，可以处理异常）
//        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
//        new Thread(futureTask).start();
//        Integer integer = futureTask.get();
//        System.out.println("返回的结果:"+integer);
        //4）、线程池[ExecutorService]
        //  给线程池直接提交任务。
//        ExecutorService service = Executors.newFixedThreadPool(3);
//        service.execute(new Runable01());
        *//**
         * 七大参数
         * corePoolSize:[5] 核心线程数[一直存在除非（allowCoreThreadTimeOut）]; 线程池，创建好以后就准备就绪的线程数量，就等待来接受异步任务去执行。
         *        5个  Thread thread = new Thread();  thread.start();
         * maximumPoolSize:[200] 最大线程数量;  控制资源
         * keepAliveTime:存活时间。如果当前的线程数量大于core数量。
         *      释放空闲的线程（maximumPoolSize-corePoolSize）。只要线程空闲大于指定的keepAliveTime；
         * unit:时间单位
         * BlockingQueue<Runnable> workQueue:阻塞队列。如果任务有很多，就会将目前多的任务放在队列里面。
         *              只要有线程空闲，就会去队列里面取出新的任务继续执行。
         * threadFactory:线程的创建工厂。
         * RejectedExecutionHandler handler:如果队列满了，按照我们指定的拒绝策略拒绝执行任务
         *
         *
         *
         * 工作顺序:
         * 1)、线程池创建，准备好core数量的核心线程，准备接受任务
         * 1.1、core满了，就将再进来的任务放入阻塞队列中。空闲的core就会自己去阻塞队列获取任务执行
         * 1.2、阻塞队列满了，就直接开新线程执行，最大只能开到max指定的数量
         * 1.3、max满了就用RejectedExecutionHandler拒绝任务
         * 1.4、max都执行完成，有很多空闲.在指定的时间keepAliveTime以后，释放max-core这些线程
         *
         *      new LinkedBlockingDeque<>()：默认是Integer的最大值。内存不够
         *
         * 一个线程池 core 7； max 20 ，queue：50，100并发进来怎么分配的；
         * 7个会立即得到执行，50个会进入队列，再开13个进行执行。剩下的30个就使用拒绝策略。
         * 如果不想抛弃还要执行。CallerRunsPolicy；
         *
         *//*
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                20,
                30000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(10000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        executor.execute(new Runable01());
        //        Executors.newCachedThreadPool() core是0，所有都可回收
//        Executors.newFixedThreadPool() 固定大小，core=max；都不可回收
//        Executors.newScheduledThreadPool() 定时任务的线程池
//        Executors.newSingleThreadExecutor() 单线程的线程池，后台从队列里面获取任务，挨个执行

        System.out.println("main....end...");
    }*/

    public static class Thread01 extends Thread{
        @Override
        public void run() {
            System.out.println("当前线程:"+Thread.currentThread().getId());
            int i=10/2;
            System.out.println("运行结果："+i);
        }
    }

    public static class Runable01 implements Runnable{
        @Override
        public void run() {
            System.out.println("当前线程:"+Thread.currentThread().getId());
            int i=10/2;
            System.out.println("运行结果："+i);
        }
    }

    public static class Callable01 implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程:"+Thread.currentThread().getId());
            int i=10/2;
            System.out.println("运行结果："+i);
            return i;
        }
    }


}

