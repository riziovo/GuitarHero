package guitarsynth;

import stdRes.StdAudio;
import stdRes.StdDraw;

import java.util.concurrent.ThreadLocalRandom;

public class InstrumentPlayer {
    Instrument[] strings;
    Instrument[][] guitar;
    String instrumentName;
    String NOTES;
    int POW = 24;
    int GUITAR_NOTES = 37;

    String[] INSTRUMENT_NAME = {"GUITAR", "DRUMS", "HARP"};

    public InstrumentPlayer(String str, String NOTES)
    {
        for(String x: INSTRUMENT_NAME)
        {
            if(x.equals(str))
            {
                this.instrumentName = str;
                break;
            }
        }

        this.NOTES = NOTES;

        if(str.equals("GUITAR"))
        {
            strings = null;
            guitar = new Instrument[6][GUITAR_NOTES];
            for (int i = 0; i < 6; i++)
            {
                for (int j = 0; j < GUITAR_NOTES; j++)
                    guitar[i][j] = Instrument.createInstrument((440.0 * Math.pow(2.0, (j-POW)/12)),str);
            }
        }
        else
        {
            guitar = null;
            strings = new Instrument[37];
            for (int i = 0; i < 37; i++)
                strings[i] = Instrument.createInstrument((440.0 * Math.pow(2.0, (i - POW)/12)),str);
        }
    }

    public void run()
    {
        if(this.instrumentName.equals("GUITAR"))
            this.runGuitar();
        else if(this.instrumentName.equals("DRUMS"))
            this.runDrums();
        else
            this.runInstrumentDefault();
    }

    public void runGuitar()
    {
        int k = 0, keyG;

        while(true)
        {
            double sample = 0.00;
            if(StdDraw.hasNextKeyTyped())
            {
                char key = StdDraw.nextKeyTyped();
                if(Character.isDigit(key))
                {
                    keyG = (int)key;
                    if(keyG > 0 && keyG <7)
                    {
                        k = keyG - 1;
                        this.guitarZeroOut(k);
                    }
                }
                else {
                    int indx =this.NOTES.indexOf(key);
                    if(indx == -1) continue;
                    this.guitar[k][indx].pluck();
                }
            }
            for (Instrument x: this.guitar[k])
                sample += x.sample();

            StdAudio.play(sample);
            for (Instrument x:this.guitar[k]) x.tic();
        }
    }

    public void runInstrumentDefault() {
        while (true) {
            double sample = 0.00;
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int indx = this.NOTES.indexOf(key);
                if (indx == -1) continue;
                this.strings[indx].pluck();
            }
            for (Instrument x : this.strings)
                sample += x.sample();

            StdAudio.play(sample);
            for (Instrument x : this.strings) x.tic();
        }
    }

    public void runDrums()
    {
        while (true) {
            double sample = 0.00;
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int indx = this.NOTES.indexOf(key);
                if (indx == -1) continue;
                this.strings[indx].pluck();
            }
            for (Instrument x : this.strings)
                sample += x.sample();

            StdAudio.play(sample);
            for (Instrument x : this.strings)
            {
                double factor_erg = ThreadLocalRandom.current().nextInt(0,1) > 0? 1.0 : -1.0;
                x.setINSTRUMENT_ENERGY_DECAY_FACTOR(factor_erg);
                x.tic();
            }
        }
    }

    public void guitarZeroOut(int x)
    {
        for (int i = 0; i < this.guitar.length - 1; i++)
        {
            if(i == x) i += 1;
            for (int j = 0; j < this.guitar[i].length; j++)
            {
                this.guitar[i][j].pluckZero();
            }
        }
    }
}
