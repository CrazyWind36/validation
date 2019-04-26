package com.conch.validation.view.camera;

/**
 *
 */
public  class PreviewSize {
    public final float viewW;
    public final float viewH;

    public final int preW;
    public final int preH;

    int degree = 0;

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    PreviewSize(float viewW, float viewH, int preW, int preH) {
        this.viewW = viewW;
        this.viewH = viewH;
        this.preW = preW;
        this.preH = preH;
    }

    public int getPreW() {
        return preW;
    }

    public int getPreH() {
        return preH;
    }

    @Override
    public String toString() {
        return
                "viewW:H=" + (int) viewW + "-" + (int) viewH + " / " + (viewW / viewH) +
                        "  preW:H=" + preW + "-" + preH + " / " + (1f * preW / preH);
    }
}
