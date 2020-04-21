import java.util.ArrayList;
import java.util.Random;

public class Minefield {
    private boolean started = false;
    private boolean exploded = false;
    private int mineCount;
    private int width, height;
    private MineInfo[][] field;

    public Minefield(int width, int height) {
        field = new MineInfo[height][width];
        this.width = width;
        this.height = height;

        mineCount = (width * height)/5;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                field[y][x] = new MineInfo();
            }
        }
    }

    private Point getRandomPoint() {
        Random rand = new Random();
        int x = rand.nextInt(width);
        int y = rand.nextInt(height);
        return new Point(x, y);
    }

    private void populateMinesAvoidingPoint(int count, Point startPoint) {
        ArrayList<Point> mineLocations = new ArrayList<Point>();
        do {
            final Point randPoint = getRandomPoint();
            if (!startPoint.equals(randPoint) && !mineLocations.contains(randPoint)) {
                mineLocations.add(randPoint);
            }
        } while (mineLocations.size() < count);
        for (Point point : mineLocations) {
            field[point.getY()][point.getX()].setMine(true);
        }
    }

    private ArrayList<Point> validNeighboursForPoint(Point point) {
        ArrayList<Point> neighbours = new ArrayList<Point>();
        final int minX = Math.max(0, point.getX() - 1);
        final int maxX = Math.min(width - 1, point.getX() + 1);
        final int minY = Math.max(0, point.getY() - 1);
        final int maxY = Math.min(height - 1, point.getY() + 1);
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                neighbours.add(new Point(x, y));
            }
        }
        return neighbours;
    }

    private int countMinesAtPoints(ArrayList<Point> points) {
        int count = 0;
        for (Point point : points) {
            if (field[point.getY()][point.getX()].isMine()) {
                count++;
            }
        }
        return count;
    }

    private void populateAdjacentMineCount() {
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                final ArrayList<Point> neighbours = validNeighboursForPoint(new Point(x, y));
                field[y][x].setAdjacentMineCount(countMinesAtPoints(neighbours));
            }
        }
    }

    MineInfo checkMine(Point point) {
        if (!started) {
            started = true;
            populateMinesAvoidingPoint(mineCount, point);
            populateAdjacentMineCount();
        }
        MineInfo location = field[point.getY()][point.getX()];
        if (location.isMine()) {
            exploded = true;
        }
        MineInfo mineInfo = new MineInfo();
        mineInfo.setAdjacentMineCount(location.getAdjacentMineCount());
        mineInfo.setMine(location.isMine());
        return mineInfo;
    }
}
