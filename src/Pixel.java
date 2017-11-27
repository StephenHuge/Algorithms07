public class Pixel implements Comparable<Pixel> {
    private int col, row;
    
    private double energy;                          // each pixel's energy
    
    private double distance = Double.POSITIVE_INFINITY;     // initial distance is infinity
    
    private Pixel father = null;                            // for searching back the path
    
    public Pixel(int col, int row, double energy) {
        this.col = col;
        this.row = row;
        this.energy = energy;
    }

    public int col() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int row() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public double energy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double dist() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    public Pixel father() {
        return father;
    }

    public void setFather(Pixel father) {
        this.father = father;
    }

    @Override
    public int compareTo(Pixel p) {
        if (this.dist() - p.dist() < 0) return -1;
        if (this.dist() - p.dist() > 0) return 1;
        else                            return 0;
    }

    @Override
    public String toString() {
        return "(" + col + ", " + row + ") -> Energy : " + energy + 
                "\n\t-> Dist : " + distance + "\n\t- > Father : "
                + father;
    }
    
}
