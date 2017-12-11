
package com.twitter;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Sizes {

    @Expose
    private Medium_ medium;
    @Expose
    private Thumb thumb;
    @Expose
    private Small small;
    @Expose
    private Large large;

    /**
     * 
     * @return
     *     The medium
     */
    public Medium_ getMedium() {
        return medium;
    }

    /**
     * 
     * @param medium
     *     The medium
     */
    public void setMedium(Medium_ medium) {
        this.medium = medium;
    }

    /**
     * 
     * @return
     *     The thumb
     */
    public Thumb getThumb() {
        return thumb;
    }

    /**
     * 
     * @param thumb
     *     The thumb
     */
    public void setThumb(Thumb thumb) {
        this.thumb = thumb;
    }

    /**
     * 
     * @return
     *     The small
     */
    public Small getSmall() {
        return small;
    }

    /**
     * 
     * @param small
     *     The small
     */
    public void setSmall(Small small) {
        this.small = small;
    }

    /**
     * 
     * @return
     *     The large
     */
    public Large getLarge() {
        return large;
    }

    /**
     * 
     * @param large
     *     The large
     */
    public void setLarge(Large large) {
        this.large = large;
    }

}
