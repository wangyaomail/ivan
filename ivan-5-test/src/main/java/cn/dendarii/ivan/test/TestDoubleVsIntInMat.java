package cn.dendarii.ivan.test;

import java.lang.Thread.State;
import java.util.Random;

/**
 * 测试double执行矩阵运算比int快多少
 */

public class TestDoubleVsIntInMat {
    static class TestDoubleVsIntInMatMulThread extends Thread {
        public int name;
        public double avgMultiple = 0.0;

        @Override
        public void run() {
            int dim = 6000, batchNum = 10;
            Random rand = new Random();
            double[][] mfx = new double[dim][dim];
            double[][] mfy = new double[dim][dim];
            double[][] mfz = new double[dim][dim];
            int[][] mix = new int[dim][dim];
            int[][] miy = new int[dim][dim];
            int[][] miz = new int[dim][dim];
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int batch = 0; batch < batchNum; batch++) {
                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        mfx[i][j] = rand.nextDouble() + mfz[i][j]; // 打乱JAVA代码优化
                        mfy[i][j] = rand.nextDouble() + mfz[i][j];
                        mix[i][j] = rand.nextInt() + miz[i][j];
                        miy[i][j] = rand.nextInt() + miz[i][j];
                    }
                }
                long t1 = System.nanoTime();
                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        mfz[i][j] = mfx[i][j] * mfy[i][j];
                    }
                }
                long t2 = System.nanoTime();
                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        miz[i][j] = mix[i][j] * miy[i][j];
                    }
                }
                long t3 = System.nanoTime();
                long timediff = 2 * t2 - t1 - t3;
                long avgtime = (t3 - t1) / 2;
                double rate = 1.0 * timediff / avgtime;
                double multiple = 1.0 * (t2 - t1) / (t3 - t2);
                System.out.println(name + "_" + batch + ":\t" + timediff + "\t" + avgtime + "\t"
                        + rate + "\t" + multiple + "\t" + mfz[0][0] + "\t" + miz[0][0]);
                avgMultiple += multiple;
            }
            avgMultiple /= batchNum;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int threadNum = 10;
        TestDoubleVsIntInMatMulThread[] testThreads = new TestDoubleVsIntInMatMulThread[10];
        for (int i = 0; i < threadNum; i++) {
            testThreads[i] = new TestDoubleVsIntInMatMulThread();
            testThreads[i].name = i;
        }
        for (int i = 0; i < threadNum; i++) {
            testThreads[i].start();
        }
        testThreads[threadNum - 1].join();
        while (true) {
            for (int i = 0; i < threadNum; i++) {
                if (testThreads[i].getState().equals(State.RUNNABLE)) {
                    Thread.sleep(3000);
                    continue;
                }
            }
            break;
        }
        double allAvgMultiple = 0;
        for (int i = 0; i < 10; i++) {
            allAvgMultiple += testThreads[i].avgMultiple;
        }
        allAvgMultiple /= threadNum;
        System.out.println("all thread ends, allAvgMultiple=" + allAvgMultiple);
    }
}
