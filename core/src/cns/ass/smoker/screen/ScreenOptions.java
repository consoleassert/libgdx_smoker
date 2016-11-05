package cns.ass.smoker.screen;

/**
 * Created by Ad on 05.11.2016.
 */
public class ScreenOptions {
    public String getLevelAtlasName() {
        return levelAtlasName;
    }

    public void setLevelAtlasName(String levelAtlasName) {
        this.levelAtlasName = levelAtlasName;
    }

    public String getLevelTilemapFile() {
        return levelTilemapFile;
    }

    public void setLevelTilemapFile(String levelTilemapFile) {
        this.levelTilemapFile = levelTilemapFile;
    }

    private String levelAtlasName;
    private String levelTilemapFile;


    public ScreenOptions(String levelAtlasName, String levelTilemapFile) {
        this.levelAtlasName = levelAtlasName;
        this.levelTilemapFile = levelTilemapFile;
    }

    public ScreenOptions(String levelAtlasName) {
        this.levelAtlasName = levelAtlasName;
    }
}
