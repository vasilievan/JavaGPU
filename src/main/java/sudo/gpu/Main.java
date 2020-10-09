package sudo.gpu;

import com.aparapi.Kernel;
import com.aparapi.Range;

import java.util.Random;

public class Main {
    public static void main(String[] _args) {
        int size = 1;
        sumMatrices(size, size);
        linearSearch(size);
        multiplyMatrices(size, size, size);
        radixSort(size);
        bubbleSort(size);
    }

    public static void sumMatrices(int row, int column) {
        final Random random = new Random();
        final float[][] augend = new float[row][column];
        final float[][] addend = new float[row][column];
        final float[][] summary = new float[row][column];
        for (int index = 0; index < row; index++) {
            for (int innerIndex = 0; innerIndex < column; innerIndex++) {
                augend[index][innerIndex] = random.nextFloat();
                addend[index][innerIndex] = random.nextFloat();
            }
        }
        final long startTime = System.currentTimeMillis();
        Kernel kernel = new Kernel() {
            @Override public void run() {
                int row = getGlobalId();
                int column = getPassId();
                summary[row][column] = augend[row][column] + addend[row][column];
            }
        };
        kernel.execute(row, column);
        long totalTime = System.currentTimeMillis() - startTime;
        kernel.dispose();
        System.out.println("Total time: " + totalTime);
    }

    public static void linearSearch(int size) {
        final float buffer[] = new float[size];
        final boolean found[] = new boolean[]{ false };
        final float theElement = 1234f;
        final long startTime = System.currentTimeMillis();
        Kernel kernel = new Kernel() {
            @Override public void run(){
                if (buffer[getGlobalId()] == theElement){
                    found[0] = true;
                }
            }
        };
        kernel.execute(buffer.length);
        long totalTime = System.currentTimeMillis() - startTime;
        kernel.dispose();
        System.out.println("Total time: " + totalTime);
    }

    public static void multiplyMatrices(int row, int columnFirst, int columnSecond) {
        final Random random = new Random();
        final float[] first = new float[columnFirst*row];
        final float[] second = new float[row*columnSecond];
        final float[] result = new float[columnFirst*columnSecond];

        for (int index = 0; index < first.length; index++) {
            first[index] = random.nextFloat();
            second[index] = random.nextFloat();
        }
        final long startTime = System.currentTimeMillis();
        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int i = getGlobalId();
                int j = getPassId();
                int value = 0;
                for (int k = 0; k < columnFirst; k++) {
                    value += first[k + i * columnFirst] * second[k * columnSecond + j];
                }
                result[i * columnFirst + j] = value;
            }
        };
        kernel.execute(row, columnSecond);
        long totalTime = System.currentTimeMillis() - startTime;
        kernel.dispose();
        System.out.println("Total time: " + totalTime);
    }

    public static void radixSort(int size) {
        final Random random = new Random();
        final int[] first = new int[size];
        final int[][] second = new int[19][size];
        for (int index = 0; index < size; index++) {
            first[index] = random.nextInt();
            second[9][index] = 1;
        }
        final double[] loopCount = { 0.0 };
        final int[] currentIndex = { 0 };
        final boolean[] continueTask = { true };
        long startTime = System.currentTimeMillis();
        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                while (continueTask[0]) {
                    continueTask[0] = false;
                    int i = getGlobalId();
                    int rem = (first[i] / (int) Math.pow(10.0, loopCount[0])) % 10 + 9;
                    second[rem][i] = first[i];
                    if (rem != 9) {
                        continueTask[0] = true;
                    }
                    for (int index = 0; index < 19; index++) {
                        for (int innerIndex = 0; innerIndex < size; innerIndex++) {
                            if (index != 9 && second[index][innerIndex] != 0) {
                                first[currentIndex[0]] = second[index][innerIndex];
                                second[index][innerIndex] = 0;
                                currentIndex[0]++;
                            }
                            if (index == 9 && second[index][innerIndex] != 1) {
                                first[currentIndex[0]] = second[index][innerIndex];
                                second[index][innerIndex] = 1;
                                currentIndex[0]++;
                            }
                        }
                    }
                    currentIndex[0] = 0;
                    loopCount[0] += 1.0;
                }
            }
        };
        kernel.execute(Range.create(size));
        long totalTime = System.currentTimeMillis() - startTime;
        kernel.dispose();
        System.out.println("Total time: " + totalTime);
    }

    public static void bubbleSort(int size) {
        final Random random = new Random();
        final int[] first = new int[size];
        for (int index = 0; index < size; index++) {
            first[index] = random.nextInt();
        }
        long startTime = System.currentTimeMillis();
        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int i = getGlobalId();
                for (int j = 0; j < i; j++) {
                    if (first[j] < first[i]) {
                        int temp = first[j];
                        first[j] = first[i];
                        first[i] = temp;
                    }
                }
            }
        };
        kernel.execute(Range.create(size));
        long totalTime = System.currentTimeMillis() - startTime;
        kernel.dispose();
        System.out.println("Total time: " + totalTime);
    }
}
