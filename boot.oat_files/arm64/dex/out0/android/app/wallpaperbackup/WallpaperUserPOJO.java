package android.app.wallpaperbackup;

public class WallpaperUserPOJO {
    private String component;
    private int height;
    private String name;
    private int userId;
    private int width;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public WallpaperUserPOJO(int width, int height, String name, String component, int userId) {
        this.width = width;
        this.height = height;
        this.name = name;
        this.component = component;
        this.userId = userId;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getName() {
        return this.name;
    }

    public String getComponent() {
        return this.component;
    }

    public int getUserId() {
        return this.userId;
    }
}
