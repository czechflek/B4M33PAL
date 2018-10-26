package pal;

public class Node implements Comparable{
    private Integer id;
    private Integer distance;

    public Node(Integer id, Integer distance) {
        this.id = id;
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(this.getDistance(), ((Node) o).getDistance());
    }
}
