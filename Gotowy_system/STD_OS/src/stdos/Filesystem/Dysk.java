package stdos.Filesystem;
//w trakcie budowy
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.Integer.parseInt;

public abstract class Dysk {
    private static final int BLOCK_SIZE = 32;
    static final byte EMPTY_CELL = -3;
    static final byte INDEX_CELL = -2; //marks beginning of an index block

    private static byte physicalDisk[] = new byte[1024];
    private static boolean blockTaken[] = new boolean[physicalDisk.length / BLOCK_SIZE];
    private static int blockAmount = blockTaken.length;
    private static int currentBlock = 0;

    static {
        Arrays.fill(physicalDisk, EMPTY_CELL);
    }

    public static byte[] empty() {
        return new byte[]{EMPTY_CELL};
    }

    public static byte[] invalid() {
        return new byte[]{-1};
    }

    public static byte[] index() {
        return new byte[]{INDEX_CELL};
    }


    /**
     * Gets the value of specified block
     *
     * @param index block to be returned
     * @return byte[] corresponding to specified block
     */
    public static byte[] getBlock(int index) {
        return Arrays.copyOfRange(physicalDisk, index * BLOCK_SIZE, index * BLOCK_SIZE + BLOCK_SIZE);
    }

    /***
     * Gets all blocks assigned to an index block
     *
     * @param index     which index block to get content from
     * @return byte[] of all data blocks
     */
    public static byte[] getBlockByIndex(int index) {
        byte[] indexBlock = getBlock(index);
        int blockAmount = 0;
        Vector<Byte> temp = new Vector<>();
        for (int i = 1; i < indexBlock.length; i++) {
            if (indexBlock[i] == EMPTY_CELL) break;
            blockAmount++;
        }
        for (int i = 1; i <= blockAmount; i++) {
            for (byte e : getBlock((char) indexBlock[i])) {
                temp.add(e);
            }
        }
        byte[] result = new byte[blockAmount * BLOCK_SIZE];
        for (int i = 0; i < blockAmount * BLOCK_SIZE; i++) {
            result[i] = temp.get(i);
        }
        return result;
    }


    /**
     * Inserts data into a block. Does not divide data, does not check for empty block.
     *
     * @param index   which block to insert into
     * @param content content ot be inserted, no longer than {@value BLOCK_SIZE}
     */
    static void setBlock(int index, byte[] content) {
        int max = content.length < BLOCK_SIZE ? BLOCK_SIZE : content.length;
        blockTaken[index] = true;
        for (int i = 0; i < max; i++) {
            if (i >= content.length) {
                physicalDisk[index * BLOCK_SIZE + i] = EMPTY_CELL;
            } else {
                physicalDisk[index * BLOCK_SIZE + i] = content[i];
            }
        }
    }


    static boolean isTaken(int index) {
        return blockTaken[index];
    }

    /**
     * Finds free block on disk, checking whole disk
     *
     * @param index from which index to start looking
     * @return next free index
     */
    static int findNextFree(int index) {
        for (int i = index; i < blockAmount; i++) {
            if (!isTaken(i)) {
                return i;
            }
        }
        for (int i = 0; i < index; i++) {
            if (!isTaken(i)) {
                return i;
            }
        }
        return -1;
    }

    static byte[] divideContent(byte content[]) {
        if (content.length < BLOCK_SIZE) {
            return content;
        }
        return Arrays.copyOfRange(content, 0, BLOCK_SIZE);
    }

    /**
     * Writes data onto disk searching for free space, dividing as needed.
     *
     * @param content content to be written
     * @param index   from which index to start searching
     * @return number of the assigned index block`
     */
    public static int addContent(byte[] content, int index) {
        int currentIndex = findNextFree(index);
        int x = 0;
        byte currentContent[] = content;
        byte indexBlock[] = new byte[BLOCK_SIZE];
        Arrays.fill(indexBlock, EMPTY_CELL);
        indexBlock[0] = INDEX_CELL;
        do {
            currentIndex = findNextFree(currentIndex);
            currentContent = Arrays.copyOfRange(content, BLOCK_SIZE * x, content.length);
            setBlock(currentIndex, divideContent(currentContent));
            x++;
            indexBlock[x] = Byte.parseByte(Integer.toString(currentIndex));
        } while (currentContent.length > BLOCK_SIZE);
//        System.out.println(Arrays.toString(indexBlock));
        int freeIndex = findNextFree(0);
        setBlock(freeIndex, indexBlock);
        return freeIndex;
    }

    public static void remove(int index) {
        byte[] indexBlock = getBlock(index);
        if (indexBlock[0] == INDEX_CELL) {
            blockTaken[index] = false;
            for (int i = 1; i < BLOCK_SIZE; i++) {
                if (indexBlock[i] != EMPTY_CELL) {
                    blockTaken[indexBlock[i]] = false;
                } else {
                    break;
                }
            }
        }
    }


    /***
     * Shows disk content in a formatted table
     */
    public static void show() {
        System.out.print("     ");
        for (int i = 0; i <= BLOCK_SIZE / 10; i++) {
            System.out.print(i + "                             ");
        }
        System.out.println();
        System.out.print("     ");
        for (int i = 0; i < BLOCK_SIZE; i++) {
            System.out.print(String.format("%2s", i % 10 + "  "));
        }
        System.out.print(" \t\ttaken");
        System.out.println();
        for (int i = 0; i < physicalDisk.length / BLOCK_SIZE; i++) {
            System.out.print((i < 10 ? " " + i : i) + "  ");
            boolean iBlock = false;
            for (byte y : getBlock(i)) {
                String temp = "";
                temp += (char) y;
                if (iBlock) {
                    System.out.print(String.format("%2s", (y == EMPTY_CELL ? "_" : (int) y)) + " ");
                    continue;
                }
                if (Character.isSpaceChar(temp.charAt(0))) {
                    temp = " ";
                } else if (temp.charAt(0) == '\n') {
                    temp = "\\n";
                } else if (temp.charAt(0) == '\t') {
                    temp = "\\t";
                } else if (y == -2) {
                    temp = "!";
                    iBlock = true;
                } else if (y < 32){
                    temp = "?";
                }
                System.out.print(String.format("%2s", (y == EMPTY_CELL ? "_" : temp)) + " ");
            }
            System.out.println(String.format("\t%2d\t%s", i ,blockTaken[i]));
        }
        System.out.println();
    }

    public static boolean run() {
        Scanner scan = new Scanner(System.in);
        System.out.print(">");
        String input = scan.nextLine();
        addContent(input.getBytes(), currentBlock);
        show();

        return true;
    }

}
