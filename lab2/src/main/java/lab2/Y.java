package lab2;

import lab2.X;

public class Y extends X {
    protected int yMask = mask(xMask);

    public Y(){fullMask = yMask;}
    public int mask(int orig){
        return (orig & fullMask);
    }

}
