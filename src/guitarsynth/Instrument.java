package guitarsynth;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Map.entry;

public class Instrument {
    public final static Map<String,Integer> INSTRUMENT_SET = Map.ofEntries(
            entry("GUITAR",1),
            entry("HARP",2),
            entry("DRUMS",3),
            entry("PIANO",4)
    );

    private int INSTRUMENT_NOTES_RANGE = 12;
    private int INSTRUMENT_SAMPLE_RATE = 44100;
    private double INSTRUMENT_ENERGY_DECAY_FACTOR = 0.996;
    private double INSTRUMENT_BF_FACTOR = 1.0;


    private final static double HARP_DECAY_FACTOR = -0.996;
    private final static double HARP_BF_FACTOR = 0.5;

    private final static double GUITAR_DECAY_FACTOR = 0.996;
    private final static double GUITAR_BF_FACTOR = 1.0;

    private final static double DRUMS_DECAY_FACTOR = 1.0;
    private final static double DRUMS_BF_FACTOR = 1.0;

    private String instrumentName = "INSTRUMENT";
    private double f;
    private double timestep;
    private double duration;
    private ArrayRingBuffer<Double> buffer;

    private Instrument(double frequency, double INSTRUMENT_BF_FACTOR)
    {
        this.INSTRUMENT_BF_FACTOR = INSTRUMENT_BF_FACTOR;
        this.duration = 1;
        this.f = frequency;
        double cap = (int)(INSTRUMENT_SAMPLE_RATE*duration/this.f)*this.INSTRUMENT_BF_FACTOR;
        buffer = new ArrayRingBuffer((int)cap);
        for (int i = 0; i < buffer.capacity(); i++)
            this.buffer.add(0.00);
        this.timestep = 0;
    }

    public Instrument(double[] buf)
    {
        this.buffer = new ArrayRingBuffer(buf.length);
        this.duration = (double)buf.length/INSTRUMENT_SAMPLE_RATE;
        for (int i = 0; i < buf.length; i++)
            this.buffer.add(buf[i]);

        this.timestep = 0;
    }

    public static Instrument createInstrument(double fq, String name)
    {
        if(name.equals("GUITAR"))
        {
            Instrument Guitar = new Instrument(fq, GUITAR_BF_FACTOR);
            Guitar.INSTRUMENT_ENERGY_DECAY_FACTOR = GUITAR_DECAY_FACTOR;
            Guitar.instrumentName = "GUITAR";
            return Guitar;
        }
        else if(name.equals("HARP"))
        {
            Instrument Harp = new Instrument(fq, HARP_BF_FACTOR);
            Harp.INSTRUMENT_ENERGY_DECAY_FACTOR = HARP_DECAY_FACTOR;
            Harp.instrumentName = "HARP";
            return Harp;
        }
        else if(name.equals("DRUMS"))
        {
            Instrument Drums = new Instrument(fq, DRUMS_BF_FACTOR);
            Drums.INSTRUMENT_ENERGY_DECAY_FACTOR = DRUMS_DECAY_FACTOR;
            Drums.instrumentName = "DRUMS";
            return Drums;
        }

        return null;
    }

    public void pluck()
    {
        int len = this.buffer.capacity();
        this.buffer = null;
        this.buffer = new ArrayRingBuffer<>(len);

        for (int i = 0; i < this.buffer.capacity(); i++)
            this.buffer.add(ThreadLocalRandom.current().nextDouble(-0.5,0.5));
    }

    public void pluckZero()
    {
        int len = this.buffer.capacity();
        this.buffer = null;
        this.buffer = new ArrayRingBuffer<>(len);

        for (int i = 0; i < this.buffer.capacity(); i++)
            this.buffer.add(0.0);

        buffer.resetHeaders();
    }

    public void tic()
    {
        double oldFront = this.buffer.remove();
        oldFront = ((oldFront + this.buffer.get())/2.0)* INSTRUMENT_ENERGY_DECAY_FACTOR;
        this.buffer.add(oldFront);

        this.timestep+=1;
    }

    public double sample()
    {
        return this.buffer.get();
    }

    public void setINSTRUMENT_ENERGY_DECAY_FACTOR(double x)
    {
        this.INSTRUMENT_ENERGY_DECAY_FACTOR = x;
    }

}
