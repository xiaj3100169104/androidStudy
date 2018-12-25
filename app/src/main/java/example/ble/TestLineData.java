package example.ble;

public class TestLineData {
    public Float mse;
    public int d1;
    public int d4;
    public float[] signals = new float[4];

    public TestLineData(String[] s) {
        d1 = Integer.valueOf(s[0]);
        d4 = Integer.valueOf(s[1]);
        for (int i = 0; i < 4; i++) {
            signals[i] = Float.valueOf(s[i + 2]);
        }
    }
}
