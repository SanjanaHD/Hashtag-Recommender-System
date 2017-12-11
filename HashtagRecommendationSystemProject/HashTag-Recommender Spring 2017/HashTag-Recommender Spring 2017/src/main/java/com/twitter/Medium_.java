
package com.twitter;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Medium_ {

    @Expose
    private Long w;
    @Expose
    private Long h;
    @Expose
    private String resize;

    /**
     * 
     * @return
     *     The w
     */
    public Long getW() {
        return w;
    }

    /**
     * 
     * @param w
     *     The w
     */
    public void setW(Long w) {
        this.w = w;
    }

    /**
     * 
     * @return
     *     The h
     */
    public Long getH() {
        return h;
    }

    /**
     * 
     * @param h
     *     The h
     */
    public void setH(Long h) {
        this.h = h;
    }

    /**
     * 
     * @return
     *     The resize
     */
    public String getResize() {
        return resize;
    }

    /**
     * 
     * @param resize
     *     The resize
     */
    public void setResize(String resize) {
        this.resize = resize;
    }

}
