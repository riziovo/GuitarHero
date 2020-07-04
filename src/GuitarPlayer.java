import guitarsynth.Instrument;
import guitarsynth.InstrumentPlayer;
import stdRes.StdAudio;
import stdRes.StdDraw;

import java.util.LinkedList;
import java.util.Scanner;

public class GuitarPlayer {
    public static final int totalKeys = 37;

    public static void main(String[] arg)
    {
        Scanner scn = new Scanner(System.in);
        System.out.println("Pick instrument." +
                "\n1. GUITAR" +
                "\n2.HARP" +
                "\n3.DRUMS\nENTER:");
        String name = scn.nextLine();

        String notes = "asbcvdreftgh";
//        String notes = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

        InstrumentPlayer player = new InstrumentPlayer(name,notes);
        player.run();
    }
}
