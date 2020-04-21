public class MineInfo {
    private boolean isMine = false;
    private int adjacentMineCount = 0;

    public boolean isMine() {
        return isMine;
    }
    public void setMine(boolean mine) {
        isMine = mine;
    }
    public int getAdjacentMineCount() {
        return adjacentMineCount;
    }
    public void setAdjacentMineCount(int adjacentMineCount) {
        this.adjacentMineCount = adjacentMineCount;
    }
}