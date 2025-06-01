import java.awt.*;
import java.util.Random;

public class ColorGenerator
{
    private static int groupCount;

    public static Color[] generateColors(long seed) {
        Color[] colors = new Color[groupCount];
        Random rand = new Random(seed);
        for (int i = 0; i < groupCount; i++) {
            float hue = rand.nextFloat();
            float saturation = 0.6f + rand.nextFloat() * 0.4f;
            float brightness = 0.7f + rand.nextFloat() * 0.3f;
            colors[i] = Color.getHSBColor(hue, saturation, brightness);
        }
        return colors;
    }

    public static void setGroupCount(int group) { groupCount = group; }
}