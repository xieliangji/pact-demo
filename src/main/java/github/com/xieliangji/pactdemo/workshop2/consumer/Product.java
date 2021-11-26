package github.com.xieliangji.pactdemo.workshop2.consumer;

/**
 * Coder   谢良基
 * Date    2021/11/25 13:30
 */
public class Product {

    private int id;

    private String type;

    private String name;

    private String version;

    public Product() {}

    public Product(int id, String type, String name, String version) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
