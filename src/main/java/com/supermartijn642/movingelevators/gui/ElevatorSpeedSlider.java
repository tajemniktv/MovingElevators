package com.supermartijn642.movingelevators.gui;

/**
 * Created 4/3/2020 by SuperMartijn642
 */
//public class ElevatorSpeedSlider extends AbstractSlider {
//
//    private static final int MIN = 1, MAX = 10;
//    private final Consumer<ElevatorSpeedSlider> onChange;
//
//    public ElevatorSpeedSlider(int xIn, int yIn, int widthIn, int heightIn, double currentValue, Consumer<ElevatorSpeedSlider> onChange){
//        super(null, xIn, yIn, widthIn, heightIn, ((int)(currentValue * 10) - MIN) / (double)(MAX - MIN));
//        this.onChange = onChange;
//        this.updateMessage();
//    }
//
//    protected void applyValue(){
//        this.onChange.accept(this);
//    }
//
//    protected void updateMessage(){
//        this.setMessage("Platform speed: " + this.getValue() + " blocks/t");
//    }
//
//    public double getValue(){
//        return ((int)Math.round(this.value * (MAX - MIN)) + MIN) / 10d;
//    }
//
//}
