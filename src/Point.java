class Point {

    private int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    int getX() {
        return x;
    }
    int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point){
            Point otherPoint = (Point)obj;
            return this.x == otherPoint.x && this.y == otherPoint.y ;
        }
        return false;
    }
}