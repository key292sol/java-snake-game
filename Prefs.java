
public class Prefs {
	public static int SCREEN_SIZE = 600;
	public static int gameDelay = 100;
	public static int gridSize  = 15;
	private static int blockSize = SCREEN_SIZE/gridSize;
	
	public static void setGridSize(int size) {
		gridSize = size;
		blockSize = SCREEN_SIZE/gridSize;
	}
	
	public static int getBlockSize() {
		return blockSize;
	}
}
